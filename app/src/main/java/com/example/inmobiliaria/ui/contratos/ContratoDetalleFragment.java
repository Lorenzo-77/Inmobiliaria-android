package com.example.inmobiliaria.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentContratoDetalleBinding;
import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.modelo.Inmueble;

public class ContratoDetalleFragment extends Fragment {

    private FragmentContratoDetalleBinding binding;
    private ContratoDetalleViewModel viewModel;
    private Contrato contratoGuardado;

    public ContratoDetalleFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContratoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ContratoDetalleViewModel.class);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                viewModel.cargarContrato(inmueble.getIdInmueble());
            }
        }

        viewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            contratoGuardado = contrato;
            binding.tvContratoCodigo.setText("Código de Contrato: " + contrato.getIdContrato());

            String fechaInicio = contrato.getFechaInicio() != null ? contrato.getFechaInicio().split("T")[0] : "";
            String fechaFin = contrato.getFechaFinalizacion() != null ? contrato.getFechaFinalizacion().split("T")[0] : "";

            binding.tvContratoFechaInicio.setText("Fecha de Inicio: " + fechaInicio);
            binding.tvContratoFechaFin.setText("Fecha de Fin: " + fechaFin);

            binding.tvContratoMonto.setText("Monto de Alquiler: $" + contrato.getMontoAlquiler());

            if(contrato.getInquilino() != null){
                binding.tvContratoInquilino.setText("Inquilino: " + contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
            }
        });


        binding.btVerPagos.setOnClickListener(v -> {
            if (contratoGuardado != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contrato", contratoGuardado);

                Navigation.findNavController(v).navigate(R.id.nav_pagos, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}