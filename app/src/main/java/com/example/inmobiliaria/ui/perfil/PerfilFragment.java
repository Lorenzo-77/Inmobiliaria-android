package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentPerfilBinding;
import com.example.inmobiliaria.modelo.Propietario;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;
    private boolean enModoEdicion = false;

    public PerfilFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        viewModel.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    binding.tvId.setText("Código de Propietario: " + propietario.getIdPropietario());
                    binding.etDni.setText(propietario.getDni());
                    binding.etNombre.setText(propietario.getNombre());
                    binding.etApellido.setText(propietario.getApellido());
                    binding.etEmail.setText(propietario.getEmail());
                    binding.etTelefono.setText(propietario.getTelefono());
                }
            }
        });

        viewModel.recuperarPerfil();

        binding.btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enModoEdicion) {

                    enModoEdicion = true;
                    binding.btnEditarPerfil.setText("Guardar");

                    binding.etDni.setEnabled(true);
                    binding.etNombre.setEnabled(true);
                    binding.etApellido.setEnabled(true);
                    binding.etEmail.setEnabled(true);
                    binding.etTelefono.setEnabled(true);
                } else {
                    // GUARDAR LOS DATOS Y VOLVER A MODO LECTURA
                    enModoEdicion = false;
                    binding.btnEditarPerfil.setText("Editar Perfil");

                    // Volvemos a bloquear las cajas de texto
                    binding.etDni.setEnabled(false);
                    binding.etNombre.setEnabled(false);
                    binding.etApellido.setEnabled(false);
                    binding.etEmail.setEnabled(false);
                    binding.etTelefono.setEnabled(false);

                    Propietario propietarioEditado = new Propietario();

                    String idTexto = binding.tvId.getText().toString().replace("Código de Propietario: ", "");
                    propietarioEditado.setIdPropietario(Integer.parseInt(idTexto));

                    propietarioEditado.setDni(binding.etDni.getText().toString());
                    propietarioEditado.setNombre(binding.etNombre.getText().toString());
                    propietarioEditado.setApellido(binding.etApellido.getText().toString());
                    propietarioEditado.setEmail(binding.etEmail.getText().toString());
                    propietarioEditado.setTelefono(binding.etTelefono.getText().toString());

                    viewModel.guardarPerfil(propietarioEditado);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}