package com.example.contabilidad;

import android.provider.BaseColumns;

public final class ContabilidadContract {

    private ContabilidadContract() {}

    public static abstract class ContabilidadEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contabilidad";
        public static final String COLUMN_NAME_FECHA = "fecha";
        public static final String COLUMN_NAME_CONCEPTO = "concepto";
        public static final String COLUMN_NAME_CANTIDAD = "cantidad";
    }

}
