package com.example.inmobiliaria.ui.inmuebles;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.inmobiliaria.databinding.FragmentInmuebleDetalleBinding;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

public class InmuebleDetalleFragment extends Fragment {

    private FragmentInmuebleDetalleBinding binding;
    private InmuebleDetalleViewModel viewModel;
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

        viewModel = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                cargarDatos();
                configurarObservers();
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

    private void configurarObservers() {
        // FEEDBACK 2: El Fragment es el único que muestra Toasts observando al ViewModel
        viewModel.getmExito().observe(getViewLifecycleOwner(), mensaje -> {
            actualizarColorEstado(binding.swDetalleEstado.isChecked());
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.getmError().observe(getViewLifecycleOwner(), mensaje -> {
            revertirSwitch(binding.swDetalleEstado.isChecked());
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });
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
            viewModel.actualizarDisponibilidad(inmueble, isChecked);
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