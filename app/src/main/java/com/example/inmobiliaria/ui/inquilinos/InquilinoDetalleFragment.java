package com.example.inmobiliaria.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentInquilinoDetalleBinding;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.modelo.Inquilino;

public class InquilinoDetalleFragment extends Fragment {

    private FragmentInquilinoDetalleBinding binding;
    private InquilinoDetalleViewModel viewModel;

    public InquilinoDetalleFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquilinoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InquilinoDetalleViewModel.class);


        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {

                viewModel.cargarContrato(inmueble.getIdInmueble());
            }
        }

        viewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            Inquilino inquilino = contrato.getInquilino();
            if (inquilino != null) {
                binding.tvInquilinoCodigo.setText("Código: " + inquilino.getIdInquilino());
                binding.tvInquilinoNombre.setText("Nombre: " + inquilino.getNombre());
                binding.tvInquilinoApellido.setText("Apellido: " + inquilino.getApellido());
                binding.tvInquilinoDni.setText("DNI: " + inquilino.getDni());
                binding.tvInquilinoEmail.setText("Email: " + inquilino.getEmail());
                binding.tvInquilinoTelefono.setText("Teléfono: " + inquilino.getTelefono());

                binding.tvInquilinoLugarTrabajo.setVisibility(View.GONE);
                binding.tvGaranteNombre.setVisibility(View.GONE);
                binding.tvGaranteDni.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}