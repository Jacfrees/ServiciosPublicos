package com.example.serviciospublicos.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.serviciospublicos.Login;
import com.example.serviciospublicos.Utilidades.Utilidades;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Maincontroller extends SQLiteOpenHelper {



    public Maincontroller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.table_usuario);
        db.execSQL(Utilidades.table_compania);
        db.execSQL(Utilidades.table_lectura);
        db.execSQL(Utilidades.table_eventualidad);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_usuario+"'");
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_compania+"'");
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_lectura+"'");
        db.execSQL("DROP TABLE IF EXISTS '"+Utilidades.table_eventualidad+"'");
        db.execSQL(Utilidades.table_usuario);
        db.execSQL(Utilidades.table_compania);
        db.execSQL(Utilidades.table_lectura);
        db.execSQL(Utilidades.table_eventualidad);
    }

    public void agregar(Integer id_unico,String usuario,String contrasen,String fechaactualizacion,String observaciones,Integer rol,Integer tercero,Integer estado){
        ContentValues valores = new ContentValues();
        valores.put("id_unico", id_unico);
        valores.put("usuario", usuario);
        valores.put("contrasen", contrasen);
        valores.put("fechaactualizacion", fechaactualizacion);
        valores.put("observaciones", observaciones);
        valores.put("rol", rol);
        valores.put("tercero", tercero);
        valores.put("estado", estado);

        this.getWritableDatabase().insertOrThrow("usuario", null, valores);
    }

    public void agregarEventualidad(Integer idUnico,String nombre,Integer tipoEventualidad,Integer valor){
        ContentValues valores = new ContentValues();
        valores.put("id_unico", idUnico);
        valores.put("nombre", nombre);
        valores.put("tipo_eventualidad", tipoEventualidad);
        valores.put("valor", valor);
        this.getWritableDatabase().insertOrThrow("eventualidad", null, valores);
    }


    public void agregar_compania(String nombre,String nit,String correo,String direccion,String telefono,String slogan,String ruta_logo,String ciudad,String codigo_ean,String nuir){
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
        valores.put("nuir", nuir);

        this.getWritableDatabase().insert("compania", null, valores);
    }

    public Cursor validarLogin(String usuario, String contrasena) throws SQLException {
        Cursor mcursor = null;
        mcursor = this.getReadableDatabase().query("usuario", new String[] {"id_unico","usuario", "contrasen", "fechaactualizacion", "observaciones", "rol", "tercero", "estado"},
                "usuario = '"+usuario+"' and contrasen = '"+contrasena+"'",null, null,null,null);
        return mcursor;
    }

    public Cursor validarEventualidad(Integer id_unico) throws SQLException {
        Cursor mcursor = null;
        mcursor = this.getReadableDatabase().query("eventualidad", new String[] {"id_unico","nombre", "tipo_eventualidad", "valor"},
                "id_unico = '"+id_unico+"' ",null, null,null,null);
        return mcursor;
    }

    public Cursor validarDatos(Integer identificar, String usuario) throws SQLException {
        Cursor mcursor = null;
        mcursor = this.getReadableDatabase().query("lectura", new String[] {"id_unico","identificador","fecha", "fecha_vencimiento","periodo","numero_factura","sector","codigo_ruta","codigo_interno", "usuario","direccion","estrato","uso","numero_medidor","consumo_mes_6","consumo_mes_5", "consumo_mes_4","consumo_mes_3","consumo_mes_2","consumo_mes_1","promedio","consumo_basico", "consumo_complementario","consumo_suntuario","deuda_anterior","atraso","estado_medidor", "eventualidad","lectura_anterior","lectura_actual","consumo","consumo_facturado", "acueducto_valor_mtr_3","acueducto_cargo_fijo","acueducto_consumo_basico", "acueducto_consumo_complementario","acueducto_consumo_suntuario", "acueducto_subsido_cargo_fijo","acueducto_subsido_basico","acueducto_subsido_complementario", "acueducto_subsido_suntuario","acueducto_contribucion","acueducto_mora","acueducto_concepto1", "acueducto_concepto2","acueducto_concepto3","alcantarillado_valor_mtr_3", "alcantarillado_cargo_fijo","alcantarillado_consumo_basico","alcantarillado_consumo_complementario","alcantarillado_consumo_suntuario","alcantarillado_subsido_cargo_fijo","alcantarillado_subsido_basico","alcantarillado_subsido_complementario", "alcantarillado_subsido_suntuario","alcantarillado_contribucion","alcantarillado_mora","alcantarillado_concepto1","alcantarillado_concepto2","alcantarillado_concepto3","aseo_valor_mtr_3","aseo_cargo_fijo","aseo_consumo_basico","aseo_consumo_complementario","aseo_consumo_suntuario","aseo_subsido_cargo_fijo","aseo_subsido_basico","aseo_subsido_complementario","aseo_subsido_suntuario","aseo_contribucion","aseo_mora","aseo_concepto1", "aseo_concepto2", "aseo_concepto3", "matricula", "medidor", "llaves", "tapas", "financiacion", "reconexion", "fecha_factura", "aforador", "observaciones", "mora", "tipo_cliente", "cft", "trbl", "historico_123", "historico_456", "aseo", "subsidio", "deuda", "ajuste_d", "ajuste_c", "uni_residenciales", "un_comerciales", "porcentaje_subsidio", "porcentaje_contribucion", "frec_barrido", "frec_recoleccion", "total_aseo", "observaciones_financiacion", "ajuste_peso", "contribucion_aseo", "trna_tafna_tra_tafa", "abonos", "porcentaje", "fecha_suspension", "total_acueducto", "total_alcantarillado", "total_acdto_alc"},
                "identificador = '"+identificar+"' and usuario = '"+usuario+"'",null, null,null,null);
        return mcursor;
    }

    public void borrarRegistros(String table, Activity activity){
        SQLiteDatabase db = getWritableDatabase();
           db.execSQL("DELETE FROM " + table);
        Toast.makeText(activity, "Se limpio correctamente", Toast.LENGTH_SHORT).show();
    }

    public void borrarSincronizados(Activity activity){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM lectura where Sincronizo = 1 or lectura_actual is null");
        Toast.makeText(activity, "Se limpio correctamente", Toast.LENGTH_SHORT).show();
    }

    public void borrarSincronizados( String sector, Activity activity){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM lectura where sector = '" + sector + "' and Sincronizo = 1");
        Toast.makeText(activity, "Se limpio correctamente", Toast.LENGTH_SHORT).show();
    }

    public void agregarDatos(Integer id_unico,Integer identificador,String fecha,String fecha_vencimiento,String periodo,String numero_factura,String sector, String codigo_ruta,String codigo_interno,String usuario,String direccion,String estrato, String uso,String numero_medidor,String consumo_mes_6, String consumo_mes_5,String consumo_mes_4, String consumo_mes_3,String consumo_mes_2, String consumo_mes_1,Integer promedio, Integer consumo_basico,Integer consumo_complementario,Integer consumo_suntuario,Double deuda_anterior,Integer atraso,Integer estado_medidor, String eventualidad,Integer lectura_anterior,String lectura_actual,Integer consumo,Integer consumo_facturado, Double acueducto_valor_mtr_3, Long acueducto_cargo_fijo,String acueducto_consumo_basico,String acueducto_consumo_complementario,String acueducto_consumo_suntuario, String acueducto_subsido_cargo_fijo,String acueducto_subsido_basico,String acueducto_subsido_complementario,String acueducto_subsido_suntuario, String acueducto_contribucion, String acueducto_mora,String acueducto_concepto1,String acueducto_concepto2,String acueducto_concepto3, String alcantarillado_valor_mtr_3,Long alcantarillado_cargo_fijo,String alcantarillado_consumo_basico, String alcantarillado_consumo_complementario,String alcantarillado_consumo_suntuario,String alcantarillado_subsido_cargo_fijo, String alcantarillado_subsido_basico,String alcantarillado_subsido_complementario,String alcantarillado_subsido_suntuario, String alcantarillado_contribucion,String alcantarillado_mora,String alcantarillado_concepto1,String alcantarillado_concepto2, String alcantarillado_concepto3,String aseo_valor_mtr_3,String aseo_cargo_fijo, String aseo_consumo_basico,String aseo_consumo_complementario, String aseo_consumo_suntuario,String aseo_subsido_cargo_fijo,String aseo_subsido_basico,String aseo_subsido_complementario, String aseo_subsido_suntuario,String aseo_contribucion,String aseo_mora,String aseo_concepto1,String aseo_concepto2,String aseo_concepto3, Double matricula,Double medidor,Double llaves, Double tapas,String financiacion,Double reconexion,String fecha_factura,String aforador,String observaciones,String mora,String tipo_cliente,String cft,String trbl,String historico_123,String historico_456,String aseo,String subsidio,String deuda,String ajuste_d,String ajuste_c,String uni_residenciales,String un_comerciales,String porcentaje_subsidio,String porcentaje_contribucion,String frec_barrido, String frec_recoleccion,String total_aseo,String observaciones_financiacion,Double ajuste_peso,String contribucion_aseo,String trna_tafna_tra_tafa,Double abonos, Double porcentaje, String fecha_suspension,Double total_acueducto, Double total_alcantarillado,Double total_acdto_alc, Boolean Sincronizo, Integer orden, Double suspension, Double geofono, Double otros_cobros, String observaciones_otros_cobros, String conceptos_otros_cobros){
        ContentValues valores = new ContentValues();
        valores.put("id_unico", id_unico);
        valores.put("identificador", identificador);
        valores.put("fecha", fecha);
        valores.put("fecha_vencimiento", fecha_vencimiento);
        valores.put("periodo", periodo);
        valores.put("numero_factura", numero_factura);
        valores.put("sector", sector);
        valores.put("codigo_ruta", codigo_ruta);
        valores.put("codigo_interno", codigo_interno);
        valores.put("usuario", usuario);
        valores.put("direccion", direccion);
        valores.put("estrato", estrato);
        valores.put("uso", uso);
        valores.put("numero_medidor", numero_medidor);
        valores.put("consumo_mes_6", consumo_mes_6);
        valores.put("consumo_mes_5", consumo_mes_5);
        valores.put("consumo_mes_4", consumo_mes_4);
        valores.put("consumo_mes_3", consumo_mes_3);
        valores.put("consumo_mes_2", consumo_mes_2);
        valores.put("consumo_mes_1", consumo_mes_1);
        valores.put("promedio", promedio);
        valores.put("consumo_basico", consumo_basico);
        valores.put("consumo_complementario", consumo_complementario);
        valores.put("consumo_suntuario", consumo_suntuario);
        valores.put("deuda_anterior", deuda_anterior);
        valores.put("atraso", atraso);
        valores.put("estado_medidor", estado_medidor);
        valores.put("eventualidad", eventualidad);
        valores.put("lectura_anterior", lectura_anterior);
        valores.put("lectura_actual", lectura_actual);
        valores.put("consumo", consumo);
        valores.put("consumo_facturado", consumo_facturado);
        valores.put("acueducto_valor_mtr_3", acueducto_valor_mtr_3);
        valores.put("acueducto_cargo_fijo", acueducto_cargo_fijo);
        valores.put("acueducto_consumo_basico", acueducto_consumo_basico);
        valores.put("acueducto_consumo_complementario", acueducto_consumo_complementario);
        valores.put("acueducto_consumo_suntuario", acueducto_consumo_suntuario);
        valores.put("acueducto_subsido_cargo_fijo", acueducto_subsido_cargo_fijo);
        valores.put("acueducto_subsido_basico", acueducto_subsido_basico);
        valores.put("acueducto_subsido_complementario", acueducto_subsido_complementario);
        valores.put("acueducto_subsido_suntuario", acueducto_subsido_suntuario);
        valores.put("acueducto_contribucion", acueducto_contribucion);
        valores.put("acueducto_mora", acueducto_mora);
        valores.put("acueducto_concepto1", acueducto_concepto1);
        valores.put("acueducto_concepto2", acueducto_concepto2);
        valores.put("acueducto_concepto3", acueducto_concepto3);
        valores.put("alcantarillado_valor_mtr_3", alcantarillado_valor_mtr_3);
        valores.put("alcantarillado_cargo_fijo", alcantarillado_cargo_fijo);
        valores.put("alcantarillado_consumo_basico", alcantarillado_consumo_basico);
        valores.put("alcantarillado_consumo_complementario", alcantarillado_consumo_complementario);
        valores.put("alcantarillado_consumo_suntuario", alcantarillado_consumo_suntuario);
        valores.put("alcantarillado_subsido_cargo_fijo", alcantarillado_subsido_cargo_fijo);
        valores.put("alcantarillado_subsido_basico", alcantarillado_subsido_basico);
        valores.put("alcantarillado_subsido_complementario", alcantarillado_subsido_complementario);
        valores.put("alcantarillado_subsido_suntuario", alcantarillado_subsido_suntuario);
        valores.put("alcantarillado_contribucion", alcantarillado_contribucion);
        valores.put("alcantarillado_mora", alcantarillado_mora);
        valores.put("alcantarillado_concepto1", alcantarillado_concepto1);
        valores.put("alcantarillado_concepto2", alcantarillado_concepto2);
        valores.put("alcantarillado_concepto3", alcantarillado_concepto3);
        valores.put("aseo_valor_mtr_3", aseo_valor_mtr_3);
        valores.put("aseo_cargo_fijo", aseo_cargo_fijo);
        valores.put("aseo_consumo_basico", aseo_consumo_basico);
        valores.put("aseo_consumo_complementario", aseo_consumo_complementario);
        valores.put("aseo_consumo_suntuario", aseo_consumo_suntuario);
        valores.put("aseo_subsido_cargo_fijo", aseo_subsido_cargo_fijo);
        valores.put("aseo_subsido_basico", aseo_subsido_basico);
        valores.put("aseo_subsido_complementario", aseo_subsido_complementario);
        valores.put("aseo_subsido_suntuario", aseo_subsido_suntuario);
        valores.put("aseo_contribucion", aseo_contribucion);
        valores.put("aseo_mora", aseo_mora);
        valores.put("aseo_concepto1", aseo_concepto1);
        valores.put("aseo_concepto2", aseo_concepto2);
        valores.put("aseo_concepto3", aseo_concepto3);
        valores.put("matricula", matricula);
        valores.put("medidor", medidor);
        valores.put("llaves", llaves);
        valores.put("tapas", tapas);
        valores.put("financiacion", financiacion);
        valores.put("reconexion", reconexion);
        valores.put("fecha_factura", fecha_factura);
        valores.put("aforador", aforador);
        valores.put("observaciones",observaciones);
        valores.put("mora",mora);
        valores.put("tipo_cliente",tipo_cliente);
        valores.put("cft",cft);
        valores.put("trbl",trbl);
        valores.put("historico_123",historico_123);
        valores.put("historico_456",historico_456);
        valores.put("aseo",aseo);
        valores.put("subsidio",subsidio);
        valores.put("deuda",deuda);
        valores.put("ajuste_d",ajuste_d);
        valores.put("ajuste_c",ajuste_c);
        valores.put("uni_residenciales",uni_residenciales);
        valores.put("un_comerciales",un_comerciales);
        valores.put("porcentaje_subsidio",porcentaje_subsidio);
        valores.put("porcentaje_contribucion",porcentaje_contribucion);
        valores.put("frec_barrido",frec_barrido);
        valores.put("frec_recoleccion",frec_recoleccion);
        valores.put("total_aseo",total_aseo);
        valores.put("observaciones_financiacion",observaciones_financiacion);
        valores.put("ajuste_peso",ajuste_peso);
        valores.put("contribucion_aseo",contribucion_aseo);
        valores.put("trna_tafna_tra_tafa",trna_tafna_tra_tafa);
        valores.put("abonos",abonos);
        valores.put("porcentaje",porcentaje);
        valores.put("fecha_suspension",fecha_suspension);
        valores.put("total_acueducto",total_acueducto);
        valores.put("total_alcantarillado",total_alcantarillado);
        valores.put("total_acdto_alc",total_acdto_alc);
        valores.put("Sincronizo",Sincronizo);
        valores.put("orden",orden);
        valores.put("suspension",suspension);
        valores.put("geofono",geofono);
        valores.put("otros_cobros",otros_cobros);
        valores.put("observaciones_otros_cobros",observaciones_otros_cobros);
        valores.put("conceptos_otros_cobros",conceptos_otros_cobros);
        this.getWritableDatabase().insertOrThrow("lectura", null, valores);
    }

    public int updateDatos(ContentValues valores,String identificador){
    int can = this.getWritableDatabase().update("lectura",  valores, "identificador="+identificador,null);
    return can;
    }

    public int updateSincro(ContentValues valores,String identificador){
        int can = this.getWritableDatabase().update("lectura",  valores, "identificador="+identificador,null);
        return can;
    }

    public Double formato(String valor){
        double replace = 0;
        String form = valor .replace(",",".");
        replace = Double.parseDouble(form);
        return replace;
    }

    public String formatoSalida(String valor){
        String formato = "";
        DecimalFormat decimalFormat = new DecimalFormat("$#,###.##");
        if (valor.contains(".")){
            formato = decimalFormat.format(this.formato(valor)); //SI valor TRAE COMA
        }else if (!valor.contains(".")){
            double rta = Double.parseDouble(valor);
            formato = decimalFormat.format(rta)+".00"; //SI valor NO TRAE PUNTO
        }else{
            double rta = Double.parseDouble(valor);
            formato = decimalFormat.format(rta); //SI valor VIENE CON FORMATO
        }
        return formato;
    }

    public String formatoSalida2(String valor){
        String formato = "";
        DecimalFormat decimalFormat = new DecimalFormat("$#,###");
        if (valor.contains(".")){
            formato = decimalFormat.format(this.formato(valor)); //SI valor TRAE COMA
        }else if (!valor.contains(".")){
            double rta = Double.parseDouble(valor);
            formato = decimalFormat.format(rta); //SI valor NO TRAE PUNTO
        }else{
            double rta = Double.parseDouble(valor);
            formato = decimalFormat.format(rta); //SI valor VIENE CON FORMATO
        }
        return formato;
    }
}

