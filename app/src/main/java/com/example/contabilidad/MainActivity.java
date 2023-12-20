package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Atributos para manejar la BD
    private ContabilidadDbHelper dbHelper;
    private SQLiteDatabase db;

    // Resto de atributos
    private TextView txtCantidad;

/*    private void initContabilidad() {
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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ContabilidadDbHelper(this, "Contabilidad.db");
        db = dbHelper.getWritableDatabase();

        // Initialize TextView
        txtCantidad = findViewById(R.id.txtCantidad);

        //initContabilidad();
        updateTxtCantidad(); // Update txtCantidad with the sum of "cantidad" columns
    }

    private void updateTxtCantidad() {
        // Query to get the sum of "cantidad" columns
        String query = "SELECT SUM(" + ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD + ") FROM " +
                ContabilidadContract.ContabilidadEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Get the sum from the first column of the result set
            double sum = cursor.getDouble(0);

            // Update txtCantidad with the sum
            txtCantidad.setText(String.format("%.2f", sum));
        }

        // Close the cursor
        cursor.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public void ingresar(View v) {
        Intent intento = new Intent(this, Actividad2.class);
        startActivity(intento);
    }
}