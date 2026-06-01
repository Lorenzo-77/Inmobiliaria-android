package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Contrato;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<Contrato> contratoActual;

    public InquilinoDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContrato() {
        if (contratoActual == null) {
            contratoActual = new MutableLiveData<>();
        }
        return contratoActual;
    }

    public void cargarContrato(int idInmueble) {
        String token = ApiClient.getToken(getApplication());
        ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();

        Call<Contrato> call = api.obtenerContratoPorInmueble(token, idInmueble);
        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contratoActual.setValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener datos del inquilino", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}