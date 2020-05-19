package com.sigiep.serviciospublicos.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UsuarioController extends SQLiteOpenHelper {

    private static final String nombre_bd = "Servicios_publicos";
    private static final int version_bd = 1;
    private static final String table_usuario = "create table usuario(id_usuario integer PRIMARY KEY AUTOINCREMENT, nombre text, documento text, usuario text, contrasena text)";

    public UsuarioController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_usuario);
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
}
