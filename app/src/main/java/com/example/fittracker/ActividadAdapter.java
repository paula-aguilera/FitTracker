package com.example.fittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter del RecyclerView que muestra las sesiones registradas.
 */
public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    private final List<Actividad> listaActividades;

    public ActividadAdapter(List<Actividad> listaActividades) {
        this.listaActividades = listaActividades;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividad, parent, false);
        return new ActividadViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = listaActividades.get(position);

        holder.txtTipo.setText(actividad.getTipo());
        holder.txtDuracion.setText(actividad.getMinutos() + " min");
        holder.txtIntensidad.setText("Intensidad: " + actividad.getIntensidad());

        String habitos = "Calentamiento " + marca(actividad.isCalentamiento())
                + "   Hidratación " + marca(actividad.isHidratacion())
                + "   Estiramiento " + marca(actividad.isEstiramiento());
        holder.txtHabitos.setText(habitos);

        holder.ratingActividad.setRating(actividad.getRating());
    }

    private String marca(boolean completado) {
        return completado ? "✔" : "✘";
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public static class ActividadViewHolder extends RecyclerView.ViewHolder {

        TextView txtTipo;
        TextView txtDuracion;
        TextView txtIntensidad;
        TextView txtHabitos;
        RatingBar ratingActividad;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtDuracion = itemView.findViewById(R.id.txtDuracion);
            txtIntensidad = itemView.findViewById(R.id.txtIntensidad);
            txtHabitos = itemView.findViewById(R.id.txtHabitos);
            ratingActividad = itemView.findViewById(R.id.ratingActividad);
        }
    }
}
