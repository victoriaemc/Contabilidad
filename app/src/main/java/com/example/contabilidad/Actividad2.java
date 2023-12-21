package com.example.contabilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Actividad2 extends AppCompatActivity {
    private ContabilidadDbHelper dbHelper;
    private SQLiteDatabase db;

    Button btAccept, btGoBack;
    EditText etConcept, etAmount; //etDate;
    private EditText etDate;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);

        dbHelper = new ContabilidadDbHelper(this, "Contabilidad.db");
        db = dbHelper.getWritableDatabase();

        btAccept=(Button) findViewById(R.id.btAccept);
        btGoBack=(Button) findViewById(R.id.btGoBack);

        etConcept=(EditText) findViewById(R.id.etConcept);
        etAmount=(EditText) findViewById(R.id.etAmount);
        etDate=(EditText) findViewById(R.id.etDate);

        selectDate();


        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });

        btGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar(view);
            }
        });

    }

    private void selectDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etDate.setText(updateDate());
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Actividad2.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    private void saveDataToDatabase() {
        String concept = etConcept.getText().toString();
        String amountString = etAmount.getText().toString();
        String dateString = etDate.getText().toString();

        if (concept.isEmpty() || amountString.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.toast_uncomplete, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
            // Optionally, clear the input fields after successful insertion
            etConcept.setText("");
            etAmount.setText("");
            etDate.setText("");
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_fail, Toast.LENGTH_SHORT).show();
        }
    }


    private void regresar(View v){
        finish();
    }
}