package com.example.inmobiliaria.ui.inmuebles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentInmueblesAgregarBinding;

public class InmueblesAgregarFragment extends Fragment {
    private InmueblesAgregarViewModel vm;
    private FragmentInmueblesAgregarBinding b;
    private ActivityResultLauncher<Intent> selector;
    private Intent intent;

    public static InmueblesAgregarFragment newInstance() {
        return new InmueblesAgregarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentInmueblesAgregarBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InmueblesAgregarViewModel.class);

        vm.getmUri().observe(getViewLifecycleOwner(), uri -> {
            b.ivFoto.setImageURI(uri);
        });

        b.btCargarImagen.setOnClickListener(view -> {
            selector.launch(intent);
        });

        b.btGuardar.setOnClickListener(view -> {
            vm.cargarInmueble(
                    b.etDireccion.getText().toString(),
                    b.etUso.getText().toString(),
                    b.etTipo.getText().toString(),
                    b.etAmbientes.getText().toString(),
                    b.etSuperficie.getText().toString(),
                    b.etValor.getText().toString()
            );
        });

        abrirGaleria();
        return b.getRoot();
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult resultado) {
                vm.recibirFoto(resultado);
                Log.d("galeria", "onActivityResult: " + resultado.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}