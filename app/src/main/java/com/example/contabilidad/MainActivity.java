package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // Atributos para manejar la BD
    private ContabilidadDbHelper dbHelper;
    private SQLiteDatabase db;

    // Resto de atributos
    // Pon aquí los campos que veas necesarios en la interfaz gráfica

    private void initContabilidad() {
        // Adición de valores a la BD
        ContentValues values = new ContentValues();

        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA, "2020-10-01");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CONCEPTO, "Compra de comida");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD, -10.0);
        db.insert(ContabilidadContract.ContabilidadEntry.TABLE_NAME, null, values);

        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA, "2020-10-02");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CONCEPTO, "Sueldo");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD, 1000.0);
        db.insert(ContabilidadContract.ContabilidadEntry.TABLE_NAME, null, values);

        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA, "2020-10-03");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CONCEPTO, "Cena con amigos");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD, -50.0);
        db.insert(ContabilidadContract.ContabilidadEntry.TABLE_NAME, null, values);

        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA, "2020-10-04");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CONCEPTO, "Ropa");
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD, -20.0);
        db.insert(ContabilidadContract.ContabilidadEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ContabilidadDbHelper(this, "Contabilidad.db");
        db = dbHelper.getWritableDatabase();

        initContabilidad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}