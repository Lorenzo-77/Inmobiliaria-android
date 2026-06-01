package com.example.inmobiliaria.ui.pagos;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Pago;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> listaPagos;

    public PagosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Pago>> getPagos() {
        if (listaPagos == null) {
            listaPagos = new MutableLiveData<>();
        }
        return listaPagos;
    }

    public void cargarPagos(int idContrato) {
        String token = ApiClient.getToken(getApplication());
        ApiClient.ServicioInmobiliaria api = ApiClient.getServicio();

        Call<List<Pago>> call = api.obtenerPagosPorContrato(token, idContrato);
        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPagos.setValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener pagos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}