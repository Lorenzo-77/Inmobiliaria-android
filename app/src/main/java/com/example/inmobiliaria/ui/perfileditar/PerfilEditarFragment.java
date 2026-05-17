package com.example.inmobiliaria.ui.perfileditar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentPerfilEditarBinding;

public class PerfilEditarFragment extends Fragment {

    private FragmentPerfilEditarBinding binding;
    private PerfilEditarViewModel viewModel;

    public PerfilEditarFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilEditarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PerfilEditarViewModel.class);


        viewModel.getMensajeResultado().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();

                // Si el mensaje indica éxito, limpiamos los campos
                if(mensaje.contains("correctamente")){
                    binding.etClaveActual.setText("");
                    binding.etClaveNueva.setText("");
                    binding.etConfirmarClave.setText("");
                }
            }
        });

        binding.btnCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String claveActual = binding.etClaveActual.getText().toString();
                String claveNueva = binding.etClaveNueva.getText().toString();
                String confirmarClave = binding.etConfirmarClave.getText().toString();

                viewModel.cambiarContrasena(claveActual, claveNueva, confirmarClave);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}