package com.example.inmobiliaria.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inmobiliaria.databinding.FragmentInquilinosBinding;

public class InquilinosFragment extends Fragment {

    private FragmentInquilinosBinding binding;
    private InquilinosViewModel viewModel;

    public InquilinosFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);
        binding.rvInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getInmueblesAlquilados().observe(getViewLifecycleOwner(), inmuebles -> {
            InquilinoAdapter adapter = new InquilinoAdapter(inmuebles, getContext());
            binding.rvInquilinos.setAdapter(adapter);
        });

        viewModel.cargarInmueblesAlquilados();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}