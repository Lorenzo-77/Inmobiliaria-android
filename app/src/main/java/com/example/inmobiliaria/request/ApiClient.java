package com.example.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.modelo.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart; // IMPORT NUEVO
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part; // IMPORT NUEVO
import retrofit2.http.Path;

public class ApiClient {
    public final static String BASE_URL ="https://capacitacion.alwaysdata.net/";

    public static ServicioInmobiliaria getServicio(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ServicioInmobiliaria.class);
    }

    public interface ServicioInmobiliaria {
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("api/Propietarios")
        Call<Propietario> getPropietario(@Header("Authorization") String token);

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPerfil(@Header("Authorization") String token, @Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarContrasena(
                @Header("Authorization") String token,
                @Field("currentPassword") String currentPassword,
                @Field("newPassword") String newPassword
        );

        @GET("api/Inmuebles")
        Call<List<Inmueble>> obtenerInmuebles(@Header("Authorization") String token);

        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> cambiarEstadoInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);

        // Cargar inmueble con imagen (Multipart)
        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> agregarInmueble(
                @Header("Authorization") String token,
                @Part okhttp3.MultipartBody.Part imagen,
                @Part("inmueble") okhttp3.RequestBody inmueble
        );
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (token == null) {
            editor.putString("token", null);
        } else {
            editor.putString("token", "Bearer " + token);
        }
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }
}