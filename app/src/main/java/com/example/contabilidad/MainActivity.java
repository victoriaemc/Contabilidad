package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;
import android.view.Gravity;

public class MainActivity extends AppCompatActivity {

    // Atributos para manejar la BD
    private ContabilidadDbHelper dbHelper;
    private SQLiteDatabase db;

    // Resto de atributos
    private TextView txtCantidad;
    private ScrollView scrollView;
    private TableLayout tableLayout;

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
        scrollView = findViewById(R.id.scrollView);
        tableLayout = findViewById(R.id.tableLayout);

        //initContabilidad();
        updateTxtCantidad(); // Update txtCantidad with the sum of "cantidad" columns
        displayEntries(); // Display all entries in the database
    }

    private void addDataRow(String date, String concept, String amount) {
        // Create a new TableRow
        TableRow row = new TableRow(this);

        // Set layout parameters for the TableRow
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(layoutParams);

        // Create and add TextViews for each column
        row.addView(createTextView(date));
        row.addView(createTextView(concept));
        row.addView(createTextView(amount));

        // Add the TableRow to the TableLayout
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.addView(row);
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f  // Set layout weight to 1 for equal distribution
        ));
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.text_default_material_dark_primary, null));
        textView.setGravity(Gravity.CENTER);

        return textView;
    }


    private void displayEntries() {
        // Query to get all entries ordered by date (most recent to oldest)
        String query = "SELECT * FROM " + ContabilidadContract.ContabilidadEntry.TABLE_NAME +
                " ORDER BY " + ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            // Retrieve data from the cursor
            @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA));
            @SuppressLint("Range") String concepto = cursor.getString(cursor.getColumnIndex(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CONCEPTO));
            @SuppressLint("Range") double cantidad = cursor.getDouble(cursor.getColumnIndex(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD));

            // Use the new addDataRow method to create and add a TableRow with the data
            addDataRow(fecha, concepto, String.format("%.2f", cantidad));
        }

        // Close the cursor
        cursor.close();
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