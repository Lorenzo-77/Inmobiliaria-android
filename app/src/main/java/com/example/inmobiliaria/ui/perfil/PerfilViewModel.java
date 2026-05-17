package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> mPropietario;
    private Context context;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Propietario> getPropietario() {
        if (mPropietario == null) {
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    public void recuperarPerfil() {
        String token = ApiClient.getToken(context);

        if (token != null) {
            ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();
            Call<Propietario> call = api.getPropietario(token);

            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mPropietario.setValue(response.body());
                    } else {
                        Toast.makeText(context, "Error al obtener perfil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Sesión expirada o no encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarPerfil(Propietario propietarioEditado) {
        String token = ApiClient.getToken(context);

        if (token != null) {
            ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();
            Call<Propietario> call = api.actualizarPerfil(token, propietarioEditado);

            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();

                        mPropietario.setValue(response.body());
                    } else {
                        Toast.makeText(context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Sesión expirada o no encontrada", Toast.LENGTH_SHORT).show();
        }
    }
}