package com.example.inmobiliaria.ui.pagos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inmobiliaria.databinding.FragmentPagosBinding;
import com.example.inmobiliaria.modelo.Contrato;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding binding;
    private PagosViewModel viewModel;

    public PagosFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        binding.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            Contrato contrato = (Contrato) getArguments().getSerializable("contrato");
            if (contrato != null) {
                viewModel.cargarPagos(contrato.getIdContrato());
            }
        }

        viewModel.getPagos().observe(getViewLifecycleOwner(), pagos -> {
            PagoAdapter adapter = new PagoAdapter(pagos, getContext());
            binding.rvPagos.setAdapter(adapter);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}