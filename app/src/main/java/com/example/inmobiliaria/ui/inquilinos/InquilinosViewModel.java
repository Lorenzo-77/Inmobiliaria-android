package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;
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

public class InquilinosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> inmueblesAlquilados;
    private MutableLiveData<String> mError;

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmueblesAlquilados() {
        if (inmueblesAlquilados == null) inmueblesAlquilados = new MutableLiveData<>();
        return inmueblesAlquilados;
    }

    public LiveData<String> getmError() {
        if (mError == null) mError = new MutableLiveData<>();
        return mError;
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
                    mError.setValue("Error al obtener inmuebles alquilados");
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                mError.setValue("Fallo de conexión");
            }
        });
    }
}