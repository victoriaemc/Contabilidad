package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

public class Actividad2 extends AppCompatActivity {
    private ContabilidadDbHelper dbHelper;
    private SQLiteDatabase db;

    Button btAccept;
    EditText etConcept, etAmount, etDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);

        dbHelper = new ContabilidadDbHelper(this, "Contabilidad.db");
        db = dbHelper.getWritableDatabase();

        btAccept=(Button) findViewById(R.id.btAccept);
        etConcept=(EditText) findViewById(R.id.etConcept);
        etAmount=(EditText) findViewById(R.id.etAmount);
        etDate=(EditText) findViewById(R.id.etDate);


        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });

    }

    private void saveDataToDatabase() {
        String concept = etConcept.getText().toString();
        String amountString = etAmount.getText().toString();
        String dateString = etDate.getText().toString();

        if (concept.isEmpty() || amountString.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);

        ContentValues values = new ContentValues();
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_FECHA, dateString);
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CONCEPTO, concept);
        values.put(ContabilidadContract.ContabilidadEntry.COLUMN_NAME_CANTIDAD, amount);

        long newRowId = db.insert(
                ContabilidadContract.ContabilidadEntry.TABLE_NAME,
                null,
                values
        );

        if (newRowId != -1) {
            Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
            // Optionally, clear the input fields after successful insertion
            etConcept.setText("");
            etAmount.setText("");
            etDate.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Failed to add data", Toast.LENGTH_SHORT).show();
        }
    }


    private void regresar(View v){
        finish();
    }
}