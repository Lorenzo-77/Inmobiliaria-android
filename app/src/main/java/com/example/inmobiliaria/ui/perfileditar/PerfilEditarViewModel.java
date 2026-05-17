package com.example.inmobiliaria.ui.perfileditar;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilEditarViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensajeResultado;
    private Context context;

    public PerfilEditarViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getMensajeResultado() {
        if (mensajeResultado == null) {
            mensajeResultado = new MutableLiveData<>();
        }
        return mensajeResultado;
    }

    public void cambiarContrasena(String claveActual, String claveNueva, String confirmarClave) {

        if (claveActual.isEmpty() || claveNueva.isEmpty() || confirmarClave.isEmpty()) {
            mensajeResultado.setValue("Por favor, complete todos los campos.");
            return;
        }

        if (!claveNueva.equals(confirmarClave)) {
            mensajeResultado.setValue("Las contraseñas nuevas no coinciden.");
            return;
        }

        String token = ApiClient.getToken(context);
        if (token == null) {
            mensajeResultado.setValue("Error de sesión. Vuelva a iniciar sesión.");
            return;
        }

        ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();
        Call<Void> call = api.cambiarContrasena(token, claveActual, claveNueva);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mensajeResultado.setValue("¡Contraseña actualizada correctamente!");
                } else {
                    mensajeResultado.setValue("Error al cambiar la contraseña. Verifique su clave actual.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensajeResultado.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}