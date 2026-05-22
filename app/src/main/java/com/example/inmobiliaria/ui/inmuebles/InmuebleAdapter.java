package com.example.inmobiliaria.ui.inmuebles;

import android.content.Context;
import android.graphics.Color;
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

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolder> {

    private List<Inmueble> listaInmuebles;
    private Context context;

    public InmuebleAdapter(List<Inmueble> listaInmuebles, Context context) {
        this.listaInmuebles = listaInmuebles;
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
        Inmueble inmueble = listaInmuebles.get(position);

        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText("$ " + inmueble.getValor());

        if (inmueble.isDisponible()) {
            holder.tvEstado.setText("Disponible");
            holder.tvEstado.setTextColor(Color.parseColor("#4CAF50")); // Verde
        } else {
            holder.tvEstado.setText("No Disponible");
            holder.tvEstado.setTextColor(Color.parseColor("#F44336")); // Rojo
        }

        String urlImagen = ApiClient.BASE_URL + inmueble.getImagen();
        Glide.with(context)
                .load(urlImagen)
                .placeholder(android.R.drawable.ic_menu_gallery) // Icono por defecto mientras carga
                .into(holder.ivImagen);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);
            Navigation.findNavController(v).navigate(R.id.nav_inmueble_detalle, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
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