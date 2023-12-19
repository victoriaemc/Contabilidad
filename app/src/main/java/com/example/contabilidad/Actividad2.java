package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

public class Actividad2 extends AppCompatActivity {

    Button btAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);

        btAceptar=(Button) findViewById(R.id.btAceptar);

        btAceptar.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               Toast.makeText(getApplicationContext(), "Agregado exitosamente", Toast.LENGTH_SHORT).show();
           }
        });
    }

    private void regresar(View v){
        finish();
    }
}