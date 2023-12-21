package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;
import android.view.Gravity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class MainActivity extends AppCompatActivity {

    // Atributos para manejar la BD
    private ContabilidadDbHelper dbHelper;
    private SQLiteDatabase db;

    // Resto de atributos
    private TextView txtCantidad;
    private ScrollView scrollView;
    private TableLayout tableLayout;

    private FloatingActionButton deleteButton;


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
        deleteButton = findViewById(R.id.floatingActionButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });




        updateTxtCantidad(); // Update txtCantidad with the sum of "cantidad" columns
        displayEntries(); // Display all entries in the database
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the displayed entries when the activity resumes
        displayEntries();
        // Update the txtCantidad after any potential changes
        updateTxtCantidad();
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

        // Remove all rows except the header row
        int childCount = tableLayout.getChildCount();
        for (int i = 1; i < childCount; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                tableLayout.removeView(child);
            }
        }

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

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, so delete all entries
                        deleteAllEntries();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteAllEntries() {
        db.delete(ContabilidadContract.ContabilidadEntry.TABLE_NAME, null, null);
        // Clear the TableLayout to remove all displayed rows
        tableLayout.removeAllViews();
        // Update the txtCantidad after deletion
        updateTxtCantidad();
    }


}