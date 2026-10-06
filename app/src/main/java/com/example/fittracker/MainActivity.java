package com.example.fittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Pantalla de Bienvenida: logo, mensaje explicativo y botón para ingresar.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Evita que el contenido quede debajo de la barra de estado/navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        Button btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}
