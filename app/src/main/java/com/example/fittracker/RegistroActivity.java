package com.example.fittracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Pantalla principal: formulario de registro de actividad,
 * resumen de la meta diaria y listado de sesiones (RecyclerView).
 */
public class RegistroActivity extends AppCompatActivity {

    private static final int META_DIARIA_MINUTOS = 60;
    private static final String KEY_LISTA = "lista_actividades";

    private Spinner spinnerTipo;
    private EditText edtDuracion;
    private RadioGroup rgIntensidad;
    private CheckBox chkCalentamiento, chkHidratacion, chkEstiramiento;
    private RatingBar ratingDificultad;
    private ProgressBar progressMeta;
    private TextView txtTotalSesiones, txtTotalMinutos, txtMeta, txtPorcentaje, txtVacio;

    private ArrayList<Actividad> listaActividades;
    private ActividadAdapter adapter;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        // ----- Referencias a las vistas -----
        spinnerTipo = findViewById(R.id.spinnerTipo);
        edtDuracion = findViewById(R.id.edtDuracion);
        rgIntensidad = findViewById(R.id.rgIntensidad);
        chkCalentamiento = findViewById(R.id.chkCalentamiento);
        chkHidratacion = findViewById(R.id.chkHidratacion);
        chkEstiramiento = findViewById(R.id.chkEstiramiento);
        ratingDificultad = findViewById(R.id.ratingDificultad);
        progressMeta = findViewById(R.id.progressMeta);
        txtTotalSesiones = findViewById(R.id.txtTotalSesiones);
        txtTotalMinutos = findViewById(R.id.txtTotalMinutos);
        txtMeta = findViewById(R.id.txtMeta);
        txtPorcentaje = findViewById(R.id.txtPorcentaje);
        txtVacio = findViewById(R.id.txtVacio);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        RecyclerView recyclerSesiones = findViewById(R.id.recyclerSesiones);

        // ----- Spinner: tipos de entrenamiento -----
        ArrayAdapter<CharSequence> adapterTipos = ArrayAdapter.createFromResource(
                this, R.array.tipos_entrenamiento, android.R.layout.simple_spinner_item);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);

        // ----- Ciclo de vida: recuperar la lista si la Activity se recreó (ej. rotación) -----
        if (savedInstanceState != null && savedInstanceState.getSerializable(KEY_LISTA) != null) {
            listaActividades = (ArrayList<Actividad>) savedInstanceState.getSerializable(KEY_LISTA);
        } else {
            listaActividades = new ArrayList<>();
        }

        // ----- RecyclerView -----
        adapter = new ActividadAdapter(listaActividades);
        recyclerSesiones.setLayoutManager(new LinearLayoutManager(this));
        recyclerSesiones.setAdapter(adapter);

        progressMeta.setMax(META_DIARIA_MINUTOS);
        txtMeta.setText(META_DIARIA_MINUTOS + " min");
        actualizarResumen();

        btnGuardar.setOnClickListener(v -> guardarSesion());
    }

    /** Captura los valores del formulario, valida y agrega la sesión al listado. */
    private void guardarSesion() {
        // Spinner
        String tipo = spinnerTipo.getSelectedItem().toString();

        // Duración
        String textoDuracion = edtDuracion.getText().toString().trim();
        if (TextUtils.isEmpty(textoDuracion) || Integer.parseInt(textoDuracion) <= 0) {
            edtDuracion.setError(getString(R.string.error_duracion));
            edtDuracion.requestFocus();
            return;
        }
        int minutos = Integer.parseInt(textoDuracion);

        // RadioGroup
        int idSeleccionado = rgIntensidad.getCheckedRadioButtonId();
        if (idSeleccionado == -1) {
            Toast.makeText(this, R.string.error_intensidad, Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton rbSeleccionado = findViewById(idSeleccionado);
        String intensidad = rbSeleccionado.getText().toString();

        // RatingBar
        float rating = ratingDificultad.getRating();
        if (rating < 1) {
            Toast.makeText(this, R.string.error_rating, Toast.LENGTH_SHORT).show();
            return;
        }

        // CheckBox
        Actividad actividad = new Actividad(
                tipo,
                minutos,
                intensidad,
                chkCalentamiento.isChecked(),
                chkHidratacion.isChecked(),
                chkEstiramiento.isChecked(),
                rating);

        listaActividades.add(actividad);
        adapter.notifyItemInserted(listaActividades.size() - 1);

        actualizarResumen();
        limpiarFormulario();
        Toast.makeText(this, R.string.msg_guardado, Toast.LENGTH_SHORT).show();
    }

    /** Actualiza la ProgressBar y la tabla de resumen. */
    private void actualizarResumen() {
        int totalMinutos = 0;
        for (Actividad a : listaActividades) {
            totalMinutos += a.getMinutos();
        }

        int porcentaje = Math.min(100, totalMinutos * 100 / META_DIARIA_MINUTOS);

        progressMeta.setProgress(Math.min(totalMinutos, META_DIARIA_MINUTOS));
        txtTotalSesiones.setText(String.valueOf(listaActividades.size()));
        txtTotalMinutos.setText(totalMinutos + " min");
        txtPorcentaje.setText(porcentaje + "%");

        txtVacio.setVisibility(listaActividades.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void limpiarFormulario() {
        spinnerTipo.setSelection(0);
        edtDuracion.setText("");
        rgIntensidad.clearCheck();
        chkCalentamiento.setChecked(false);
        chkHidratacion.setChecked(false);
        chkEstiramiento.setChecked(false);
        ratingDificultad.setRating(0);
    }

    /** Ciclo de vida: guarda la lista antes de que la Activity se destruya. */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LISTA, listaActividades);
    }
}
