package com.sigiep.serviciospublicos.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MainController extends SQLiteOpenHelper {

    private static final String table_usuario = "create table usuario(id_usuario integer PRIMARY KEY AUTOINCREMENT, nombre text, documento text, usuario text, contrasena text)";
    private static final String table_compania = "create table compania(id_compania integer PRIMARY KEY AUTOINCREMENT, nombre text DEFAULT NULL, nit text DEFAULT NULL, correo text DEFAULT NULL, direccion text DEFAULT NULL, telefono text DEFAULT NULL, slogan text DEFAULT NULL, ruta_logo text DEFAULT NULL, ciudad text DEFAULT NULL, codigo_ean text DEFAULT NULL)";
    private static final String table_lectura = "create table lectura(" +
                                                "identificador text DEFAULT NULL, " +
                                                "fecha text DEFAULT NULL, " +
                                                "fecha_vencimiento text DEFAULT NULL, " +
                                                "periodo text DEFAULT NULL, " +
                                                "numero_factura text DEFAULT NULL, " +
                                                "sector text DEFAULT NULL, " +
                                                "codigo_ruta text DEFAULT NULL, " +
                                                "codigo_interno text DEFAULT NULL, " +
                                                "usuario text DEFAULT NULL, " +
                                                "direccion text DEFAULT NULL, " +
                                                "estrato text DEFAULT NULL, " +
                                                "uso text DEFAULT NULL, " +
                                                "numero_medidor text DEFAULT NULL, " +
                                                "consumo_mes_6 text DEFAULT NULL, " +
                                                "consumo_mes_5 text DEFAULT NULL, " +
                                                "consumo_mes_4 text DEFAULT NULL, " +
                                                "consumo_mes_3 text DEFAULT NULL, " +
                                                "consumo_mes_2 text DEFAULT NULL, " +
                                                "consumo_mes_1 text DEFAULT NULL, " +
                                                "promedio text DEFAULT NULL, " +

                                                "consumo_basico text DEFAULT NULL, " +
                                                "mtrs_max_subsidio text DEFAULT NULL, " +
                                                "deuda_anterior text DEFAULT NULL, " +
                                                "atraso text DEFAULT NULL, " +
                                                "estado_medidor text DEFAULT NULL, " +
                                                "casa_vacia text DEFAULT NULL, " +
                                                "lectura_anterior text DEFAULT NULL, " +
                                                "lectura_actual text DEFAULT NULL, " +
                                                "lectura text DEFAULT NULL, " +

                                                "valor_mtr3_acueducto text DEFAULT NULL, " +
                                                "cargo_fijo_acueducto text DEFAULT NULL, " +
                                                "consumo_acueducto text DEFAULT NULL, " +
                                                "contribucion_acueducto text DEFAULT NULL, " +
                                                "intereses_mora_de_acueducto text DEFAULT NULL, " +
                                                "subsidio_acueducto text DEFAULT NULL, " +
                                                "acueducto_concepto1 text DEFAULT NULL, " +
                                                "acueducto_concepto2 text DEFAULT NULL, " +
                                                "acueducto_concepto3 text DEFAULT NULL, " +

                                                "valor_mtr3_alcantarillado text DEFAULT NULL, " +
                                                "cargo_fijo_alcantarillado text DEFAULT NULL, " +
                                                "consumo_alcantarillado text DEFAULT NULL, " +
                                                "contribucion_alcantarillado text DEFAULT NULL, " +
                                                "intereses_mora_de_alcantarillado text DEFAULT NULL, " +
                                                "subsidio_alcantarillado text DEFAULT NULL, " +
                                                "alcantarillado_concepto1 text DEFAULT NULL, " +
                                                "alcantarillado_concepto2 text DEFAULT NULL, " +
                                                "alcantarillado_concepto3 text DEFAULT NULL, " +

                                                "valor_mtr3_aseo text DEFAULT NULL, " +
                                                "cargo_fijo_aseo text DEFAULT NULL, " +
                                                "subsidio_aseo text DEFAULT NULL, " +
                                                "intereses_de_mora_aseo text DEFAULT NULL, " +
                                                "contribucion_aseo text DEFAULT NULL, " +
                                                "aseo_concepto1 text DEFAULT NULL, " +
                                                "aseo_concepto2 text DEFAULT NULL, " +
                                                "aseo_concepto3 text DEFAULT NULL)";


    public MainController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_usuario);
        db.execSQL(table_compania);
        db.execSQL(table_lectura);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_usuario );
        db.execSQL(table_usuario);
    }

    private void abrirConexion(){
        this.getWritableDatabase();
    }

    private void cerrarConexion(){
        this.close();
    }

    public void agregar(String nombre,String documento,String usuario,String contrasena){
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("documento", documento);
        valores.put("usuario", usuario);
        valores.put("contrasena", contrasena);

        this.getWritableDatabase().insert("usuario", null, valores);
    }

    public void agregar_compania(String nombre,String nit,String correo,String direccion,String telefono,String slogan,String ruta_logo,String ciudad,String codigo_ean){
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("nit", nit);
        valores.put("correo", correo);
        valores.put("direccion", direccion);
        valores.put("telefono", telefono);
        valores.put("slogan", slogan);
        valores.put("ruta_logo", ruta_logo);
        valores.put("ciudad", ciudad);
        valores.put("codigo_ean", codigo_ean);

        this.getWritableDatabase().insert("compania", null, valores);
    }

    public Cursor validarLogin(String usuario, String contrasena) throws SQLException {
        Cursor mcursor = null;

        mcursor = this.getReadableDatabase().query("usuario", new String[] {"id_usuario","nombre", "documento", "usuario", "contrasena"},
                "usuario = '"+usuario+"' and contrasena = '"+contrasena+"'",null, null,null,null);

        return mcursor;
    }

    // CONSULTA NÚMERO DE REGISTROS EN TABLA DE BD
    public boolean numeroRegistos(String tabla){
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tabla, null);

        try {
            if(cursor != null)
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    count = cursor.getInt(0);
                }
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if(count>0)
            return false;
        else
            return true;
    }

    public void borrarRegistros(String table){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + table);
    }
}
