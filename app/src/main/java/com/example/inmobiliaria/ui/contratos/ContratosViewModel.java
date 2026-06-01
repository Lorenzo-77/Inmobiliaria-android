package com.example.inmobiliaria.ui.contratos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> inmueblesAlquilados;

    public ContratosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmueblesAlquilados() {
        if (inmueblesAlquilados == null) {
            inmueblesAlquilados = new MutableLiveData<>();
        }
        return inmueblesAlquilados;
    }

    public void cargarInmueblesAlquilados() {
        String token = ApiClient.getToken(getApplication());
        ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();

        Call<List<Inmueble>> call = api.obtenerInmueblesAlquilados(token);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmueblesAlquilados.setValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener contratos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}