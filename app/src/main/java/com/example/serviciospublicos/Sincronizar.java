package com.example.serviciospublicos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.serviciospublicos.Controlador.Maincontroller;
import com.example.serviciospublicos.Controlador.SincronizacionController;
import com.example.serviciospublicos.util.AppUtil;
import com.example.serviciospublicos.util.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.serviciospublicos.Login.urlServidor;
import static com.example.serviciospublicos.Login.usuarioF;

public class Sincronizar extends AppCompatActivity {

    public static String Url = AppUtil.getUrlEncode(urlServidor + "UpdateCalculos.php");

    public static boolean sincronizando = false;
    Button btnLimpiar;
    Maincontroller objuser = new Maincontroller(this, "Servicios_publicos", null, 1);
    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String usuari, contrasen, fechaactualizacion, observaciones;
    Integer id_unico, rol, tercero, estado, a = 0;
    Button btnSincronizar;
    Spinner spinner_sector;
    TextView cargando, txtTotalRutasMensaje, txtTRutas;
    ArrayAdapter<String> SectorAdapter;
    ArrayList<String> listaSector = new ArrayList<>();
    String urlUsuario = AppUtil.getUrlEncode(urlServidor + "CargarUsuario.php");
    String urlCargarSector = AppUtil.getUrlEncode(urlServidor + "SpinnerSector.php");
    String urlEventualidad = AppUtil.getUrlEncode(urlServidor + "CargarEventualidad.php");
    String urlDatos;
    String SelectedSector;
    List<Map> listaRecursos = null;
    private int indiceActual = 0;
    Boolean update = false;
    Button btnSubida;
    String responsee = null;
    int resputa = 0;
    private UIHelper helper = new UIHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        txtTotalRutasMensaje = (TextView) findViewById(R.id.txtTotalRutas);
        txtTRutas = (TextView) findViewById(R.id.txtTRutas);
        cargando = (TextView) findViewById(R.id.txtCargando);
        btnSincronizar = (Button) findViewById(R.id.btnSincro);
        btnSubida = (Button) findViewById(R.id.btnSubida);
        spinner_sector = (Spinner) findViewById(R.id.spSector);
        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);
        cargarSector();

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSincronizar.setEnabled(false);
                helper.showLoadingDialog("Guardando Informacion ...");

                cargarUsuarios();
                cargarEventualidad();
                SelectedSector = spinner_sector.getSelectedItem().toString();
                SincronizarBajada();
            }
        });

        btnSubida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SincronizarSubida();

            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSincronizar.setEnabled(false);
                btnSubida.setEnabled(false);
                btnLimpiar.setEnabled(false);
                limpiarBd();
            }
        });
    }

    public void cargarSector() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlCargarSector + "?aforador=" + usuarioF, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("gp_facturacion_servicios");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sector = jsonObject.optString("sector");
                        listaSector.add(sector);

                    }
                    SectorAdapter = new ArrayAdapter<>(Sincronizar.this, R.layout.custom_spinner, listaSector);
                    SectorAdapter.setDropDownViewResource(R.layout.custom_spinner);
                    spinner_sector.setAdapter(SectorAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helper.dismissLoadingDialog();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    Integer contador = 0;

    public void SincronizarBajada() {
        sincronizando = true;
        SincronizarSubida();
        limpiarSincronizados(SelectedSector);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                urlDatos = urlServidor + "CargarDatos.php?sector=" + SelectedSector + "&aforador=" + usuarioF + "", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("CargarDatos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer id_unico = jsonObject.isNull("id_unico") ? null : jsonObject.optInt("id_unico");
                        Integer identificador = jsonObject.isNull("identificador") ? null : jsonObject.optInt("identificador");
                        String fecha = jsonObject.isNull("fecha") ? null : jsonObject.optString("fecha");
                        String fecha_vencimiento = jsonObject.isNull("fecha_vencimiento") ? null : jsonObject.optString("fecha_vencimiento");
                        String periodo = jsonObject.isNull("periodo") ? null : jsonObject.optString("periodo");
                        String numero_factura = jsonObject.isNull("numero_factura") ? null : jsonObject.optString("numero_factura");
                        String sector = jsonObject.isNull("sector") ? null : jsonObject.optString("sector");
                        String codigo_ruta = jsonObject.isNull("codigo_ruta") ? null : jsonObject.optString("codigo_ruta");
                        String codigo_interno = jsonObject.isNull("codigo_interno") ? null : jsonObject.optString("codigo_interno");
                        String usuario = jsonObject.isNull("usuario") ? null : jsonObject.optString("usuario");
                        String direccion = jsonObject.isNull("direccion") ? null : jsonObject.optString("direccion");
                        String estrato = jsonObject.isNull("estrato") ? null : jsonObject.optString("estrato");
                        String uso = jsonObject.isNull("uso") ? null : jsonObject.optString("uso");
                        String numero_medidor = jsonObject.isNull("numero_medidor") ? null : jsonObject.optString("numero_medidor");
                        String consumo_mes_6 = jsonObject.isNull("consumo_mes_6") ? null : jsonObject.optString("consumo_mes_6");
                        String consumo_mes_5 = jsonObject.isNull("consumo_mes_5") ? null : jsonObject.optString("consumo_mes_5");
                        String consumo_mes_4 = jsonObject.isNull("consumo_mes_4") ? null : jsonObject.optString("consumo_mes_4");
                        String consumo_mes_3 = jsonObject.isNull("consumo_mes_3") ? null : jsonObject.optString("consumo_mes_3");
                        String consumo_mes_2 = jsonObject.isNull("consumo_mes_2") ? null : jsonObject.optString("consumo_mes_2");
                        String consumo_mes_1 = jsonObject.isNull("consumo_mes_1") ? null : jsonObject.optString("consumo_mes_1");
                        Integer promedio = jsonObject.isNull("promedio") ? null : jsonObject.optInt("promedio");
                        Integer consumo_basico = jsonObject.isNull("consumo_basico") ? null : jsonObject.optInt("consumo_basico");
                        Integer consumo_complementario = jsonObject.isNull("consumo_complementario") ? null : jsonObject.optInt("consumo_complementario");
                        Integer consumo_suntuario = jsonObject.isNull("consumo_suntuario") ? null : jsonObject.optInt("consumo_suntuario");
                        Double deuda_anterior = jsonObject.isNull("deuda_anterior") ? null : jsonObject.optDouble("deuda_anterior");


                        Integer atraso = jsonObject.isNull("atraso") ? null : jsonObject.optInt("atraso");
                        Integer estado_medidor = jsonObject.isNull("estado_medidor") ? null : jsonObject.optInt("estado_medidor");
                        String eventualidad = jsonObject.isNull("eventualidad") ? null : jsonObject.optString("eventualidad");
                        Integer lectura_anterior = jsonObject.isNull("lectura_anterior") ? null : jsonObject.optInt("lectura_anterior");
                        String lectura_actual = jsonObject.isNull("lectura_actual") ? null : jsonObject.optString("lectura_actual");
                        Integer consumo = jsonObject.isNull("consumo") ? null : jsonObject.optInt("consumo");
                        Integer consumo_facturado = jsonObject.isNull("consumo_facturado") ? null : jsonObject.optInt("consumo_facturado");
                        Double acueducto_valor_mtr_3 = jsonObject.isNull("acueducto_valor_mtr_3") ? null : jsonObject.optDouble("acueducto_valor_mtr_3");
                        Long acueducto_cargo_fijo = jsonObject.isNull("acueducto_cargo_fijo") ? null : Math.round(jsonObject.optDouble("acueducto_cargo_fijo"));

                        String acueducto_consumo_basico = jsonObject.isNull("acueducto_consumo_basico") ? null : jsonObject.optString("acueducto_consumo_basico");
                        String acueducto_consumo_complementario = jsonObject.isNull("acueducto_consumo_complementario") ? null : jsonObject.optString("acueducto_consumo_complementario");
                        String acueducto_consumo_suntuario = jsonObject.isNull("acueducto_consumo_suntuario") ? null : jsonObject.optString("acueducto_consumo_suntuario");
                        String acueducto_subsidio_cargo_fijo = jsonObject.isNull("acueducto_subsido_cargo_fijo") ? null : jsonObject.optString("acueducto_subsido_cargo_fijo");
                        String acueducto_subsidio_basico = jsonObject.isNull("acueducto_subsido_basico") ? null : jsonObject.optString("acueducto_subsido_basico");
                        String acueducto_subsidio_complementario = jsonObject.isNull("acueducto_subsido_complementario") ? null : jsonObject.optString("acueducto_subsido_complementario");
                        String acueducto_subsidio_suntuario = jsonObject.isNull("acueducto_subsido_suntuario") ? null : jsonObject.optString("acueducto_subsido_suntuario");
                        String acueducto_contribucion = jsonObject.isNull("acueducto_contribucion") ? null : jsonObject.optString("acueducto_contribucion");
                        String acueducto_mora = jsonObject.isNull("acueducto_mora") ? null : jsonObject.optString("acueducto_mora");
                        String acueducto_concepto1 = jsonObject.isNull("acueducto_concepto1") ? null : jsonObject.optString("acueducto_concepto1");
                        String acueducto_concepto2 = jsonObject.isNull("acueducto_concepto2") ? null : jsonObject.optString("acueducto_concepto2");
                        String acueducto_concepto3 = jsonObject.isNull("acueducto_concepto3") ? null : jsonObject.optString("acueducto_concepto3");


                        String alcantarillado_valor_mtr3 = jsonObject.isNull("alcantarillado_valor_mtr_3") ? null : jsonObject.optString("alcantarillado_valor_mtr_3");
                        Long alcantarillado_cargo_fijo = jsonObject.isNull("alcantarillado_cargo_fijo") ? null : Math.round(jsonObject.optDouble("alcantarillado_cargo_fijo"));
                        String alcantarillado_consumo_basico = jsonObject.isNull("alcantarillado_consumo_basico") ? null : jsonObject.optString("alcantarillado_consumo_basico");
                        String alcantarilado__consumo_complementario = jsonObject.isNull("alcantarillado_consumo_complementario") ? null : jsonObject.optString("alcantarillado_consumo_complementario");
                        String alcantarillado_consumo_suntuario = jsonObject.isNull("alcantarillado_consumo_suntuario") ? null : jsonObject.optString("alcantarillado_consumo_suntuario");
                        String alcantarillado_subsidio_cargo_fijo = jsonObject.isNull("alcantarillado_subsido_cargo_fijo") ? null : jsonObject.optString("alcantarillado_subsido_cargo_fijo");
                        String alcantarillado_subsidio_basico = jsonObject.isNull("alcantarillado_subsido_basico") ? null : jsonObject.optString("alcantarillado_subsido_basico");
                        String alcantarillado_subsidio_complementario = jsonObject.isNull("alcantarillado_subsido_complementario") ? null : jsonObject.optString("alcantarillado_subsido_complementario");
                        String alcantarillado_subsidio_suntuario = jsonObject.isNull("alcantarillado_subsido_suntuario") ? null : jsonObject.optString("alcantarillado_subsido_suntuario");
                        String alcantarillado_contribucion = jsonObject.isNull("alcantarillado_contribucion") ? null : jsonObject.optString("alcantarillado_contribucion");
                        String alcantarillado_mora = jsonObject.isNull("alcantarillado_mora") ? null : jsonObject.optString("alcantarillado_mora");
                        String alcantarillado_concepto1 = jsonObject.isNull("alcantarillado_concepto1") ? null : jsonObject.optString("alcantarillado_concepto1");
                        String alcantarillado_concepto2 = jsonObject.isNull("alcantarillado_concepto2") ? null : jsonObject.optString("alcantarillado_concepto2");
                        String alcantarillado_concepto3 = jsonObject.isNull("alcantarillado_concepto3") ? null : jsonObject.optString("alcantarillado_concepto3");

                        String aseo_valor_mtr_3 = jsonObject.isNull("aseo_valor_mtr_3") ? null : jsonObject.optString("aseo_valor_mtr_3");
                        String aseo_cargo_fijo = jsonObject.isNull("aseo_cargo_fijo") ? null : jsonObject.optString("aseo_cargo_fijo");
                        String aseo_consumo_basico = jsonObject.isNull("aseo_consumo_basico") ? null : jsonObject.optString("aseo_consumo_basico");
                        String aseo_consumo_complementario = jsonObject.isNull("aseo_consumo_complementario") ? null : jsonObject.optString("aseo_consumo_complementario");
                        String aseo_consumo_suntuario = jsonObject.isNull("aseo_consumo_sunturio") ? null : jsonObject.optString("aseo_consumo_sunturio");
                        String aseo_subsidio_cargo_fijo = jsonObject.isNull("aseo_subsido_cargo_fijo") ? null : jsonObject.optString("aseo_subsido_cargo_fijo");
                        String aseo_subsidio_basico = jsonObject.isNull("aseo_subsido_basico") ? null : jsonObject.optString("acueducto_consumo_basico");
                        String aseo_subsidio_complementario = jsonObject.isNull("aseo_subsido_complementario") ? null : jsonObject.optString("aseo_subsido_basico");
                        String aseo_subsidio_suntuario = jsonObject.isNull("aseo_subsido_suntuario") ? null : jsonObject.optString("aseo_subsido_suntuario");
                        String aseo_contribucion = jsonObject.isNull("aseo_contribucion") ? null : jsonObject.optString("aseo_contribucion");
                        String aseo_mora = jsonObject.isNull("aseo_mora") ? null : jsonObject.optString("aseo_mora");
                        String aseo_concepto1 = jsonObject.isNull("aseo_concepto1") ? null : jsonObject.optString("aseo_concepto1");
                        String aseo_concepto2 = jsonObject.isNull("aseo_concepto2") ? null : jsonObject.optString("aseo_concepto2");
                        String aseo_concepto3 = jsonObject.isNull("aseo_concepto3") ? null : jsonObject.optString("aseo_concepto3");

                        Double matricula = jsonObject.isNull("matricula") ? null : jsonObject.optDouble("matricula");
                        Double medidor = jsonObject.isNull("medidor") ? null : jsonObject.optDouble("medidor");
                        Double llaves = jsonObject.isNull("llaves") ? null : jsonObject.optDouble("llaves");
                        Double tapas = jsonObject.isNull("tapas") ? null : jsonObject.optDouble("tapas");
                        String financiacion = jsonObject.isNull("financiacion") ? null : jsonObject.optString("financiacion");
                        Double reconexion = jsonObject.isNull("reconexion") ? null : jsonObject.optDouble("reconexion");
                        String fecha_factura = jsonObject.isNull("fecha_factura") ? null : jsonObject.optString("fecha_factura");
                        String aforador = jsonObject.isNull("aforador") ? null : jsonObject.optString("aforador");
                        String observaciones = jsonObject.isNull("observaciones") ? null : jsonObject.optString("observaciones");
                        String mora = jsonObject.isNull("mora") ? null : jsonObject.optString("mora");
                        String tipo_cliente = jsonObject.isNull("tipo_cliente") ? null : jsonObject.optString("tipo_cliente");
                        String cft = jsonObject.isNull("cft") ? null : jsonObject.optString("cft");
                        String trbl = jsonObject.isNull("trbl") ? null : jsonObject.optString("trbl");
                        String historico_123 = jsonObject.isNull("historico_123") ? null : jsonObject.optString("historico_123");
                        String historico_456 = jsonObject.isNull("historico_456") ? null : jsonObject.optString("historico_456");
                        String aseo = jsonObject.isNull("aseo") ? null : jsonObject.optString("aseo");
                        String subsidio = jsonObject.isNull("subsidio") ? null : jsonObject.optString("subsidio");
                        String deuda = jsonObject.isNull("deuda") ? null : jsonObject.optString("deuda");
                        String ajuste_d = jsonObject.isNull("ajuste_d") ? null : jsonObject.optString("ajuste_d");

                        String ajuste_c = jsonObject.isNull("ajuste_c") ? null : jsonObject.optString("ajuste_c");
                        String uni_residenciales = jsonObject.isNull("uni_residenciales") ? null : jsonObject.optString("uni_residenciales");
                        String un_comerciales = jsonObject.isNull("un_comerciales") ? null : jsonObject.optString("un_comerciales");
                        String porcentaje_subsidio = jsonObject.isNull("porcentaje_subsidio") ? null : jsonObject.optString("porcentaje_subsidio");
                        String porcentaje_contribucion = jsonObject.isNull("porcentaje_contribucion") ? null : jsonObject.optString("porcentaje_contribucion");
                        String frec_barrido = jsonObject.isNull("frec_barrido") ? null : jsonObject.optString("frec_barrido");
                        String frec_recoleccion = jsonObject.isNull("frec_recoleccion") ? null : jsonObject.optString("frec_recoleccion");
                        String total_aseo = jsonObject.isNull("total_aseo") ? null : jsonObject.optString("total_aseo");
                        String observaciones_financiacion = jsonObject.isNull("observaciones_financiacion") ? null : jsonObject.optString("observaciones_financiacion");
                        Double ajuste_peso = jsonObject.isNull("ajuste_peso") ? null : jsonObject.optDouble("ajuste_peso");
                        String contribucion_aseo = jsonObject.isNull("contribucion_aseo") ? null : jsonObject.optString("contribucion_aseo");
                        String trna_tafna_tra_tafa = jsonObject.isNull("trna_tafna_tra_tafa") ? null : jsonObject.optString("trna_tafna_tra_tafa");
                        Double abonos = jsonObject.isNull("abonos") ? null : jsonObject.optDouble("abonos");
                        Double porcentaje = jsonObject.isNull("porcentaje") ? null : jsonObject.optDouble("porcentaje");
                        String fecha_suspension = jsonObject.isNull("fecha_suspension") ? null : jsonObject.optString("fecha_suspension");
                        Double total_acueducto = jsonObject.isNull("total_acueducto") ? null : jsonObject.optDouble("total_acueducto");
                        Double total_alcantarillado = jsonObject.isNull("total_alcantarillado") ? null : jsonObject.optDouble("total_alcantarillado");
                        Double total_acdto_alc = jsonObject.isNull("total_acdto_alc") ? null : jsonObject.optDouble("total_acdto_alc");
                        Integer orden = jsonObject.isNull("orden") ? null : jsonObject.optInt("orden");
                        Double suspension = jsonObject.isNull("suspension") ? null : jsonObject.optDouble("suspension");
                        Double geofono = jsonObject.isNull("geofono") ? null : jsonObject.optDouble("geofono");
                        Double otros_cobros = jsonObject.isNull("otros_cobros") ? null : jsonObject.optDouble("otros_cobros");
                        String observaciones_otros_cobros = jsonObject.isNull("observaciones_otros_cobros") ? null : jsonObject.optString("observaciones_otros_cobros");
                        String conceptos_otros_cobros = jsonObject.isNull("conceptos_otros_cobros") ? null : jsonObject.optString("conceptos_otros_cobros");
                        Boolean Sincronizo = false;

                        if (lectura_actual != null && !lectura_actual.isEmpty()) {
                            Sincronizo = true;
                        }

                        Cursor cursorr = objuser.validarDatos(identificador, usuario);
                        if (cursorr.getCount() == 0) {
                            objuser.agregarDatos(id_unico, identificador, fecha, fecha_vencimiento, periodo, numero_factura, sector, codigo_ruta, codigo_interno, usuario, direccion, estrato, uso, numero_medidor, consumo_mes_6, consumo_mes_5, consumo_mes_4, consumo_mes_3, consumo_mes_2, consumo_mes_1, promedio, consumo_basico, consumo_complementario, consumo_suntuario, deuda_anterior, atraso, estado_medidor, eventualidad, lectura_anterior, lectura_actual, consumo, consumo_facturado, acueducto_valor_mtr_3, acueducto_cargo_fijo, acueducto_consumo_basico, acueducto_consumo_complementario, acueducto_consumo_suntuario, acueducto_subsidio_cargo_fijo, acueducto_subsidio_basico, acueducto_subsidio_complementario, acueducto_subsidio_suntuario, acueducto_contribucion, acueducto_mora, acueducto_concepto1, acueducto_concepto2, acueducto_concepto3, alcantarillado_valor_mtr3, alcantarillado_cargo_fijo, alcantarillado_consumo_basico, alcantarilado__consumo_complementario, alcantarillado_consumo_suntuario, alcantarillado_subsidio_cargo_fijo, alcantarillado_subsidio_basico, alcantarillado_subsidio_complementario, alcantarillado_subsidio_suntuario, alcantarillado_contribucion, alcantarillado_mora, alcantarillado_concepto1, alcantarillado_concepto2, alcantarillado_concepto3, aseo_valor_mtr_3, aseo_cargo_fijo, aseo_consumo_basico, aseo_consumo_complementario, aseo_consumo_suntuario, aseo_subsidio_cargo_fijo, aseo_subsidio_basico, aseo_subsidio_complementario, aseo_subsidio_suntuario, aseo_contribucion, aseo_mora, aseo_concepto1, aseo_concepto2, aseo_concepto3, matricula, medidor, llaves, tapas, financiacion, reconexion, fecha_factura, aforador, observaciones, mora, tipo_cliente, cft, trbl, historico_123, historico_456, aseo, subsidio, deuda, ajuste_d, ajuste_c, uni_residenciales, un_comerciales, porcentaje_subsidio, porcentaje_contribucion, frec_barrido, frec_recoleccion, total_aseo, observaciones_financiacion, ajuste_peso, contribucion_aseo, trna_tafna_tra_tafa, abonos, porcentaje, fecha_suspension, total_acueducto, total_alcantarillado, total_acdto_alc, Sincronizo, orden, suspension, geofono, otros_cobros, observaciones_otros_cobros, conceptos_otros_cobros);
                            contador = contador + 1;
                        }
                    }
                        cargando.setVisibility(View.INVISIBLE);
                        // Toast.makeText(Sincronizar.this, "se sincronizo correctamente" , Toast.LENGTH_SHORT).show();
                        txtTotalRutasMensaje.setVisibility(View.VISIBLE);
                        txtTRutas.setText(String.valueOf(contador));
                        txtTRutas.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();

                }finally {
                    helper.dismissLoadingDialog();
                    btnSincronizar.setEnabled(true);
                    sincronizando = false;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helper.dismissLoadingDialog();
                sincronizando = false;
                btnSincronizar.setEnabled(true);
            }
        });
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }

    public void cargarUsuarios() {

        Toast.makeText(Sincronizar.this, "Cargando datos por favor espere", Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                urlUsuario, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Datos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        id_unico = jsonObject.optInt("id_unico");
                        usuari = jsonObject.optString("usuario");
                        contrasen = jsonObject.optString("contrasen");
                        fechaactualizacion = jsonObject.optString("fechaactualizacion");
                        observaciones = jsonObject.optString("observaciones");
                        rol = jsonObject.optInt("rol");
                        tercero = jsonObject.optInt("tercero");
                        estado = jsonObject.optInt("estado");

                        Cursor cursor = objuser.validarLogin(usuari, contrasen);
                        if (cursor.getCount() > 0) {
                            a = 1;
                        } else {
                            objuser.agregar(id_unico, usuari, contrasen, fechaactualizacion, observaciones, rol, tercero, estado);
                            a = 2;
                        }
                    }
                    if (a == 1) {

                        // Toast.makeText(Sincronizar.this, "Ya esta sincronizado", Toast.LENGTH_SHORT).show();

                    } else if (a == 2) {
                        // Toast.makeText(Sincronizar.this, "se sincronizo correctamente", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helper.dismissLoadingDialog();
                Toast.makeText(Sincronizar.this, "No se pudo cargar los datos por favor revise su conexion a internet", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void cargarEventualidad() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlEventualidad, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Eventualidad");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer idUnico = jsonObject.optInt("id_unico");
                        String nombre = AppUtil.cleanString(jsonObject.optString("nombre"));
                        Integer idTipo = jsonObject.optInt("tipo_eventualidad");
                        Integer valor = jsonObject.optInt("valor");
                        Cursor cursor = objuser.validarEventualidad(idUnico);
                        if (cursor.getCount() > 0) {
                            a = 1;
                        } else {
                            objuser.agregarEventualidad(idUnico, nombre, idTipo, valor);
                            a = 2;
                        }
                    }
                    if (a == 1) {
                        // Toast.makeText(Sincronizar.this, "Ya esta sincronizado", Toast.LENGTH_SHORT).show();

                    } else if (a == 2) {

                        // Toast.makeText(Sincronizar.this, "se sincronizo correctamente", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helper.dismissLoadingDialog();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void SincronizarSubida() {
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
            //subirDatos(usuarioActual);
        }
        if (!listaRecursos.isEmpty()) {
            subirDatos();
        }
    }


    public void subirDatos() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {

                if (response.endsWith("ok_OK")) {
                    update = true;
                    responsee = null;
                    resputa = 1;
                    respuesta();
                    ContentValues parametroos = new ContentValues();
                    parametroos.put("Sincronizo", 1);
                    String identificador = null;
                    identificador = listaRecursos.get(indiceActual).get("identificador").toString();
                    objuser.updateSincro(parametroos, identificador);
                } else {
                    update = false;
                    responsee = response;
                    resputa = 1;
                    respuesta();
                }
                if ( indiceActual < listaRecursos.size()){
                    indiceActual ++;
                    subirDatos();
                }
            }
        }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Sincronizar.this, error.toString(), Toast.LENGTH_SHORT).show();
                ;
                helper.dismissLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (indiceActual<=listaRecursos.size()){
                    return listaRecursos.get(indiceActual);
                }
                return null;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void respuesta() {
        if (resputa == 1) {
            if (update) {
                Toast.makeText(Sincronizar.this, "Sincronizo exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sincronizar.this, "No se sincronizo el sistema; " + responsee, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void limpiarSincronizados(String sector) {
        objuser.borrarSincronizados(sector, this);
    }

    public void limpiarBd() {
        objuser.borrarSincronizados(this);
        objuser.borrarRegistros("eventualidad", this);
        objuser.borrarRegistros("usuario", this);
        objuser.borrarRegistros("compania", this);

        btnSincronizar.setEnabled(true);
        btnSubida.setEnabled(true);
        btnLimpiar.setEnabled(true);
    }

    public void onBackPressed() {

        startActivity(new Intent(Sincronizar.this, MainActivity.class));
        finish();
    }

}