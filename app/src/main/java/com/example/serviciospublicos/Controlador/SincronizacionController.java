package com.example.serviciospublicos.Controlador;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.serviciospublicos.Sincronizar;
import com.example.serviciospublicos.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.serviciospublicos.Login.urlServidor;

public class SincronizacionController {
    Maincontroller objuser;
    Context context;
    List<Map> listaRecursos = null;
    RequestQueue requestQueue;
    private int indiceActual = 0;
    public final static String [] vec  = {"acueducto_consumo_basico","acueducto_consumo_complementario","acueducto_consumo_suntuario","acueducto_subsido_cargo_fijo","acueducto_subsido_basico","acueducto_subsido_complementario","acueducto_subsido_suntuario","acueducto_contribucion","acueducto_mora","acueducto_concepto1","acueducto_concepto2","acueducto_concepto3","alcantarillado_consumo_basico","alcantarillado_consumo_complementario","alcantarillado_consumo_suntuario","alcantarillado_subsido_cargo_fijo","alcantarillado_subsido_basico","alcantarillado_subsido_complementario","alcantarillado_subsido_suntuario","alcantarillado_contribucion","alcantarillado_mora","alcantarillado_concepto1","alcantarillado_concepto2","alcantarillado_concepto3","aseo_valor_mtr_3","aseo_cargo_fijo","aseo_consumo_basico","aseo_consumo_complementario","aseo_consumo_suntuario","aseo_subsido_cargo_fijo","aseo_subsido_basico","aseo_subsido_complementario","aseo_subsido_suntuario","aseo_contribucion","aseo_mora","aseo_concepto1","aseo_concepto2","aseo_concepto3","lectura_actual","consumo","consumo_facturado","eventualidad","observaciones_financiacion","observaciones","ajuste_peso","fecha_factura","total_acueducto","total_alcantarillado","total_acdto_alc","observaciones","observaciones_financiacion","identificador"};
    String Url = Sincronizar.Url;

    public SincronizacionController(Context context) {
        this.objuser = new Maincontroller(context, "Servicios_publicos", null, 1);
        this.context =context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void sincronizarSubida() {
        if (!Sincronizar.sincronizando) {
            boolean x = false;
            listaRecursos = new ArrayList<>();
            indiceActual = 0;
            String campos = Arrays.toString(SincronizacionController.vec);
            campos = campos.substring(1, campos.length() - 1);
            SQLiteDatabase db = objuser.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + campos + " FROM lectura WHERE lectura_actual is not null and Sincronizo = ?", new String[]{String.valueOf(0)});
            while (cursor.moveToNext()) {
                Map usuarioActual = new HashMap();
                for (String col : cursor.getColumnNames()) {
                    usuarioActual.put(col, cursor.getString(cursor.getColumnIndex(col)));

                    if (cursor.getString(cursor.getColumnIndex(col)) == null) {
                        usuarioActual.put(col, "");
                    }
                }
                listaRecursos.add(usuarioActual);
            }
            if (!listaRecursos.isEmpty() && listaRecursos.size() !=0) {
                subirDatos();
            }
        }
    }

    public void subirDatos(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                if (response.endsWith("ok_OK")) {
                    ContentValues parametroos = new ContentValues();
                    parametroos.put("Sincronizo", 1);
                    String identificador= null;
                    identificador = listaRecursos.get(indiceActual).get("identificador").toString();
                    objuser.updateSincro(parametroos,identificador);

                    if (indiceActual < listaRecursos.size()){
                        indiceActual ++;
                        subirDatos();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return listaRecursos.get(indiceActual);
            }
        };
        requestQueue.add(stringRequest);
    }
}
