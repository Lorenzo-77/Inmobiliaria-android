package com.example.inmobiliaria.ui.inquilinos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.ViewHolder> {

    private List<Inmueble> inmueblesAlquilados;
    private Context context;

    public InquilinoAdapter(List<Inmueble> inmueblesAlquilados, Context context) {
        this.inmueblesAlquilados = inmueblesAlquilados;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = inmueblesAlquilados.get(position);

        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText("Propiedad Alquilada");
        holder.tvEstado.setText("");

        String urlImagen = ApiClient.BASE_URL + inmueble.getImagen();
        Glide.with(context)
                .load(urlImagen)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivImagen);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);

            Navigation.findNavController(v).navigate(R.id.nav_inquilino_detalle, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return inmueblesAlquilados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDireccion, tvPrecio, tvEstado;
        ImageView ivImagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            ivImagen = itemView.findViewById(R.id.ivImagenInmueble);
        }
    }
}