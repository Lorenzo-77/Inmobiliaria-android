package com.example.inmobiliaria.ui.inmuebles;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.databinding.FragmentInmuebleDetalleBinding;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleFragment extends Fragment {

    private FragmentInmuebleDetalleBinding binding;
    private Inmueble inmueble;

    public InmuebleDetalleFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                cargarDatos();
                configurarSwitch();
            }
        }
    }

    private void cargarDatos() {
        binding.tvDetalleDireccion.setText("Dirección: " + inmueble.getDireccion());
        binding.tvDetalleValor.setText("Valor: $ " + inmueble.getValor());
        binding.tvDetalleAmbientes.setText("Ambientes: " + inmueble.getAmbientes());

        binding.tvDetalleSuperficie.setText("Superficie: " + inmueble.getSuperficie());

        binding.tvDetalleUso.setText("Uso: " + inmueble.getUso());
        binding.tvDetalleTipo.setText("Tipo: " + inmueble.getTipo());
        actualizarColorEstado(inmueble.isDisponible());

        String urlImagen = ApiClient.BASE_URL + inmueble.getImagen();
        Glide.with(this)
                .load(urlImagen)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivDetalleImagen);
    }

    private void actualizarColorEstado(boolean estaDisponible) {
        if (estaDisponible) {
            binding.tvDetalleEstado.setText("Estado: Disponible");
            binding.tvDetalleEstado.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            binding.tvDetalleEstado.setText("Estado: No Disponible");
            binding.tvDetalleEstado.setTextColor(Color.parseColor("#F44336"));
        }
    }

    private void configurarSwitch() {
        binding.swDetalleEstado.setOnCheckedChangeListener(null);
        binding.swDetalleEstado.setChecked(inmueble.isDisponible());

        binding.swDetalleEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String token = ApiClient.getToken(getContext());
            if (token != null) {
                inmueble.setDisponible(isChecked);

                ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();
                Call<Inmueble> call = api.cambiarEstadoInmueble(token, inmueble);

                call.enqueue(new Callback<Inmueble>() {
                    @Override
                    public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                        if (response.isSuccessful()) {
                            actualizarColorEstado(isChecked);
                            Toast.makeText(getContext(), "Disponibilidad actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            revertirSwitch(isChecked);
                            Toast.makeText(getContext(), "Error al actualizar en servidor", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable t) {
                        revertirSwitch(isChecked);
                        Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void revertirSwitch(boolean estadoFallido) {
        inmueble.setDisponible(!estadoFallido);
        binding.swDetalleEstado.setOnCheckedChangeListener(null);
        binding.swDetalleEstado.setChecked(!estadoFallido);
        configurarSwitch();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}