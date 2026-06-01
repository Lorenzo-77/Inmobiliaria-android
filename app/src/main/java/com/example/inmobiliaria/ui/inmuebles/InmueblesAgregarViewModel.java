package com.example.inmobiliaria.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesAgregarViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> mUri;
    private MutableLiveData<String> mMensaje; // NUEVO: Variable para los avisos

    public InmueblesAgregarViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getmUri() {
        if(mUri == null){
            mUri = new MutableLiveData<>();
        }
        return mUri;
    }

    // NUEVO: Getter para que el Fragment pueda escuchar los mensajes
    public LiveData<String> getMensaje() {
        if(mMensaje == null){
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public void recibirFoto(ActivityResult resultado) {
        if (resultado.getResultCode() == RESULT_OK) {
            Intent data = resultado.getData();
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                if(mUri == null) mUri = new MutableLiveData<>();
                mUri.setValue(uri);
            }
        }
    }

    public void cargarInmueble(String direccion, String uso, String tipo,
                               String ambientes, String superficie, String valor, boolean disponible){
        try {
            if(!direccion.isEmpty() || !uso.isEmpty() || !tipo.isEmpty() || !ambientes.isEmpty()
                    || !superficie.isEmpty() || !valor.isEmpty()){

                Inmueble i = new Inmueble();
                i.setDireccion(direccion);
                i.setUso(uso);
                i.setTipo(tipo);
                i.setAmbientes(Integer.parseInt(ambientes));
                i.setValor(Double.parseDouble(valor));
                i.setSuperficie(Integer.parseInt(superficie));
                i.setDisponible(disponible);

                byte[] imagen = transformarImagen();
                if (imagen.length == 0){
                    mMensaje.setValue("Debe ingresar una imagen"); // FEEDBACK 2
                    return;
                }

                String inmuebleJson = new Gson().toJson(i);
                RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
                MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);

                ApiClient.ServicioInmobiliaria si = ApiClient.getServicio();
                Call<Inmueble> call = si.agregarInmueble(ApiClient.getToken(getApplication()), imagenPart, inmuebleBody);

                call.enqueue(new Callback<Inmueble>() {
                    @Override
                    public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                        if (response.isSuccessful()){
                            mMensaje.setValue("Inmueble guardado correctamente"); // FEEDBACK 2
                        } else {
                            mMensaje.setValue("Error al cargar inmueble"); // FEEDBACK 2
                        }
                    }

                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable t) {
                        mMensaje.setValue("Fallo de conexión"); // FEEDBACK 2
                    }
                });
            } else {
                mMensaje.setValue("Debe llenar todos los campos."); // FEEDBACK 2
            }
        } catch (NumberFormatException e){
            mMensaje.setValue("Superficie, ambientes y valor deben ser numéricos"); // FEEDBACK 2
        }
    }

    private byte[] transformarImagen(){
        try {
            if (mUri == null || mUri.getValue() == null) return new byte[]{};
            Uri uri = mUri.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            mMensaje.setValue("Error al procesar la foto");
            return new byte[]{};
        }
    }
}