package com.sigiep.serviciospublicos.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sigiep.serviciospublicos.models.LecturaEntity;
import com.sigiep.serviciospublicos.utilities.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class MainController extends SQLiteOpenHelper {

    public MainController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.table_usuario);
        db.execSQL(Utilidades.table_compania);
        db.execSQL(Utilidades.table_lectura);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_usuario+"'");
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_compania+"'");
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_lectura+"'");
        db.execSQL(Utilidades.table_usuario);
        db.execSQL(Utilidades.table_compania);
        db.execSQL(Utilidades.table_lectura);
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

    public void guardarLectura(String lecturaActual,String lectura,String acueductoConsumo,String acueductoSubsidio,String alcantarilladoConsumo,String alcantarilladoSudsidio, String codigoRuta){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update lectura set lectura_actual = '"+lecturaActual+"', " +
                "lectura = '"+lectura+"', " +
                "consumo_acueducto = '"+acueductoConsumo+"', " +
                "subsidio_acueducto = '"+acueductoSubsidio+"', " +
                "consumo_alcantarillado = '"+alcantarilladoConsumo+"', " +
                "subsidio_alcantarillado = '"+alcantarilladoSudsidio+"' " +
                "where codigo_ruta = '"+codigoRuta+"'");
    }

    public void checkDanado(String estado, String codigoRuta){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update lectura set estado_medidor = '"+estado+"' where codigo_ruta = '"+codigoRuta+"'");
    }

    public void checkCasaVacia(String estado, String codigoRuta){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update lectura set casa_vacia = '"+estado+"' where codigo_ruta = '"+codigoRuta+"'");
    }

    public List<LecturaEntity> findAllByCodRuta(String codigoRuta){
        SQLiteDatabase db = getReadableDatabase();

        String a = "select * from lectura where codigo_ruta = '"+codigoRuta+"'";
        Cursor cursor = db.rawQuery(a, null);
        List<LecturaEntity> lectura = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                lectura.add(new LecturaEntity(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),

                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getString(18),
                        cursor.getString(19),

                        cursor.getString(20),
                        cursor.getString(21),
                        cursor.getString(22),
                        cursor.getString(23),
                        cursor.getString(24),
                        cursor.getString(25),
                        cursor.getString(26),
                        cursor.getString(27),
                        cursor.getString(28),
                        cursor.getString(29),

                        cursor.getString(30),
                        cursor.getString(31),
                        cursor.getString(32),
                        cursor.getString(33),
                        cursor.getString(34),
                        cursor.getString(35),
                        cursor.getString(36),
                        cursor.getString(37),
                        cursor.getString(38),
                        cursor.getString(39),

                        cursor.getString(40),
                        cursor.getString(41),
                        cursor.getString(42),
                        cursor.getString(43),
                        cursor.getString(44),
                        cursor.getString(45),
                        cursor.getString(46),
                        cursor.getString(47),
                        cursor.getString(48),
                        cursor.getString(49),

                        cursor.getString(50),
                        cursor.getString(51),
                        cursor.getString(52),
                        cursor.getString(53),
                        cursor.getString(54),
                        cursor.getString(55),
                        cursor.getString(56)
                        ));
            }while (cursor.moveToNext());

        }
        cursor.close();

        return lectura;
    }

}
