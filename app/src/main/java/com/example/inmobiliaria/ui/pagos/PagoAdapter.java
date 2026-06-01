package com.example.inmobiliaria.ui.pagos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelo.Pago;

import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolder> {

    private List<Pago> listaPagos;
    private Context context;

    public PagoAdapter(List<Pago> listaPagos, Context context) {
        this.listaPagos = listaPagos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pago, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = listaPagos.get(position);
        holder.tvPagoNumero.setText("Detalle: " + pago.getDetalle());

        String fecha = pago.getFechaPago() != null ? pago.getFechaPago().split("T")[0] : "";
        holder.tvPagoFecha.setText("Fecha: " + fecha);

        holder.tvPagoImporte.setText("Importe: $" + pago.getMonto());
    }

    @Override
    public int getItemCount() {
        return listaPagos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPagoNumero, tvPagoFecha, tvPagoImporte;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPagoNumero = itemView.findViewById(R.id.tvPagoNumero);
            tvPagoFecha = itemView.findViewById(R.id.tvPagoFecha);
            tvPagoImporte = itemView.findViewById(R.id.tvPagoImporte);
        }
    }
}