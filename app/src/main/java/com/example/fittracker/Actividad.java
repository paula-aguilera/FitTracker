package com.example.fittracker;

import java.io.Serializable;

/**
 * Modelo de una sesión de entrenamiento registrada.
 * Implementa Serializable para poder guardarla en el Bundle
 * y no perder la lista al rotar la pantalla (ciclo de vida).
 */
public class Actividad implements Serializable {

    private final String tipo;
    private final int minutos;
    private final String intensidad;
    private final boolean calentamiento;
    private final boolean hidratacion;
    private final boolean estiramiento;
    private final float rating;

    public Actividad(String tipo,
                     int minutos,
                     String intensidad,
                     boolean calentamiento,
                     boolean hidratacion,
                     boolean estiramiento,
                     float rating) {
        this.tipo = tipo;
        this.minutos = minutos;
        this.intensidad = intensidad;
        this.calentamiento = calentamiento;
        this.hidratacion = hidratacion;
        this.estiramiento = estiramiento;
        this.rating = rating;
    }

    public String getTipo() { return tipo; }

    public int getMinutos() { return minutos; }

    public String getIntensidad() { return intensidad; }

    public boolean isCalentamiento() { return calentamiento; }

    public boolean isHidratacion() { return hidratacion; }

    public boolean isEstiramiento() { return estiramiento; }

    public float getRating() { return rating; }
}
