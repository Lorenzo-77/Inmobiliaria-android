package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<String> mExito;
    private MutableLiveData<String> mError;

    public InmuebleDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getmExito() {
        if (mExito == null) mExito = new MutableLiveData<>();
        return mExito;
    }

    public LiveData<String> getmError() {
        if (mError == null) mError = new MutableLiveData<>();
        return mError;
    }

    public void actualizarDisponibilidad(Inmueble inmueble, boolean isChecked) {
        String token = ApiClient.getToken(getApplication());
        if (token != null) {
            inmueble.setDisponible(isChecked);

            ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();
            Call<Inmueble> call = api.cambiarEstadoInmueble(token, inmueble);

            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful()) {

                        mExito.setValue("Disponibilidad actualizada");
                    } else {
                        mError.setValue("Error al actualizar en servidor");
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    mError.setValue("Fallo de conexión");
                }
            });
        }
    }
}