package com.example.inmobiliaria;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewModelLoginActivity extends AndroidViewModel {
    private MutableLiveData<String> mensaje;
    private Context context;

    public ViewModelLoginActivity(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getMensaje(){
        if(mensaje == null){
            mensaje = new MutableLiveData<>();
        }
        return mensaje;
    }

    public void recuperarDatos(String email, String password) {
        if(email.isEmpty() || password.isEmpty()){
            mensaje.setValue("Por favor, complete todos los campos");
        } else {
            ApiClient.ServicioInmobiliaria servicio = ApiClient.getServicio();
            Call<String> call = servicio.login(email, password);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    if (response.isSuccessful()) {
                        String token = response.body();
                        Log.d("token", token);


                        ApiClient.setToken(context, token);


                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        mensaje.setValue("Usuario o contraseña incorrectos");
                        Log.d("Error", response.message());
                        Log.d("Error", response.code()+"");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mensaje.setValue("Error de conexión con el servidor");
                    Log.d("mensaje", t.getMessage());
                }
            });
        }
    }
}