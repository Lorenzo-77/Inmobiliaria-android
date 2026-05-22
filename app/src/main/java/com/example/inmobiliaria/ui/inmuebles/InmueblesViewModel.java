package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.content.Context;
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

public class InmueblesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> mInmuebles;
    private Context context;

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Inmueble>> getInmuebles() {
        if (mInmuebles == null) {
            mInmuebles = new MutableLiveData<>();
        }
        return mInmuebles;
    }

    public void cargarInmuebles() {
        String token = ApiClient.getToken(context);
        if (token != null) {
            ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();
            Call<List<Inmueble>> call = api.obtenerInmuebles(token);

            call.enqueue(new Callback<List<Inmueble>>() {
                @Override
                public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mInmuebles.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No se pudieron cargar los inmuebles", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                    Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}