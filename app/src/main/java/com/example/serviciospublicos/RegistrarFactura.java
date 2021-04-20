package com.example.serviciospublicos;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.serviciospublicos.Adapter.CustomArrayAdapter;
import com.example.serviciospublicos.Adapter.EventualidadAdapter;
import com.example.serviciospublicos.Controlador.Maincontroller;
import com.example.serviciospublicos.Controlador.PermisoApp;
import com.example.serviciospublicos.modelo.ClEventualidad;
import com.example.serviciospublicos.modelo.ClFactura;
import com.example.serviciospublicos.modelo.ItemLista;
import com.example.serviciospublicos.modelo.Parse;
import com.example.serviciospublicos.print.PrintUtil;
import com.example.serviciospublicos.util.SettingsHelper;
import com.example.serviciospublicos.util.UIHelper;
import com.udojava.evalex.Expression;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.serviciospublicos.Login.urlServidor;
import static com.example.serviciospublicos.Login.usuarioF;
import static com.example.serviciospublicos.modelo.ClFactura.observaciones;
import static java.lang.Double.NaN;

public class RegistrarFactura extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static String URL_CARGA = urlServidor + "DatosLectura.php";

    boolean reemplazo;
    String event, eventualidades, eventualidades2;
    private String identificador;
    private String orden;
    String total_acueducto = "0", total_acdto_alc = "0", total_alcantarillado = "0";
    private String ruta;
    private int indiceActual;
    private List<ItemLista> listaReferencia;

    ListView listView;

    private final static String[] vec = {"acueducto_consumo_basico", "acueducto_consumo_complementario", "acueducto_consumo_suntuario", "acueducto_subsido_cargo_fijo", "acueducto_subsido_basico", "acueducto_subsido_complementario", "acueducto_subsido_suntuario", "acueducto_contribucion", "acueducto_mora", "acueducto_concepto1", "acueducto_concepto2", "acueducto_concepto3", "alcantarillado_consumo_basico", "alcantarillado_consumo_complementario", "alcantarillado_consumo_suntuario", "alcantarillado_subsido_cargo_fijo", "alcantarillado_subsido_basico", "alcantarillado_subsido_complementario", "alcantarillado_subsido_suntuario", "alcantarillado_contribucion", "alcantarillado_mora", "alcantarillado_concepto1", "alcantarillado_concepto2", "alcantarillado_concepto3", "aseo_valor_mtr_3", "aseo_cargo_fijo", "aseo_consumo_basico", "aseo_consumo_complementario", "aseo_consumo_suntuario", "aseo_subsido_cargo_fijo", "aseo_subsido_basico", "aseo_subsido_complementario", "aseo_subsido_suntuario", "aseo_contribucion", "aseo_mora", "aseo_concepto1", "aseo_concepto2", "aseo_concepto3"};
    Maincontroller admin;

    RequestQueue requestQueue;
    ArrayList<ClEventualidad> listaEventualidad = new ArrayList<>();
    public static Integer l = 0;
    TextView txtcodRuta;
    TextView txtdireccion;
    TextView txtnombre;
    TextView txtmedidor;
    TextView txtlectura;

    boolean lecturaMenor = false, lecturaMayor = false;
    EditText txtCantidad, Numero;

    Button btnAtras;
    Button btnNext;
    Button btnlectura, btnPdf;
    String s;
    CheckBox checkDaniado;
    CheckBox checkCasaVacia;
    boolean pdf;
    double TOTAL, TOTAL2, aux;
    int index = 0;
    ArrayList<RegistrarFactura> obj;
    Bitmap bmp, scaledbmp; //PARA AGREGAR IMAGENES
    EventualidadAdapter adapter;
    final List<ClEventualidad> users = new ArrayList<>();

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formattedDate = df.format(new Date());

    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDatee = dff.format(new Date());


    int preSelectedIndex = -1;
    public String Par;
    String observaciones_financiacion = null, Observaciones = "";
    String codigoReferencia, codigoRuta, usuario, direccion, fecha, fechaVencimiento, periodo, lecturaActual, estrato, uso, fechaFactura, alcantarilladoValorMtr3, aseoSubsidioBasico, aseoCargoFijo, alcantarilladoCargoFijo, aseoMora, numeroMedidor, numeroFactura, acueductoCargoFijo, acueductoValorMtr3, acueductoConsumoBasico;
    Double consumoFacturado;
    Integer consumoMes6, consumoMes5, consumoMes4, consumoMes3, consumoMes2, consumoMes1, lecturaAnterior, consumoBasico, promedio, consumo, eventualidad, ConsumoComplementario;

    private Timer timer;
    double deudaAnterior;
    int tipoEventualidad = 0;
    String acueductoConsumoComplementario, acueductoConsumoSuntuario, acueductoSubsidoCargoFijo, acueductoSubsidoBasico, acueductoSubsidoComplementario, acueductoSubsidoSuntuario,
            acueductoContribucion, acueductoMora, acueductoConcepto1, acueductoConcepto2, acueductoConcepto3, alcantarilladoConsumoBasico, alcantarilladoConsumoComplementario,
            alcantarilladoConsumoSuntuario, alcantarilladoSubsidoCargoFijo, alcantarilladoSubsidoBasico, alcantarilladoSubsidoComplementario, alcantarilladoSubsidoSuntuario,
            alcantarilladoContribucion, alcantarilladoMora, alcantarilladoConcepto1, alcantarilladoConcepto2, alcantarilladoConcepto3, aseoValorMtr3, aseoSubsidoCargoFijo, aseoConsumoBasico, aseoConsumoComplementario,
            aseoSubsidoComplementario, aseoConsumoSuntuario, aseoSubsidoBasico, aseoContribucion, aseoSubsidoSuntuario, aseoConcepto1, aseoConcepto2, aseoConcepto3, aseo, subsidio, mora, tipo, deuda,
            cft, trbl, historicos123, historicos456, atraso, uniResidenciales, unComerciales, porcentajeSubsidio, porcentajeContribucion, frecBarrido, frecRecoleccion, ajusteD, ajusteC, tra;

    public static String sector;
    private JSONObject usuarioActual;

    private String macAddress;
    ClEventualidad eventualidadActual;
    private UIHelper helper = new UIHelper(this);
    private String claseOrigen;

    private AutoCompleteTextView autoCompleteTextView;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_factura);
        PermisoApp.verifyStoragePermissions(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getIntent();
        claseOrigen = intent.getStringExtra("activity");
        txtlectura = findViewById(R.id.txt_lectura);
        if (claseOrigen.equals(FacturasImpresas.class.toString()) && MainActivity.factur) {
            listaReferencia = FacturasImpresas.listaRuta;
        } else {
            listaReferencia = MainActivity.listaRuta;
            txtlectura.setEnabled(true);
            txtlectura.setText("");
        }
        Numero = (EditText) findViewById(R.id.edtNumero);

        final String item = intent.getStringExtra("item");
        getDataPrincipal(item, -1);


        SharedPreferences settings = getApplicationContext().getSharedPreferences(ConnectionScreen.PREFS_NAME, 0);
        macAddress = settings.getString(ConnectionScreen.bluetoothAddressKey, "");

        requestQueue = Volley.newRequestQueue(this);

        admin = new Maincontroller(RegistrarFactura.this, "Servicios_publicos", null, 1);

        // textoTitulo = findViewById(R.id.textViewTitulo);
        txtcodRuta = findViewById(R.id.txt_codigo_ruta);
        txtdireccion = findViewById(R.id.txt_direccion);
        txtnombre = findViewById(R.id.txt_nombre);
        txtmedidor = findViewById(R.id.txt_medidor);

        btnAtras = findViewById(R.id.button_back);
        btnNext = findViewById(R.id.button_next);
        btnlectura = findViewById(R.id.btn_guardar_lectura);

        listView = findViewById(R.id.listview);
        Handler handler = new Handler();
        btnPdf = findViewById(R.id.btnPdf);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_paz_de_rio);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 55, 55, false); //TAMAÑO DEL LOGO PAZ DE RÍO
        listaEventualidad = new ArrayList<ClEventualidad>();

        //Numero.setText(MainActivity.N);

        asignarEventualidadDesdeDB();

        cargarLectura();

        btnAtras.setOnClickListener(crearEventoBotonesDesplazamiento("(select max(identificador) from lectura where identificador < ?)", 2));
        btnNext.setOnClickListener(crearEventoBotonesDesplazamiento("(select min(identificador) from lectura where identificador > ?)", 1));



        Numero.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                reemplazo = true;
                return false;
            }
        });

        Numero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                orden = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.toString() != null && !s.toString().isEmpty()) {
                    if (!orden.equals(s.toString())) {
                        System.out.println("Antes: " + orden + " Ahora:" + s);
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int ordenI = Integer.parseInt(s.toString());
                                        if (reemplazo) {
                                            buscarItemPorOrden(ordenI);
                                        }
                                        asignarDataDesdeDB();
                                    }
                                });
                            }
                        }, 900); // 600ms delay before the timer executes the „run“ method from TimerTask
                    }
                }
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdf = true;
                procesos();
            }
        });


//recorrer lista de eventualidades y verificar cuales estan en en true de manera que estas se concatenten en un string separadas por coma
        btnlectura.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (!txtlectura.getText().toString().equals("")) {
                    guardarEventualidades();

                    consumoFacturado = 0.0;
                    lecturaActual = txtlectura.getText().toString();

                    int lectu = Integer.parseInt(lecturaActual);
                    if (lecturaActual != null && !lecturaActual.isEmpty()) {
                        if (lectu <= lecturaAnterior) {
                            consumoFacturado = Double.valueOf(promedio);
                            switch (tipoEventualidad) {
                                case 1:
                                    consumoFacturado = Double.valueOf(0);
                                    break;
                                case 2:
                                    if (eventualidades.equals("4")) {
                                        txtlectura.setText(lecturaAnterior.toString());
                                    }
                                    if (eventualidades.equals("3")) {
                                        txtlectura.setText(lecturaAnterior.toString());
                                    }
                                    consumoFacturado = Double.valueOf(promedio);
                                    break;
                                case 3:
                                    if (aseoCargoFijo.equals("")) {
                                        aseoCargoFijo = "0";
                                    }
                                    consumoFacturado = Double.valueOf(0);
                                    break;
                                case 4:
                                    consumoFacturado = Double.valueOf(eventualidadActual.getValor());
                                    break;
                                default:
                                    break;
                                case 6:
                                    if (promedio != 0) {
                                        consumoFacturado = Double.valueOf(promedio);
                                    } else {
                                        consumoFacturado = Double.valueOf(eventualidadActual.getValor());
                                    }
                            }
                            String porcentaje = null;
                            try {
                                porcentaje = String.valueOf(usuarioActual.get("porcentaje"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!porcentaje.equals("") && Double.parseDouble(porcentaje) != 100){
                                consumoFacturado = consumoFacturado * Double.parseDouble(porcentaje)/100;
                                consumoFacturado = Double.valueOf(Math.round(consumoFacturado));
                            }

                            ClFactura.setConsumo_facturado(Double.parseDouble(String.valueOf(consumoFacturado)));
                            try {
                                usuarioActual.put("lectura_actual", txtlectura.getText().toString());
                                usuarioActual.put("consumo", consumo.toString());
                                usuarioActual.put("consumo_facturado", consumoFacturado);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            consumo = lectu - lecturaAnterior;
                            consumoFacturado = Double.valueOf(consumo);
                            String porcentaje = null;
                            try {
                                porcentaje = String.valueOf(usuarioActual.get("porcentaje"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!porcentaje.equals("") && Double.parseDouble(porcentaje) != 100){
                                consumoFacturado = consumoFacturado * Double.parseDouble(porcentaje)/100;
                                consumoFacturado = Double.valueOf(Math.round(consumoFacturado));
                                }

                            ClFactura.setConsumo_facturado(Double.valueOf(consumoFacturado));
                            try {
                                usuarioActual.put("lectura_actual", txtlectura.getText());
                                usuarioActual.put("consumo", consumo.toString());
                                usuarioActual.put("consumo_facturado", consumoFacturado);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    double consumo_mayor = 0;
                    if (promedio > 40) {
                        consumo_mayor = promedio;
                        consumo_mayor = 0.35 * consumo_mayor;
                        consumo_mayor = consumo_mayor + promedio;
                    } else if (promedio < 40) {
                        consumo_mayor = promedio;
                        consumo_mayor = 0.65 * consumo_mayor;
                        consumo_mayor = consumo_mayor + promedio;
                    }

                    lecturaActual = txtlectura.getText().toString();
                    if (Integer.parseInt(lecturaActual) == promedio) {
                        procesos();
                    } else if (Integer.parseInt(lecturaActual) < lecturaAnterior) {
                        new AlertDialog.Builder(RegistrarFactura.this)
                                .setTitle("¿Volver?")
                                .setMessage("¿La lectura es menor a la anterior desea continuar?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        txtlectura.setEnabled(false);
                                        procesos();
                                    }
                                }).create().show();
                    } else if (consumoFacturado > consumo_mayor) {
                        new AlertDialog.Builder(RegistrarFactura.this, R.style.AlertDialogCustom)
                                .setTitle("¿Volver?")
                                .setMessage("El consumo es de " + consumoFacturado + " m³ ¿Desea continuar?")
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        txtlectura.setText("");
                                    }
                                })
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        txtlectura.setEnabled(false);
                                        procesos();

                                    }
                                }).create().show();
                    } else if(Integer.parseInt(lecturaActual) == lecturaAnterior){
                        new AlertDialog.Builder(RegistrarFactura.this, R.style.AlertDialogCustom)
                                .setTitle("¿Volver?")
                                .setMessage("La lectura actual es igual a la lectura anterior")
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        txtlectura.setText("");
                                    }
                                })
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        txtlectura.setEnabled(false);
                                        procesos();

                                    }
                                }).create().show();
                    }else{
                        procesos();
                    }
                } else {
                    Toast.makeText(RegistrarFactura.this, "Por favor ingresar la lectura", Toast.LENGTH_SHORT).show();
                }
            }
        });

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.txtBusqueda);
        CustomArrayAdapter<ItemLista> adapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            adapter = new CustomArrayAdapter<>(this, R.layout.dropdown_multiline_item, MainActivity.listaRuta);
            adapter.setmFieldId(R.id.text1);
        }

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDataPrincipal(parent.getItemAtPosition(position).toString(), -1);
                cargarLectura();
                autoCompleteTextView.setText("");
                txtlectura.requestFocus();
            }
        });


    }


    public void buscarItemPorOrden(int orden) {

        for (int i = 0; i < listaReferencia.size(); i++) {
            ItemLista item = listaReferencia.get(i);
            if (item.orden >= orden) {
                getDataPrincipal(item.texto, i);

                return;
            }
        }

        getDataPrincipal(listaReferencia.get(listaReferencia.size() - 1).texto, listaReferencia.size() - 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void procesos() {
        //helper.showLoadingDialog("Guardando lectura ...");
        String l = txtlectura.getText().toString();
        despejarFormulas();
        Map<String, Object> usuarioActualCopia = getCopyUser();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            extraerDatosJson(usuarioActualCopia);


            if (ClFactura.getLectura_actual() == null || ClFactura.getLectura_actual().isEmpty() || ClFactura.getLectura_actual().equals("")) {
                updateBd(usuarioActualCopia);
            } else {
                printFacturaCalculada(usuarioActualCopia);
                mostrarSiguienteRegistro();
               // helper.dismissLoadingDialog();

            }
        }
        //
    }


    private void guardarEventualidades() {
        event = null;
        eventualidades = null;
        eventualidades2 = null;
        eventualidadActual = null;
        for (int i = 0; i < listaEventualidad.size(); i++) {
            if (listaEventualidad.get(i).getIsSelected()) {
                eventualidadActual = listaEventualidad.get(i);
                eventualidades = eventualidadActual.getIdUnico().toString();
                eventualidades2 = eventualidades2 + "," + eventualidades;
                tipoEventualidad = listaEventualidad.get(i).getIdTipo();
            }
        }
    }


    public void asignarDatos(JSONObject usuarioActual) {
        ClFactura lecturaEntity = Parse.jsonToclFactura(usuarioActual);
        if (lecturaEntity.getEventualidad() != null) {
            if (!lecturaEntity.getEventualidad().equals("")) {
                String[] eventualidadesUsuario = lecturaEntity.getEventualidad().split(",");

                for (String eventualidad : eventualidadesUsuario) {
                    for (ClEventualidad even : listaEventualidad) {
                        if (even.getIdUnico() == Integer.parseInt(eventualidad)) {
                            even.setIsSelected(true);
                            break;
                        }
                    }

                }
                if (adapter != null) {
                    adapter.updateRecords(listaEventualidad);
                }
            }
        }
        //textoTitulo.setText(lecturaEntity.getSector());
        if (lecturaEntity.getCodigo_ruta().equals("")) {
            txtcodRuta.setText("");
        } else {
            txtcodRuta.setText(lecturaEntity.getCodigo_ruta());
        }

        if (lecturaEntity.getDireccion().equals("")) {
            txtdireccion.setText("");
        } else {
            txtdireccion.setText(lecturaEntity.getDireccion());
        }

        if (lecturaEntity.getUsuario().equals("")) {
            txtnombre.setText("");
        } else {
            txtnombre.setText(lecturaEntity.getUsuario());
        }

        if (lecturaEntity.getNumero_medidor().equals("")) {
            txtmedidor.setText("");
        } else {
            txtmedidor.setText(lecturaEntity.getNumero_medidor());
        }
        identificador = lecturaEntity.getIdentificador().toString();
        ruta = lecturaEntity.getCodigo_ruta();


        lecturaActual = lecturaEntity.getLectura_actual();


        codigoReferencia = lecturaEntity.getCodigo_interno();
        codigoRuta = lecturaEntity.getCodigo_ruta();
        usuario = lecturaEntity.getUsuario();
        direccion = lecturaEntity.getDireccion();
        fecha = lecturaEntity.getFecha();
        fechaVencimiento = lecturaEntity.getFecha_vencimiento();
        periodo = lecturaEntity.getPeriodo();
        estrato = lecturaEntity.getEstrato();
        uso = lecturaEntity.getUso();
        fechaFactura = lecturaEntity.getFecha_factura();
        numeroMedidor = lecturaEntity.getNumero_medidor();
        numeroFactura = lecturaEntity.getNumero_factura();

        consumoMes6 = lecturaEntity.getConsumo_mes_6();
        consumoMes5 = lecturaEntity.getConsumo_mes_5();
        consumoMes4 = lecturaEntity.getConsumo_mes_4();
        consumoMes3 = lecturaEntity.getConsumo_mes_3();
        consumoMes2 = lecturaEntity.getConsumo_mes_2();
        consumoMes1 = lecturaEntity.getConsumo_mes_1();
        if (!lecturaEntity.getConsumo_complementario().equals("")) {
            ConsumoComplementario = Integer.parseInt(lecturaEntity.getConsumo_complementario());
        } else {
            ConsumoComplementario = 0;
        }
        lecturaAnterior = lecturaEntity.getLectura_anterior();
        consumoBasico = lecturaEntity.getConsumo_basico();
        promedio = (int) Math.floor(((double) (consumoMes1 + consumoMes2 + consumoMes3 + consumoMes4 + consumoMes5 + consumoMes6) / (double) 6));

        acueductoCargoFijo = lecturaEntity.getAcueducto_cargo_fijo();
        acueductoValorMtr3 = lecturaEntity.getAcueducto_valor_mtr_3();

        alcantarilladoCargoFijo = lecturaEntity.getAlcantarillado_cargo_fijo();
        alcantarilladoValorMtr3 = lecturaEntity.getAlcantarillado_valor_mtr3();

        aseoCargoFijo = lecturaEntity.getAseo_cargo_fijo();
        aseoSubsidioBasico = lecturaEntity.getAseo_subsidio_basico();
        aseoMora = lecturaEntity.getAseo_mora();
        if (!lecturaEntity.getDeuda_anterior().equals("")) {
            deudaAnterior = lecturaEntity.getDeuda_anterior();
        } else {
            deudaAnterior = 0;
        }
        acueductoConsumoBasico = lecturaEntity.getAcueducto_consumo_basico();
        consumo = lecturaEntity.getConsumo();

        if (lecturaEntity.getLectura_actual() == null || !lecturaEntity.getLectura_actual().equals("")) {
            txtlectura.setText(lecturaEntity.getLectura_actual());
            txtlectura.setEnabled(lecturaEntity.getLectura_actual() == null || lecturaEntity.getLectura_actual().isEmpty() || !lecturaEntity.getLectura_actual().equals(""));
        }
        if (claseOrigen.equals(FacturasImpresas.class.toString()) && !txtlectura.getText().toString().isEmpty()) {
            txtlectura.setEnabled(false);
        }
        btnAtras.setEnabled(true);
        btnNext.setEnabled(true);

    }

    public void asignarDataDesdeDB() {
        SQLiteDatabase db = admin.getReadableDatabase();
        String sector;
        if (MainActivity.offline2 == 0) {
            sector = MainActivity.SelectedSector;
        } else {
            sector = FacturasImpresas.SelectedSector;
        }
        usuarioActual = null;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM lectura WHERE sector =? and identificador =?", new String[]{sector, identificador});
        usuarioActual = new JSONObject();
        if (cursor.moveToNext()) {

            for (String col : cursor.getColumnNames()) {
                try {
                    usuarioActual.put(col, cursor.getString(cursor.getColumnIndex(col)));

                    if (cursor.getString(cursor.getColumnIndex(col)) == null) {
                        usuarioActual.put(col, "");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            asignarDatos(usuarioActual);

        }
    }

    public void asignarEventualidadDesdeDB() {

//02030791000  0102 3   -   09010545200
        SQLiteDatabase db = admin.getReadableDatabase();
        listaEventualidad = new ArrayList<ClEventualidad>();
        Cursor cursor = db.rawQuery("SELECT id_unico,nombre,tipo_eventualidad,valor FROM eventualidad", new String[]{});

        while (cursor.moveToNext()) {
            Integer idUnico = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Integer idTipo = cursor.getInt(2);
            Integer valor = cursor.getInt(3);
            listaEventualidad.add(new ClEventualidad(idUnico, nombre, false, idTipo, valor));
        }
        //  obtenerRuta();

        adapter = new EventualidadAdapter(RegistrarFactura.this, listaEventualidad);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    public void cargarLectura() {
        l = 1;
        crearEventoBotonesDesplazamiento("?", 0).onClick(null);
    }

    private View.OnClickListener crearEventoBotonesDesplazamiento(final String condicion, final int direccion) {//direccion 0 sin direccion 1 adelante 2 atras
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reemplazo = false;
                resetearListaEventualidades();
                Numero = (EditText) findViewById(R.id.edtNumero);
                if (direccion == 1) {
                    if (indiceActual + 1 < listaReferencia.size()) {
                        getDataPrincipal(listaReferencia.get(indiceActual + 1).texto, -1);


                    } else {
                        Toast.makeText(RegistrarFactura.this, "No existe un registro siguiente", Toast.LENGTH_SHORT).show();
                    }
                } else if (direccion == 2) {
                    if (indiceActual - 1 >= 0) {
                        getDataPrincipal(listaReferencia.get(indiceActual - 1).texto, -1);
                    } else {
                        Toast.makeText(RegistrarFactura.this, "No existe un registro anterior", Toast.LENGTH_SHORT).show();
                    }
                }
                asignarDataDesdeDB();
            }
        };
    }

    private void resetearListaEventualidades() {
        //este metodo debe recorrer listEventualidades y resetear en falso el isSelect de todas
        for (int i = 0; i < listaEventualidad.size(); i++) {
            if (listaEventualidad.get(i).getIsSelected()) {
                listaEventualidad.get(i).setIsSelected(false);
                tipoEventualidad = 0;

            }
            adapter.updateRecords(listaEventualidad);
        }

    }


    public void updateBd(Map<String, Object> usuarioActualCopia) {
        ContentValues parametros = new ContentValues();
        for (String parament : vec) {
            parametros.put(parament, String.valueOf(usuarioActualCopia.get(parament)));
        }
        parametros.put("lectura_actual", txtlectura.getText().toString());
        parametros.put("consumo", String.valueOf(consumoFacturado));
        parametros.put("consumo_facturado", String.valueOf(consumoFacturado));

        if (eventualidades == null) {
            eventualidades = "0";
        }

        parametros.put("eventualidad", eventualidades);
        parametros.put("observaciones_financiacion", observaciones_financiacion);
        parametros.put("observaciones", observaciones);
        parametros.put("ajuste_peso", String.valueOf(usuarioActualCopia.get("AJUSTE")));
        SimpleDateFormat dfff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateee = dfff.format(new Date());
        parametros.put("fecha_factura", formattedDateee);
        parametros.put("total_acueducto", total_acueducto);
        parametros.put("total_alcantarillado", total_alcantarillado);
        parametros.put("total_acdto_alc", total_acdto_alc);
        parametros.put("observaciones", observaciones);
        parametros.put("observaciones_financiacion", observaciones_financiacion);

        parametros.put("identificador", identificador);


        int can = admin.updateDatos(parametros, identificador);

        if (can == 1) {
            Toast.makeText(RegistrarFactura.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
            usuarioActual = null;
            printFacturaCalculada(usuarioActualCopia);
            mostrarSiguienteRegistro();
            txtlectura.setText(null);
            txtlectura.setEnabled(true);
            resetearListaEventualidades();
        } else {
            Toast.makeText(RegistrarFactura.this, "Error no se registro la lectura", Toast.LENGTH_SHORT).show();

        }
    }


    public Map<String, Object> getCopyUser() {
        Map<String, Object> copy = new HashMap<>();
        Iterator<String> keys = usuarioActual.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = null;
            try {
                value = usuarioActual.get(key);
                copy.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        usuarioActual = null;
        return copy;
    }

    public void mostrarSiguienteRegistro() {
        if (!MainActivity.factur) {
            if (indiceActual + 1 < listaReferencia.size()) {
                ItemLista aux = listaReferencia.get(indiceActual);
                getDataPrincipal(listaReferencia.get(indiceActual + 1).texto, -1);
                String actual = listaReferencia.get(indiceActual).texto;
                listaReferencia.remove(aux);
                getDataPrincipal(actual, -1);
                crearEventoBotonesDesplazamiento("?", 0).onClick(null);
            } else {
                ItemLista aux = listaReferencia.get(indiceActual);
                listaReferencia.remove(aux);
                Toast.makeText(RegistrarFactura.this, "No existe un registro siguiente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrarFactura.this, MainActivity.class));
                finish();
            }
            helper.dismissLoadingDialog();
        } else {
            crearEventoBotonesDesplazamiento("(select min(identificador) from lectura where identificador > ?)", 1).onClick(null);
        }
    }

    public void printFacturaCalculada(final Map<String, Object> usuario) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                Looper.prepare();
                enviarArchivoImpresora(usuario);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void enviarArchivoImpresora(Map<String, Object> usuario) {
        PrintUtil.sendFile(this, macAddress, helper, usuario);
    }

    public void despejarFormulas() {
        Iterator<String> keys = usuarioActual.keys();
        Pattern regex = Pattern.compile("\\&([^&]*)&"); //PATRON PARA BUSCAR CONTENIDO ENTRE &&
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("acueducto_consumo_basico")) {
                System.out.println(usuarioActual);
            }
            try {
                String value = usuarioActual.getString(key);
                if (value.contains("&")) {
                    Matcher regexMatcher = regex.matcher(value);
                    while (regexMatcher.find()) {//Encuentra un patron coincidente en la cadena
                        String var = regexMatcher.group();
                        String varPr = regexMatcher.group(1);
                        if (value.contains(var) && usuarioActual.has(varPr)) {
                            value = value.replace(var, usuarioActual.getString(varPr));//Obteniendo grupo de cadena
                        }
                    }
                    if (tipoEventualidad == 3 && Integer.parseInt(lecturaActual) < lecturaAnterior) {
                        if (key.equals("acueducto_cargo_fijo") || key.equals("acueducto_subsido_cargo_fijo") || key.equals("alcantarillado_cargo_fijo") || key.equals("alcantarillado_subsido_cargo_fijo") || key.equals("aseo_cargo_fijo") || key.equals("aseo_subsido_cargo_fijo")) {
                            value = value.replace("AND", "&&");
                            Expression expression = new Expression(value);
                            try {
                                usuarioActual.put(key, Math.round(Double.parseDouble(String.valueOf(expression.eval()))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            usuarioActual.put(key, "0");
                        }
                    } else {
                        value = value.replace("AND", "&&");
                        Expression expression = new Expression(value);
                        try {
                            usuarioActual.put(key, Math.round(Double.parseDouble(String.valueOf(expression.eval()))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public double redondear(Double valor) {
        double d = valor;
        double roundDbl = Math.round(d * 100.0) / 100.0;
        return roundDbl;
    }

    public String SinCeros(String lecturaEn) {
        s = lecturaEn;
        s = s.replaceFirst("^0*", "");

        return s;
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void extraerDatosJson(Map<String, Object> usuario) {
        if (acueductoCargoFijo.equals("")) {
            acueductoCargoFijo = "0";
        }
        if (acueductoValorMtr3.equals("")) {
            acueductoValorMtr3 = "0";
        }
        if (alcantarilladoCargoFijo.equals("")) {
            alcantarilladoCargoFijo = "0";
        }
        if (alcantarilladoValorMtr3.equals("")) {
            alcantarilladoValorMtr3 = "0";
        }
        acueductoConsumoBasico = usuario.get("acueducto_consumo_basico").toString();
        if (acueductoConsumoBasico.equals("")) {
            acueductoConsumoBasico = "0";
        }
        acueductoConsumoComplementario = usuario.get("acueducto_consumo_complementario").toString();
        if (acueductoConsumoComplementario.equals("")) {
            acueductoConsumoComplementario = "0";
        }
        acueductoConsumoSuntuario = usuario.get("acueducto_consumo_suntuario").toString();
        if (acueductoConsumoSuntuario.equals("")) {
            acueductoConsumoSuntuario = "0";
        }
        acueductoSubsidoCargoFijo = usuario.get("acueducto_subsido_cargo_fijo").toString();
        if (acueductoSubsidoCargoFijo.equals("")) {
            acueductoSubsidoCargoFijo = "0";
        }
        acueductoSubsidoBasico = usuario.get("acueducto_subsido_basico").toString();
        if (acueductoSubsidoBasico.equals("")) {
            acueductoSubsidoBasico = "0";
        }
        acueductoSubsidoComplementario = usuario.get("acueducto_subsido_complementario").toString();
        if (acueductoSubsidoComplementario.equals("")) {
            acueductoSubsidoComplementario = "0";
        }
        acueductoSubsidoSuntuario = usuario.get("acueducto_subsido_suntuario").toString();
        if (acueductoSubsidoSuntuario.equals("")) {
            acueductoSubsidoSuntuario = "0";
        }
        acueductoContribucion = usuario.get("acueducto_contribucion").toString();
        if (acueductoContribucion.equals("")) {
            acueductoContribucion = "0";
        }
        acueductoMora = usuario.get("acueducto_mora").toString();
        if (acueductoMora.equals("")) {
            acueductoMora = "0";
        }
        acueductoConcepto1 = usuario.get("acueducto_concepto1").toString();
        if (acueductoConcepto1.equals("")) {
            acueductoConcepto1 = "0";
        }
        acueductoConcepto2 = usuario.get("acueducto_concepto2").toString();
        if (acueductoConcepto2.equals("")) {
            acueductoConcepto2 = "0";
        }
        acueductoConcepto3 = usuario.get("acueducto_concepto3").toString();
        if (acueductoConcepto3.equals("")) {
            acueductoConcepto3 = "0";
        }
        alcantarilladoConsumoBasico = usuario.get("alcantarillado_consumo_basico").toString();
        if (alcantarilladoConsumoBasico.equals("")) {
            alcantarilladoConsumoBasico = "0";
        }
        alcantarilladoConsumoComplementario = usuario.get("alcantarillado_consumo_complementario").toString();
        if (alcantarilladoConsumoComplementario.equals("")) {
            alcantarilladoConsumoComplementario = "0";
        }
        alcantarilladoConsumoSuntuario = usuario.get("alcantarillado_consumo_suntuario").toString();
        if (alcantarilladoConsumoSuntuario.equals("")) {
            alcantarilladoConsumoSuntuario = "0";
        }
        alcantarilladoSubsidoCargoFijo = usuario.get("alcantarillado_subsido_cargo_fijo").toString();
        if (alcantarilladoSubsidoCargoFijo.equals("")) {
            alcantarilladoSubsidoCargoFijo = "0";
        }
        alcantarilladoSubsidoBasico = usuario.get("alcantarillado_subsido_basico").toString();
        if (alcantarilladoSubsidoBasico.equals("")) {
            alcantarilladoSubsidoBasico = "0";
        }
        alcantarilladoSubsidoComplementario = usuario.get("alcantarillado_subsido_complementario").toString();
        if (alcantarilladoSubsidoComplementario.equals("")) {
            alcantarilladoSubsidoComplementario = "0";
        }
        alcantarilladoSubsidoSuntuario = usuario.get("alcantarillado_subsido_suntuario").toString();
        if (alcantarilladoSubsidoSuntuario.equals("")) {
            alcantarilladoSubsidoSuntuario = "0";
        }
        alcantarilladoContribucion = usuario.get("alcantarillado_contribucion").toString();
        if (alcantarilladoContribucion.equals("")) {
            alcantarilladoContribucion = "0";
        }
        alcantarilladoMora = usuario.get("alcantarillado_mora").toString();
        if (alcantarilladoMora.equals("")) {
            alcantarilladoMora = "0";
        }
        alcantarilladoConcepto1 = usuario.get("alcantarillado_concepto1").toString();
        if (alcantarilladoConcepto1.equals("")) {
            alcantarilladoConcepto1 = "0";
        }
        alcantarilladoConcepto2 = usuario.get("alcantarillado_concepto2").toString();
        if (alcantarilladoConcepto2.equals("")) {
            alcantarilladoConcepto2 = "0";
        }
        alcantarilladoConcepto3 = usuario.get("alcantarillado_concepto3").toString();
        if (alcantarilladoConcepto3.equals("")) {
            alcantarilladoConcepto3 = "0";
        }
        aseoValorMtr3 = usuario.get("aseo_valor_mtr_3").toString();
        if (aseoValorMtr3.equals("")) {
            aseoValorMtr3 = "0";
        }
        aseoCargoFijo = usuario.get("aseo_cargo_fijo").toString();
        if (aseoCargoFijo.equals("")) {
            aseoCargoFijo = "0";
        }
        aseoSubsidoCargoFijo = usuario.get("aseo_cargo_fijo").toString();
        if (aseoSubsidoCargoFijo.equals("")) {
            aseoSubsidoCargoFijo = "0";
        }
        aseoConsumoBasico = usuario.get("aseo_consumo_basico").toString();
        if (aseoConsumoBasico.equals("")) {
            aseoConsumoBasico = "0";
        }
        aseoConsumoComplementario = usuario.get("aseo_consumo_complementario").toString();
        if (aseoConsumoComplementario.equals("")) {
            aseoConsumoComplementario = "0";
        }
        aseoConsumoSuntuario = usuario.get("aseo_consumo_suntuario").toString();
        if (aseoConsumoSuntuario.equals("")) {
            aseoConsumoSuntuario = "0";
        }
        aseoSubsidoBasico = usuario.get("aseo_subsido_basico").toString();
        if (aseoSubsidoBasico.equals("")) {
            aseoSubsidoBasico = "0";
        }
        aseoSubsidoComplementario = usuario.get("aseo_subsido_complementario").toString();
        if (aseoSubsidoComplementario.equals("")) {
            aseoSubsidoComplementario = "0";
        }
        aseoSubsidoSuntuario = usuario.get("aseo_subsido_suntuario").toString();
        if (aseoSubsidoSuntuario.equals("")) {
            aseoSubsidoSuntuario = "0";
        }
        aseoContribucion = usuario.get("aseo_contribucion").toString();
        if (aseoContribucion.equals("")) {
            aseoContribucion = "0";
        }
        aseoMora = usuario.get("aseo_mora").toString();
        if (aseoMora.equals("")) {
            aseoMora = "0";
        }
        aseoConcepto1 = usuario.get("aseo_concepto1").toString();
        if (aseoConcepto1.equals("")) {
            aseoConcepto1 = "0";
        }
        aseoConcepto2 = usuario.get("aseo_concepto2").toString();
        if (aseoConcepto2.equals("")) {
            aseoConcepto2 = "0";
        }
        aseoConcepto3 = usuario.get("aseo_concepto3").toString();
        if (aseoConcepto3.equals("")) {
            aseoConcepto3 = "0";
        }


        Double consumomenor = null, consumomayor = null, total = null;
        if (!usuario.get("atraso").toString().equals("")) {
            atraso = String.valueOf(usuario.get("atraso"));
        } else {
            atraso = "0";
        }

        usuario.put("factura", numeroFactura);
        usuario.put("codigoRuta", codigoRuta);
        usuario.put("ruta", codigoRuta);
        usuario.put("referencia", codigoReferencia);
        usuario.put("usuario", this.usuario);
        usuario.put("direccion", direccion);
        usuario.put("codigoReferencia", codigoReferencia);
        usuario.put("fecha", fecha(fecha));
        if (fechaFactura.equals("") && !claseOrigen.equals(FacturasImpresas.class.toString()) && !MainActivity.factur) {
            fechaFactura = formattedDatee;
        } else if (!MainActivity.factur) {
            fechaFactura = formattedDatee;
        } else {
            fechaFactura = usuario.get("fecha_factura").toString();
        }

        String fecha_suspension = usuario.get("fecha_suspension").toString();
        if (fecha_suspension.equals("")) {
            fecha_suspension = "";
            usuario.put("fechaSuspension", fecha_suspension);
        } else {
            if (deudaAnterior > 0) {
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
                String strFecha = fecha_suspension;
                Date date2 = null;
                try {

                    date2 = formatoDelTexto.parse(strFecha);

                } catch (ParseException ex) {

                    ex.printStackTrace();

                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date2); // Configuramos la fecha que se recibe
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String fecha_suspension2 = format1.format(calendar.getTime());

                usuario.put("fechaSuspension", fecha(fecha_suspension2));
            } else {
                fecha_suspension = "";
                usuario.put("fechaSuspension", fecha_suspension);
            }
        }
        String[] fe = fechaFactura.split(" ");
        String fe1 = fe[0];

        usuario.put("fecha_factura", fecha(fe1));

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
        String strFecha = fecha(fe1);
        Date date1 = null;
        try {

            date1 = formatoDelTexto.parse(strFecha);

        } catch (ParseException ex) {

            ex.printStackTrace();

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFactura2 = format1.format(calendar.getTime());


        usuario.put("fechaFactura2", fecha(fechaFactura2));

        if (!fechaVencimiento.equals("")) {
            usuario.put("fechaVencimiento", fecha(fechaVencimiento));
        } else {
            usuario.put("fechaVencimiento", "");
        }

        usuario.put("periodo", periodo);
        String[] parts = estrato.split(" ()");
        String part1 = parts[0];
        estrato = part1;
        usuario.put("estrato", estrato);
        usuario.put("atraso", atraso);
        usuario.put("uso", uso);
        usuario.put("Fecha de Entrega", formattedDate);
        if (numeroMedidor.equals("")) {
            numeroMedidor = "";
        }

        String mes6 = null, mes5 = null, mes4 = null, mes3 = null, mes2 = null, mes1 = null;
        usuario.put("numeroMedidor", numeroMedidor);

        for (int i = 0; i < 7; i++) {
            SimpleDateFormat meses = new SimpleDateFormat("dd/MM/yyyy");
            String mesInicial = "01/" + periodo;
            Date dateMeses = null;
            try {

                dateMeses = meses.parse(mesInicial);

            } catch (ParseException ex) {

                ex.printStackTrace();

            }
            Calendar calendarMeses = Calendar.getInstance();
            // Configuramos la fecha que se recibe
            calendarMeses.add(Calendar.MONTH, -i);
            SimpleDateFormat formatMeses = new SimpleDateFormat("dd/MM/yy");
            String fecha_meses = formatMeses.format(calendarMeses.getTime());
            if (i == 1) {
                mes6 = fecha3(fecha_meses);
            } else if (i == 2) {
                mes5 = fecha3(fecha_meses);
            } else if (i == 3) {
                mes4 = fecha3(fecha_meses);
            } else if (i == 4) {
                mes3 = fecha3(fecha_meses);
            } else if (i == 5) {
                mes2 = fecha3(fecha_meses);
            } else if (i == 6) {
                mes1 = fecha3(fecha_meses);
            }
        }

        usuario.put("Mes6", mes6);
        usuario.put("Mes5", mes5);
        usuario.put("Mes4", mes4);
        usuario.put("Mes3", mes3);
        usuario.put("Mes2", mes2);
        usuario.put("Mes1", mes1);


        usuario.put("consumoMes6", consumoMes6);
        usuario.put("consumoMes5", consumoMes5);
        usuario.put("consumoMes4", consumoMes4);
        usuario.put("consumoMes3", consumoMes3);
        usuario.put("consumoMes2", consumoMes2);
        usuario.put("consumoMes1", consumoMes1);
        usuario.put("lecturaAnterior", lecturaAnterior);
        usuario.put("lecturaActual", lecturaActual);
        String partConsumo = String.valueOf(consumoFacturado);
        String[] partConsu = partConsumo.split("\\.");
        String Consu1 = partConsu[0]; //obtiene: 19
          //obtiene: 19-A

        usuario.put("consumoFacturado", Consu1);
        usuario.put("promedio", promedio);


        usuario.put("eventualidades", (eventualidades == null ? "" : eventualidadActual.getNombre()));

        String observaciones_otros_cobro = String.valueOf(usuario.get("observaciones_otros_cobros").equals("") ? "": usuario.get("observaciones_otros_cobros"));
        String observaciones_otros_cobros2 = "";
        String observaciones_otros_cobros = "";
        try {
            String[] part = observaciones_otros_cobro.split(",");
            observaciones_otros_cobros = part[0];
            observaciones_otros_cobros2 = part[1]; //obtiene: 19
            usuario.put("observaciones_otros_cobros", observaciones_otros_cobros);
            usuario.put("observaciones_otros_cobros2", observaciones_otros_cobros2);

        }catch (Exception e){
            usuario.put("observaciones_otros_cobros", "");
            usuario.put("observaciones_otros_cobros2", "");
        }




        usuario.put("fecha_vencimiento",fecha(fechaVencimiento));
        if (consumoBasico <= 11) {
            if (consumoFacturado < consumoBasico) {
                consumomenor = Double.parseDouble(String.valueOf(consumoFacturado));
            } else {
                consumomenor = Double.valueOf(consumoBasico);
            }
        }
        if (consumoFacturado > 11) {
            consumomayor = Double.parseDouble( String.valueOf(consumoFacturado)) - consumoBasico;
        } else {
            consumomayor = 0.0;
        }
        total = consumomenor + consumomayor;

        usuario.put("consumomenor", consumomenor);
        usuario.put("consumomayor", consumomayor);
        usuario.put("total", total);
        usuario.put("acueductoValorMtr3", acueductoValorMtr3);
        usuario.put("acueductoValorMtr3", acueductoValorMtr3);
        usuario.put("alcantarilladoValorMtr3", alcantarilladoValorMtr3);
        usuario.put("alcantarilladoValorMtr3", alcantarilladoValorMtr3);

        Double acueductoCargoF = Double.valueOf(Math.round(Double.parseDouble(acueductoCargoFijo)));
        Double acueductoSubsidoCargoF = Double.parseDouble(acueductoSubsidoCargoFijo);
        Double acueductoConsumoB = Double.parseDouble(acueductoConsumoBasico);
        Double acueductoSubsidoB = Double.parseDouble(acueductoSubsidoBasico);
        Double acueductoConsumoComplement = Double.parseDouble(acueductoConsumoComplementario);
        Double acueductoSubsidoComplement = Double.parseDouble(acueductoSubsidoComplementario);
        Double acueductoConsumoSuntu = Double.parseDouble(acueductoConsumoSuntuario);
        Double acueductoSubsidioSuntu = Double.parseDouble(acueductoSubsidoSuntuario);
        //Double tot = redondear(acueductoCargoF) - redondear(acueductoSubsidoCargoF) + redondear(acueductoConsumoB) - redondear(acueductoSubsidoB) + redondear(acueductoConsumoComplement) - redondear(acueductoSubsidoComplement) + redondear(acueductoConsumoSuntu) - redondear(acueductoSubsidioSuntu);
        Double tot = acueductoCargoF - acueductoSubsidoCargoF + acueductoConsumoB - acueductoSubsidoB + acueductoConsumoComplement - acueductoSubsidoComplement + acueductoConsumoSuntu - acueductoSubsidioSuntu;

        Double tot3 = tot;
        usuario.put("acueductoCargoFijo", admin.formatoSalida2(acueductoCargoFijo));
        usuario.put("acueductoSubsidoCargoFijo", "-" + admin.formatoSalida2(acueductoSubsidoCargoFijo));
        usuario.put("acueductoConsumoBasico", admin.formatoSalida2(acueductoConsumoBasico));
        usuario.put("acueductoSubsidoBasico", "-" + admin.formatoSalida2(acueductoSubsidoBasico));
        usuario.put("acueductoConsumoComplementario", admin.formatoSalida2(acueductoConsumoComplementario));
        usuario.put("acueductoSubsidoComplementario", "-" + admin.formatoSalida2(acueductoSubsidoComplementario));
        usuario.put("acueductoConsumoSuntuario", admin.formatoSalida2(acueductoConsumoSuntuario));

        //tot = Double.valueOf(Math.round(tot));
        total_acueducto = String.valueOf(redondear(tot));
        if (tot < 0) {
            tot = 0.0;
        }
        usuario.put("tot", admin.formatoSalida2(String.valueOf(tot)));


        Double alcantarilladoCargoF = Double.valueOf(Math.round(Double.parseDouble(alcantarilladoCargoFijo)));
        Double alcantarilladoSubsidoCargoF = Double.parseDouble(alcantarilladoSubsidoCargoFijo);
        Double alcantarilladoConsumoB = Double.parseDouble(alcantarilladoConsumoBasico);
        Double alcantarilladoSubsidoB = Double.parseDouble(alcantarilladoSubsidoBasico);
        Double alcantarilladoConsumoComplement = Double.parseDouble(alcantarilladoConsumoComplementario);
        Double alcantarilladoSubsidoComplement = Double.parseDouble(alcantarilladoSubsidoComplementario);
        Double alcantarilladoConsumoSuntu = Double.parseDouble(alcantarilladoConsumoSuntuario);
        Double alcantarilladoSubsidioSuntu = Double.parseDouble(alcantarilladoSubsidoSuntuario);
        //Double tot2 = redondear(alcantarilladoCargoF) - redondear(alcantarilladoSubsidoCargoF) + redondear(alcantarilladoConsumoB) - redondear(alcantarilladoSubsidoB) + redondear(alcantarilladoConsumoComplement) - redondear(alcantarilladoSubsidoComplement) + redondear(alcantarilladoConsumoSuntu) - redondear(alcantarilladoSubsidioSuntu);
        Double tot2 = alcantarilladoCargoF - alcantarilladoSubsidoCargoF + alcantarilladoConsumoB - alcantarilladoSubsidoB + alcantarilladoConsumoComplement - alcantarilladoSubsidoComplement + alcantarilladoConsumoSuntu - alcantarilladoSubsidioSuntu;
        Double tot4 = tot2;
        //tot2 = Double.valueOf(Math.round(tot2));
        total_alcantarillado = String.valueOf(redondear(tot2));

        usuario.put("alcantarilladoCargoFijo", admin.formatoSalida2(alcantarilladoCargoFijo));
        usuario.put("alcantarilladoSubsidoCargoFijo", "-" + admin.formatoSalida2(alcantarilladoSubsidoCargoFijo));
        usuario.put("alcantarilladoConsumoBasico", admin.formatoSalida2(alcantarilladoConsumoBasico));
        usuario.put("alcantarilladoSubsidoBasico", "-" + admin.formatoSalida2(alcantarilladoSubsidoBasico));
        usuario.put("alcantarilladoConsumoComplementario", admin.formatoSalida2(alcantarilladoConsumoComplementario));
        usuario.put("alcantarilladoSubsidoComplementario", "-" + admin.formatoSalida2(alcantarilladoSubsidoComplementario));
        usuario.put("alcantarilladoConsumoSuntuario", admin.formatoSalida2(alcantarilladoConsumoSuntuario));
        if (tot2 < 0) {
            tot2 = 0.0;
        }
        usuario.put("tot2", admin.formatoSalida2(String.valueOf(tot2)));

        observaciones_financiacion = usuario.get("observaciones_financiacion").toString();
        String concepto = ""; //obtiene: 19
        String valor = ""; //obtiene: 19-A
        String cuotas = "";
        String saldo = ""; //obtiene: 19-A
        String CPend = "";
        String valorCuota = "";

        String concepto2 = ""; //obtiene: 19
        String valor2 = ""; //obtiene: 19-A
        String cuotas2 = "";
        String saldo2 = ""; //obtiene: 19-A
        String CPend2 = "";
        String valorCuota2 = "";

        String concepto3 = ""; //obtiene: 19
        String valor3 = ""; //obtiene: 19-A
        String cuotas3 = "";
        String saldo3 = ""; //obtiene: 19-A
        String CPend3 = "";
        String valorCuota3 = "";

        String observaciones_financiacion3 = "";
        if (!observaciones_financiacion.equals("")) {
            String[] part = observaciones_financiacion.split(",");
            concepto = part[0]; //obtiene: 19
            valor = part[1]; //obtiene: 19-A
            cuotas = part[2];
            saldo = part[3]; //obtiene: 19-A
            CPend = part[4];
            valorCuota =  part[5];
            String[] part2 = observaciones_financiacion.split("&");
            try {
                String observaciones_financiacion2  = part2[1];
                if (!observaciones_financiacion2.equals("")){
                    String[] part3 = observaciones_financiacion2.split(",");
                    concepto2 = part3[0]; //obtiene: 19
                    valor2 = part3[1]; //obtiene: 19-A
                    cuotas2 = part3[2];
                    saldo2 = part3[3]; //obtiene: 19-A
                    CPend2 = part3[4];
                    valorCuota2 =  part3[5];
                }
            }catch (Exception e){}

            try {
                observaciones_financiacion3 = part2[2];
                if (!observaciones_financiacion3.equals("")){
                    String[] part4 = observaciones_financiacion3.split(",");
                    concepto3 = part4[0]; //obtiene: 19
                    valor3 = part4[1]; //obtiene: 19-A
                    cuotas3 = part4[2];
                    saldo3 = part4[3]; //obtiene: 19-A
                    CPend3 = part4[4];
                    valorCuota3 =  part4[5];
                }
            }catch (Exception e){

            }

        }
        if (observaciones_financiacion.equals("")) {

            concepto = "";
            valor = "";
            cuotas = "";
            saldo = "";
            CPend = "";
            valorCuota= "";
        }


        usuario.put("concepto", concepto);
        usuario.put("valor", valor);
        usuario.put("cuotas", cuotas);
        usuario.put("saldo", saldo);
        usuario.put("C.Pend", CPend);
        usuario.put("valorCuota", valorCuota);

        usuario.put("concepto2", concepto2);
        usuario.put("valor2", valor2);
        usuario.put("cuotas2", cuotas2);
        usuario.put("saldo2", saldo2);
        usuario.put("C.Pend2", CPend2);
        usuario.put("valorCuota2", valorCuota2);

        usuario.put("concepto3", concepto3);
        usuario.put("valor3", valor3);
        usuario.put("cuotas3", cuotas3);
        usuario.put("saldo3", saldo3);
        usuario.put("C.Pend3", CPend3);
        usuario.put("valorCuota3", valorCuota3);

        String financiacion;

        if (usuario.get("financiacion").toString() != null) {
            financiacion = (usuario.get("financiacion").toString());
            if (financiacion.equals("")) {
                financiacion = "0";
                String financiacion2 = "";
                usuario.put("subTotal", financiacion2);

            } else {
                if (financiacion != null) {
                    usuario.put("subTotal", admin.formatoSalida2(financiacion));

                } else {
                    String financiacion2 = "";

                    usuario.put("subTotal", financiacion2);

                }
            }
        } else {
            financiacion = "0";
        }


        Double matricula = 0.0, medidor = 0.0, llaves = 0.0, tapas = 0.0, reconexion = 0.0,financiacioon = 0.0, otrosCobros, suspension = 0.0,geofono, otros_cobros2 = 0.0;

        matricula = Double.parseDouble(String.valueOf(usuario.get("matricula").equals("") ? 0.0: usuario.get("matricula")));
        medidor = Double.parseDouble(String.valueOf(usuario.get("medidor").equals("") ? 0.0: usuario.get("medidor")));
        llaves = Double.parseDouble(String.valueOf(usuario.get("llaves").equals("") ? 0.0: usuario.get("llaves")));
        tapas = Double.parseDouble(String.valueOf(usuario.get("tapas").equals("") ? 0.0: usuario.get("tapas")));
        reconexion = Double.parseDouble(String.valueOf(usuario.get("reconexion").equals("") ? 0.0: usuario.get("reconexion")));
        financiacioon = Double.parseDouble(String.valueOf(usuario.get("financiacion").equals("") ? 0.0: usuario.get("financiacion")));
        suspension = Double.parseDouble(String.valueOf(usuario.get("suspension").equals("") ? 0.0: usuario.get("suspension")));
        geofono = Double.parseDouble(String.valueOf(usuario.get("geofono").equals("") ? 0.0: usuario.get("geofono")));
        otros_cobros2 = Double.parseDouble(String.valueOf(usuario.get("otros_cobros").equals("") ? 0.0: usuario.get("otros_cobros")));
        otrosCobros = matricula + medidor + llaves + tapas + reconexion + financiacioon + suspension + geofono + otros_cobros2;
        usuario.put("financiacion",admin.formatoSalida2(String.valueOf(otrosCobros)));

        usuario.put("aseoMora", aseoMora);
        usuario.put("codigoRuta", codigoRuta);
        usuario.put("numeroFactura", numeroFactura);


        String aseol = usuario.get("aseo").toString();
        String moral = usuario.get("mora").toString();
        String deudal = usuario.get("deuda").toString();
        String tipo_clientel = usuario.get("tipo_cliente").toString();
        String cftl = usuario.get("cft").toString();
        String historico_123l = usuario.get("historico_123").toString();
        String historico_456l = usuario.get("historico_456").toString();
        String subsidiol = usuario.get("subsidio").toString();
        String trbll = usuario.get("trbl").toString();
        String total_aseol = usuario.get("total_aseo").toString();
        String uniResidenciales1 = usuario.get("uni_residenciales").toString();
        String unComerciales1 = usuario.get("un_comerciales").toString();
        String porcentajeSubsidio1 = usuario.get("porcentaje_subsidio").toString();
        String porcentajeContribucion1 = usuario.get("porcentaje_contribucion").toString();
        String frecBarrido1 = usuario.get("frec_barrido").toString();
        String frecRecoleccion1 = usuario.get("frec_recoleccion").toString();
        String ajusteD1 = usuario.get("ajuste_d").toString();
        String ajusteC1 = usuario.get("ajuste_c").toString();
        String tra1 = usuario.get("trna_tafna_tra_tafa").toString();
        String contribucion_aseo1 = usuario.get("contribucion_aseo").toString();
        if (!aseol.equals("")) {
            aseo = SinCeros(aseol);
            if (aseo.equals("")) {
                aseo = "0";
            }
        } else {
            aseo = "0";
        }

        if (!subsidiol.equals("")) {
            subsidio = SinCeros(subsidiol);
            if (subsidio.equals("")) {
                if (!contribucion_aseo1.equals("") && !contribucion_aseo1.equals("")) {

                    subsidio = contribucion_aseo1;
                } else {
                    subsidio = "0";
                }

            }
        } else {
            subsidio = "0";
        }

        if (!moral.equals("")) {
            mora = SinCeros(moral);
            if (mora.equals("")) {
                mora = "0";
            }
        } else {
            mora = "0";
        }

        if (!deudal.equals("")) {
            deuda = SinCeros(deudal);
            if (deuda.equals("")) {
                deuda = "0";
            }
        } else {
            deuda = "0";
        }

        if (!tipo_clientel.equals("")) {
            tipo = tipo_clientel;
            if (tipo.equals("")) {
                tipo = "0";
            }
        } else {
            tipo = "";
        }
        if (!cftl.equals("")) {
            cft = cftl;
        } else {
            cft = "0";
        }
        if (!trbll.equals("")) {
            trbl = trbll;
        } else {
            trbl = "0";
        }
        if (!historico_123l.equals("")) {
            historicos123 = historico_123l;
        } else {
            historicos123 = "0";
        }
        if (!historico_456l.equals("")) {
            historicos456 = historico_456l;
        } else {
            historicos456 = "0";
        }
        if (!uniResidenciales1.equals("")) {
            uniResidenciales = uniResidenciales1;
        } else {
            uniResidenciales = "0";
        }
        if (!unComerciales1.equals("")) {
            unComerciales = unComerciales1;
        } else {
            unComerciales = "0";
        }

        if (!porcentajeSubsidio1.equals("")) {
            porcentajeSubsidio = porcentajeSubsidio1;
        } else {
            porcentajeSubsidio = "0";
        }
        if (!porcentajeContribucion1.equals("")) {
            porcentajeContribucion = porcentajeContribucion1;
        } else {
            porcentajeContribucion = "0";
        }

        if (!frecBarrido1.equals("")) {
            frecBarrido = frecBarrido1;
        } else {
            frecBarrido = "0";
        }
        if (!frecRecoleccion1.equals("")) {
            frecRecoleccion = frecRecoleccion1;
        } else {
            frecRecoleccion = "0";
        }
        if (!ajusteC1.equals("")) {
            ajusteC = SinCeros(ajusteC1);
            if (ajusteC.equals("")) {
                ajusteC = "";
            }
        } else {
            ajusteC = "";
        }
        if (!ajusteD1.equals("")) {
            ajusteD = SinCeros(ajusteD1);
            if (ajusteD.equals("")) {
                ajusteD = "";
            }
        } else {
            ajusteD = "";
        }
        if (!tra1.equals("")) {
            tra = tra1;
            if (tra.equals("")) {
                tra = "0";
            }

        } else {
            tra = "0";
        }

        usuario.put("aseoCargoFijo", admin.formatoSalida(String.valueOf(aseo)));
        usuario.put("aseoSubsidoBasico", admin.formatoSalida(String.valueOf(subsidio)));
        usuario.put("deuda", admin.formatoSalida(String.valueOf(deuda)));
        usuario.put("deudaAnterior", admin.formatoSalida(String.valueOf(deudaAnterior)));
        usuario.put("TIPO", tipo);
        usuario.put("cft", (cft));
        usuario.put("trbl", (trbl));
        usuario.put("historicos123", (historicos123));
        usuario.put("historicos456", (historicos456));
        usuario.put("uni_residenciales", uniResidenciales);
        usuario.put("uni_comerciales", unComerciales);
        usuario.put("porcentaje_subsidio", porcentajeSubsidio);
        usuario.put("porcentaje_contribucion", porcentajeContribucion);
        usuario.put("frec_barrido", frecBarrido);
        usuario.put("frec_recoleccion", frecRecoleccion);
        usuario.put("ajuste_d", ajusteD);
        usuario.put("ajuste_c", ajusteC);
        usuario.put("tra", tra);

        if (lecturaMenor) {
            usuario.put("lectura_abajo", "El consumo esta por debajo del promedio anterior");
        } else {
            usuario.put("lectura_abajo", "");
        }
        if (lecturaMayor) {
            usuario.put("lectura_encima", "El consumo esta por encima del promedioF anterior");
        } else {
            usuario.put("lectura_encima", "");
        }


        double cpto1 = Double.parseDouble(aseo); //RECIBE VALOR CON FORMATO
        if (aseoSubsidoBasico == null) {
            aseoSubsidioBasico = String.valueOf(0);
        }
        double cpto2 = Double.parseDouble(subsidio);
        if (aseoMora.equals("")) {
            aseoMora = String.valueOf(0);
        }
        if (!total_aseol.equals("")) {
            SinCeros(total_aseol);
            if (s.equals("")) {
                s = "0";
            }

        } else {
            s = "0";
        }
        double cpto3 = Double.parseDouble(mora);
        Integer totalCptoAseo = Integer.parseInt(s);


        usuario.put("totalCptoAseo", admin.formatoSalida2(String.valueOf(s)));


        if (acueductoMora.equals("")) {
            acueductoMora = String.valueOf(0);
        }
        if (alcantarilladoMora.equals("")) {
            alcantarilladoMora = String.valueOf(0);
        }
        String abonos1 = "0";
        if (!usuario.get("abonos").toString().equals("")) {
            abonos1 = usuario.get("abonos").toString();
            if (abonos1.equals("")) {
                abonos1 = "0";
            }
        }

        usuario.put("abonos", abonos1);
        if (deudaAnterior == NaN) {
            deudaAnterior = 0.0;
        }
        TOTAL = Double.parseDouble(String.valueOf(tot3)) + Double.parseDouble(String.valueOf(tot2)) + Double.parseDouble(String.valueOf(deudaAnterior)) + Double.parseDouble(acueductoMora) + Double.parseDouble(financiacion) - Double.parseDouble(abonos1) + otrosCobros;
        // TOTAL = Double.valueOf(Math.round(TOTAL));
        usuario.put("interes", admin.formatoSalida(String.valueOf(acueductoMora)));


        usuario.put("total", total);
        usuario.put("deuda_anterior", deudaAnterior);

        TOTAL2 = TOTAL + totalCptoAseo;
        TOTAL2 = Math.round(TOTAL2);
        aux = TOTAL2;
        TOTAL2 = Math.round(((double) TOTAL2 / (double) 100)) * 100;
        aux = TOTAL2 - aux;

        TOTAL = TOTAL + aux;
        TOTAL = Double.valueOf(Math.round(TOTAL));
        if (TOTAL < 0){
            usuario.put("TOTAL", "0");
        }else {
            usuario.put("TOTAL", admin.formatoSalida(String.valueOf(TOTAL)));
        }

        if(TOTAL2 < 0){

            usuario.put("TOTAL2", "0");

        }else{
            usuario.put("TOTAL2", admin.formatoSalida(String.valueOf(TOTAL2)));
        }

        usuario.put("AJUSTE", aux);
        total_acdto_alc = String.valueOf(redondear(TOTAL));

        double TOTAL_SERVICIOS = ((tot)) +
                ((tot2));
        TOTAL_SERVICIOS = Math.round(TOTAL_SERVICIOS);

        usuario.put("TOTAL_SERVICIOS", admin.formatoSalida(String.valueOf(TOTAL_SERVICIOS)));
        Integer tot_f = (int) Math.round(TOTAL);


        totalCptoAseo = (int) Math.round(totalCptoAseo);

        usuario.put("codigo_referencia_f", PrintUtil.padLeftZeros(codigoReferencia, 8));
        usuario.put("TOTAL_f", PrintUtil.padLeftZeros(String.valueOf(tot_f), 10));
        usuario.put("totalCptoAseo_f", PrintUtil.padLeftZeros(String.valueOf(totalCptoAseo), 10));

        Integer total_ff = 0;
        if (tot_f<0){
            usuario.put("TOTAL_ff",PrintUtil.padLeftZeros(String.valueOf(total_ff), 10));
        }else {
            usuario.put("TOTAL_ff", PrintUtil.padLeftZeros(String.valueOf(tot_f), 10));
        }
       int totalCptoAseo_ff = 0;
        if (totalCptoAseo<0) {
            usuario.put("totalCptoAseo_ff", PrintUtil.padLeftZeros(String.valueOf(totalCptoAseo_ff), 10));
        }else{
            usuario.put("totalCptoAseo_ff", PrintUtil.padLeftZeros(String.valueOf(totalCptoAseo), 10));
        }
        usuario.put("fecha_vencimiento_f", PrintUtil.padLeftZeros(fecha2(fechaVencimiento), 8));
        usuario.put("codigoRuta", codigoRuta);
        usuario.put("ruta", codigoRuta);
        usuario.put("referencia", codigoReferencia);

    }

    public String fecha2(String F) {

        String[] parts = F.split("-");
        String part1 = parts[0]; //obtiene: 19
        String part2 = parts[1]; //obtiene: 19-A
        String parte3 = parts[2];

        String Fecha = part1 + part2 + parte3;
        return Fecha;
    }

    public String fecha(String F) {

        String[] parts = F.split("-");
        String part1 = parts[0]; //obtiene: 19
        String part2 = parts[1]; //obtiene: 19-A
        String parte3 = parts[2];

        String Fecha = parte3 + "-" + part2 + "-" + part1;
        return Fecha;
    }

    public String fecha3(String F) {

        String[] parts = F.split("/");
        String part2 = parts[1]; //obtiene: 19-A
        String part3 = parts[2];

        String Fecha = part2 + "-" + part3;
        return Fecha;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        ClEventualidad model = listaEventualidad.get(i);
        if ((MainActivity.factur)) {
            if (model.getIsSelected()) {
                model.setIsSelected(false);
                for (int j = 0; j < listaEventualidad.size(); j++) {
                    if (!listaEventualidad.get(j).getIdUnico().equals(model.getIdUnico())) {
                        listaEventualidad.get(j).setIsSelected(false);
                        }
                }
            } else {

                model.setIsSelected(false);
                for (int j = 0; j < listaEventualidad.size(); j++) {
                    if (!listaEventualidad.get(j).getIdUnico().equals(model.getIdUnico())) {
                        if (!listaEventualidad.get(j).getIsSelected()) {
                            listaEventualidad.get(j).setIsSelected(false);

                        }
                    }
                }
            }
            listaEventualidad.set(i, model);

            //now update adapter
            adapter.updateRecords(listaEventualidad);
        } else {
            if (model.getIsSelected()) {
                model.setIsSelected(false);
                for (int j = 0; j < listaEventualidad.size(); j++) {
                    if (!listaEventualidad.get(j).getIdUnico().equals(model.getIdUnico())) {
                        listaEventualidad.get(j).setIsSelected(false);
                    }
                }
            } else {
                model.setIsSelected(true);
                for (int j = 0; j < listaEventualidad.size(); j++) {
                    if (!listaEventualidad.get(j).getIdUnico().equals(model.getIdUnico())) {
                        listaEventualidad.get(j).setIsSelected(false);
                    }
                }
            }
            listaEventualidad.set(i, model);
            //now update adapter
            adapter.updateRecords(listaEventualidad);
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu mimenu) {
        getMenuInflater().inflate(R.menu.menu, mimenu);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem opcionMenu) {
        int id = opcionMenu.getItemId();

        if (id == R.id.salir) {
            Login.Estado(RegistrarFactura.this, false);
            startActivity(new Intent(RegistrarFactura.this, Login.class));
            finishAffinity();
            return true;
        } else if (id == R.id.confImpresora) {
            startActivity(new Intent(RegistrarFactura.this, ConnectionScreen.class));
            return true;
        } else if (id == R.id.facturasImpresas) {
            startActivity(new Intent(RegistrarFactura.this, FacturasImpresas.class));
            return true;
        } else if (id == R.id.retomar) {
            String ruta = SettingsHelper.verRutaActual(getApplicationContext());
            String registro = SettingsHelper.verRegistroActual(getApplicationContext());
            if (!registro.isEmpty()) {
                MainActivity.SelectedSector = ruta;
                MainActivity.redirecionarRegistrarFactura(registro, this);
                MainActivity.factur = false;
                finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(opcionMenu);
    }

    public void getDataPrincipal(String p, int indice) {
        String[] parts = p.split("  -  ");

        identificador = parts[3];
        ruta = parts[0];
        if (indice == -1) {
            for (int i = 0; i < listaReferencia.size(); i++) {
                if (listaReferencia.get(i).texto.equals(p)) {
                    indiceActual = i;
                    break;
                }
            }
        } else {
            indiceActual = indice;
        }
        if (reemplazo) {
            Numero.setText(String.valueOf(listaReferencia.get(indiceActual).orden));
            reemplazo = false;
        } else {
            if (MainActivity.factur) {
                txtlectura.setText("");
            }
            Numero.setText(String.valueOf(listaReferencia.get(indiceActual).orden));
        }
        if (!MainActivity.factur) {
            SettingsHelper.rutaActual(getApplicationContext(), MainActivity.SelectedSector);
            SettingsHelper.registroActual(getApplicationContext(), p);
        }
    }

    public void mostrarPDF() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Sigiep/Factura.pdf");
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDF(Map<String, Object> usuario) {


        int pagewidth = 390;

        PdfDocument pdfdocument = new PdfDocument();
        Paint paint = new Paint();
        Paint paint_table = new Paint();
        Paint paint_color = new Paint();
        Paint paint_titulo = new Paint();
        Paint paint_texto = new Paint();
        Paint paint_texto2 = new Paint();
        Paint paint_texto_peque = new Paint();
        Paint paint_total = new Paint();
        Paint paint_total2 = new Paint();
        Paint paint_num = new Paint();
        Paint paint_num2 = new Paint();
        Paint paint_final = new Paint();
        Paint paint_Lector = new Paint();
        Paint paint_m3 = new Paint();
        Paint paint_Acueducto = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(390, 975, 1).create();
        PdfDocument.Page page = pdfdocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        //canvas.drawBitmap(scaledbmp,20,35, paint); //IMAGEN
        // Este es canvas.drawBitmap(scaledbmp2,20,35, paint); //IMAGEN

        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextSize(11);
        paint.setTextAlign(Paint.Align.RIGHT);

        paint_titulo.setColor(Color.rgb(0, 0, 0));
        paint_titulo.setTextSize(11);
        paint_titulo.setTextAlign(Paint.Align.CENTER);

        paint_texto.setTextSize(11);
        paint_texto.setTextAlign(Paint.Align.LEFT);

        paint_texto2.setTextSize(8);
        paint_texto2.setTextAlign(Paint.Align.LEFT);
        //paint_texto.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));

        paint_Lector.setTextSize(8);
        paint_Lector.setTextAlign(Paint.Align.LEFT);

        paint_num.setTextSize(11);
        paint_num.setTextAlign(Paint.Align.RIGHT);

        paint_num2.setTextSize(8);
        paint_num.setTextAlign(Paint.Align.RIGHT);
        //paint_texto.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));

        paint_texto_peque.setTextSize(8);
        paint_texto_peque.setTextAlign(Paint.Align.LEFT);

        paint_total.setColor(Color.rgb(0, 0, 0));
        paint_total.setTextSize(17);
        paint_total.setTextAlign(Paint.Align.LEFT);

        paint_total2.setColor(Color.rgb(0, 0, 0));
        paint_total2.setTextSize(14);
        paint_total2.setTextAlign(Paint.Align.LEFT);


        paint_final.setTextSize(17);
        paint_final.setTextAlign(Paint.Align.RIGHT);
        paint_final.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        paint_m3.setTextSize(6);
        paint_m3.setTextAlign(Paint.Align.LEFT);

        paint_Acueducto.setTextSize(8);
        paint_Acueducto.setTextAlign(Paint.Align.CENTER);
        //##########################################################################################


        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);


        //canvas.drawText("EMPRESA DE SERVICIOS PÚBLICOS DE PAZ DE RÍO",pagewidth-20,30, paint); //TEXTO PAZ DE RÍO

        canvas.drawText("FACTURA N° ", 248, 90, paint);
        canvas.drawText(numeroFactura, 270, 90, paint_texto);
        canvas.drawText("DATOS DEL SUSCRIPTOR ", 195, 112, paint_titulo);
        canvas.drawText("Código de Ruta:  " + codigoRuta, 25, 128, paint_texto);
        canvas.drawText("Nombre:  " + this.usuario, 25, 140, paint_texto);
        canvas.drawText("Dirección:  " + direccion, 25, 152, paint_texto);
        canvas.drawText("Código Referencia: " + codigoReferencia, 220, 128, paint_texto);

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);
        String[] fe = fechaFactura.split(" ");
        String fe1 = fe[0];

        canvas.drawText("INFORMACIÓN TÉCNICA ", 195, 174, paint_titulo);
        canvas.drawText("Periodo Facturación:  " + fecha(fecha) + " al " + fecha(fe1), 25, 188, paint_texto);
        canvas.drawText("Periodo :  " + periodo, 25, 200, paint_texto);
        canvas.drawText("Lector :" + usuarioF, 150, 200, paint_Lector);
        canvas.drawText("Estrato:  " + estrato, 25, 212, paint_texto);
        canvas.drawText("Meses Mora:" + atraso, 126, 212, paint_Lector);
        canvas.drawText("Uso:  " + uso, 25, 224, paint_texto);
        canvas.drawText("Fecha de Entrega:" + formattedDate, 25, 237, paint_texto);
        canvas.drawText("Nro. Medidor:  " + numeroMedidor, 25, 249, paint_texto);

        canvas.drawText("Mes6   " + consumoMes6, 220, 200, paint_texto_peque);
        canvas.drawText("Mes5   " + consumoMes5, 220, 210, paint_texto_peque);
        canvas.drawText("Mes4   " + consumoMes4, 220, 220, paint_texto_peque);
        canvas.drawText("Mes3   " + consumoMes3, 220, 230, paint_texto_peque);
        canvas.drawText("Mes2   " + consumoMes2, 220, 240, paint_texto_peque);
        canvas.drawText("Mes1   " + consumoMes1, 220, 250, paint_texto_peque);

        int barras = 251;
        if (!consumoMes6.equals("")) {

            int dt = barras - Integer.parseInt(String.valueOf(consumoMes6)) / 2;
            paint_color.setColor(Color.rgb(0, 0, 0));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(260, dt, 285, 251, paint_color);
        }
        if (!consumoMes5.equals("")) {

            int dt = barras - Integer.parseInt(String.valueOf(consumoMes5)) / 2;
            paint_color.setColor(Color.rgb(0, 0, 0));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(275, dt, 285, 251, paint_color);
        }
        if (!consumoMes4.equals("")) {

            int dt = barras - Integer.parseInt(String.valueOf(consumoMes4)) / 2;
            paint_color.setColor(Color.rgb(0, 0, 0));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(290, dt, 300, 251, paint_color);
        }
        if (!consumoMes3.equals("")) {

            int dt = barras - Integer.parseInt(String.valueOf(consumoMes3)) / 2;
            paint_color.setColor(Color.rgb(0, 0, 0));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(305, dt, 315, 251, paint_color);
        }
        if (!consumoMes2.equals("")) {

            int dt = barras - Integer.parseInt(String.valueOf(consumoMes2)) / 2;
            paint_color.setColor(Color.rgb(0, 0, 0));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(320, dt, 330, 251, paint_color);
        }
        if (!consumoMes1.equals("")) {

            int dt = barras - Integer.parseInt(String.valueOf(consumoMes1)) / 2;
            paint_color.setColor(Color.rgb(0, 0, 0));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(335, dt, 345, 251, paint_color);
        }
//revisar


        canvas.drawText("Lectura Anterior", 106, 267, paint);
        canvas.drawText("Lectura Actual", 190, 267, paint);
        canvas.drawText("Consumo M³", 271, 267, paint);
        canvas.drawText("Promedio", 350, 267, paint);
        canvas.drawText(String.valueOf(lecturaAnterior), 60, 285, paint_texto);
        canvas.drawText(String.valueOf(lecturaActual), 145, 285, paint_texto);
        canvas.drawText(String.valueOf(consumoFacturado), 235, 285, paint_texto);
        canvas.drawText(String.valueOf(promedio), 320, 285, paint_texto);


        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);


        canvas.drawText("AVISO AL SUSCRIPTOR Y/O USUARIO", 195, 304, paint_titulo);
        if (eventualidades != null) {
            if (!eventualidades.equals("0")) {
                canvas.drawText(eventualidades == null ? "" : eventualidadActual.getNombre(), 25, 334, paint_titulo);
            }
        }
        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);


        canvas.drawText("DETALLE DE FACTURACIÓN DEL PERIODO", 195, 364, paint_titulo);

        canvas.drawText("Concepto", 80, 396, paint);
        canvas.drawText("Consumo  m3", 92, 396, paint_m3);

        canvas.drawText("Valor Cargo Fijo o Consumo", 200, 381, paint_Acueducto);
        canvas.drawText("ACUEDUCTO", 164, 396, paint_Acueducto);
        canvas.drawText("ALCANTARILLADO", 228, 396, paint_Acueducto);

        canvas.drawText("V.TOTAL", 310, 381, paint);
        canvas.drawText("ACUEDUCTO", 270, 396, paint_m3);

        canvas.drawText("V.TOTAL", 360, 381, paint);
        canvas.drawText("ALCANTARILLADO", 315, 396, paint_m3);

        canvas.drawText("Cargo Fijo", 25, 415, paint_texto2);
        canvas.drawText("Subsidio Cf", 25, 427, paint_texto2);
        canvas.drawText("consumo <=11 m³", 25, 439, paint_texto2);
        canvas.drawText("Subs. <=11 m³", 25, 451, paint_texto2);
        canvas.drawText("consumo > 11 m³", 25, 463, paint_texto2);
        canvas.drawText("Sobrep. > 11m³", 25, 475, paint_texto2);
        canvas.drawText("ConsumoS. > 11m³", 25, 487, paint_texto2);
        canvas.drawText("TOTAL  ", 25, 499, paint_texto);
        Integer consumomenor = null, consumomayor = null, total = null;
        if (consumoBasico <= 11) {
            if (consumoFacturado < consumoBasico) {
                consumomenor = Integer.parseInt(String.valueOf(consumoFacturado));
            } else {
                consumomenor = consumoBasico;
            }
        }
        if (consumoFacturado > 11) {
            consumomayor = Integer.parseInt(String.valueOf(consumoFacturado)) - consumoBasico;
        } else {
            consumomayor = 0;
        }
        total = consumomenor + consumomayor;


        canvas.drawText(String.valueOf(consumomenor), 106, 439, paint_num2);
        canvas.drawText(String.valueOf(consumomayor), 106, 463, paint_num2);
        canvas.drawText(String.valueOf(total), 106, 500, paint_num2);


        canvas.drawText(admin.formatoSalida2(String.valueOf(acueductoValorMtr3)), 159, 439, paint_num2);
        canvas.drawText(admin.formatoSalida2(String.valueOf(acueductoValorMtr3)), 159, 463, paint_num2);


        double cargoA = admin.formato(String.valueOf(acueductoCargoFijo));
        double m3A = admin.formato(String.valueOf(acueductoValorMtr3));
        double totalA = cargoA + m3A;
        //canvas.drawText(admin.formatoSalida(String.valueOf(totalA))+".00",90,514, paint_texto); //TOTAL ACUEDUCTO

        canvas.drawText(admin.formatoSalida2(String.valueOf(alcantarilladoValorMtr3)), 200, 439, paint_num2);
        canvas.drawText(admin.formatoSalida2(String.valueOf(alcantarilladoValorMtr3)), 200, 463, paint_num2);


        Double acueductoCargoF = Double.parseDouble(acueductoCargoFijo);
        Double acueductoSubsidoCargoF = Double.parseDouble(acueductoSubsidoCargoFijo);
        Double acueductoConsumoB = Double.parseDouble(acueductoConsumoBasico);
        Double acueductoSubsidoB = Double.parseDouble(acueductoSubsidoBasico);
        Double acueductoConsumoComplement = Double.parseDouble(acueductoConsumoComplementario);
        Double acueductoSubsidoComplement = Double.parseDouble(acueductoSubsidoComplementario);
        Double acueductoConsumoSuntu = Double.parseDouble(acueductoConsumoSuntuario);
        Double tot = (acueductoCargoF - acueductoSubsidoCargoF + acueductoConsumoB - acueductoSubsidoB + acueductoConsumoComplement - acueductoSubsidoComplement + acueductoConsumoSuntu);


        canvas.drawText(admin.formatoSalida2(String.valueOf(acueductoCargoFijo)), 317, 415, paint_num);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(acueductoSubsidoCargoFijo)), 317, 427, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(acueductoConsumoBasico)), 317, 439, paint_num);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(acueductoSubsidoBasico)), 317, 451, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(acueductoConsumoComplementario)), 317, 463, paint_num);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(acueductoSubsidoComplementario)), 317, 475, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(acueductoConsumoSuntuario)), 317, 487, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(tot)), 317, 499, paint_num);


        Double alcantarilladoCargoF = Double.parseDouble(alcantarilladoCargoFijo);
        Double alcantarilladoSubsidoCargoF = Double.parseDouble(alcantarilladoSubsidoCargoFijo);
        Double alcantarilladoConsumoB = Double.parseDouble(alcantarilladoConsumoBasico);
        Double alcantarilladoSubsidoB = Double.parseDouble(alcantarilladoSubsidoBasico);
        Double alcantarilladoConsumoComplement = Double.parseDouble(alcantarilladoConsumoComplementario);
        Double alcantarilladoSubsidoComplement = Double.parseDouble(alcantarilladoSubsidoComplementario);
        Double alcantarilladoConsumoSuntu = Double.parseDouble(alcantarilladoConsumoSuntuario);
        Double tot2 = alcantarilladoCargoF - alcantarilladoSubsidoCargoF + alcantarilladoConsumoB -
                alcantarilladoSubsidoB + alcantarilladoConsumoComplement - alcantarilladoSubsidoComplement + alcantarilladoConsumoSuntu;
        tot2 = Double.valueOf(Math.round(tot2));

        canvas.drawText(admin.formatoSalida2(String.valueOf(alcantarilladoCargoFijo)), 367, 415, paint_num);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(alcantarilladoSubsidoCargoFijo)), 367, 427, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(alcantarilladoConsumoBasico)), 367, 439, paint_num);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(alcantarilladoSubsidoBasico)), 367, 451, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(alcantarilladoConsumoComplementario)), 367, 463, paint_num);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(alcantarilladoSubsidoComplementario)), 367, 475, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(alcantarilladoConsumoSuntuario)), 367, 487, paint_num);
        canvas.drawText(admin.formatoSalida2(String.valueOf(tot2)), 367, 499, paint_num);

        double cargoB = admin.formato(String.valueOf(alcantarilladoCargoFijo));
        double m3B = admin.formato(String.valueOf(alcantarilladoValorMtr3));
        double totalB = cargoB + m3B;


        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);


        canvas.drawText("FINANCIACIÓN", 195, 534, paint_titulo);
        canvas.drawText("Concepto", 78, 552, paint);
        canvas.drawText("Valor", 150, 552, paint);
        canvas.drawText("# Cuotas", 205, 552, paint);
        canvas.drawText("Saldo", 245, 552, paint);
        canvas.drawText("C. Pend.", 300, 552, paint);
        canvas.drawText("Valor Cuota", pagewidth - 22, 552, paint);
        canvas.drawText("Subtotal", 72, 600, paint);

        String observaciones_financiacion = usuario.get("observaciones_financiacion").toString();
        String concepto = ""; //obtiene: 19
        String valor = ""; //obtiene: 19-A
        String cuotas = "";
        String saldo = ""; //obtiene: 19-A
        String CPend = "";
        if (!observaciones_financiacion.equals("")) {
            String[] part = observaciones_financiacion.split(",");
            concepto = part[0]; //obtiene: 19
            valor = part[1]; //obtiene: 19-A
            cuotas = part[2];
            saldo = part[3]; //obtiene: 19-A
            CPend = part[4];
        }
        if (observaciones_financiacion.equals("")) {

            concepto = "";
            valor = "";
            cuotas = "";
            saldo = "";
            CPend = "";
        }

        canvas.drawText(concepto, 78, 560, paint);
        canvas.drawText(valor, 150, 560, paint);
        canvas.drawText(cuotas, 205, 560, paint);
        canvas.drawText(saldo, 245, 560, paint);
        canvas.drawText(CPend, 300, 560, paint);


        String financiacion;
        if (usuario.get("financiacion").toString() != null) {
            financiacion = (usuario.get("financiacion").toString());
            if (financiacion.equals("")) {
                financiacion = "0";
                String financiacion2 = "";
                canvas.drawText(financiacion2, pagewidth - 22, 552, paint);
                canvas.drawText(financiacion2, 86, 600, paint);
            } else {
                canvas.drawText(admin.formatoSalida2(financiacion), pagewidth - 22, 552, paint);
                canvas.drawText(admin.formatoSalida2(financiacion), 86, 600, paint);
            }
        } else {
            financiacion = "0";
        }


//----------------------------------------------------------------------------------------------------


        canvas.drawText("RESUMEN FACTURACION ASEO", 150, 882, paint_titulo);
        canvas.drawText("Aseo", 220, 894, paint_texto);
        canvas.drawText("Subsidio", 220, 906, paint_texto);
        canvas.drawText("Deuda ", 220, 918, paint_texto);
        canvas.drawText("Ajuste Debito :", 220, 930, paint_texto);
        canvas.drawText("Ajuste Credito:", 220, 942, paint_texto);
        canvas.drawText("Total:", 254, 952, paint_texto);
        canvas.drawText("CFT/CVNA/VBA: " + cft, 25, 894, paint_texto2);
        canvas.drawText("TRBL/TRLU/TRRA: " + trbl, 25, 906, paint_texto2);
        canvas.drawText("TRNA/TAFNA/TRA TAFA: " + tra, 25, 918, paint_texto2);
        canvas.drawText("HISTORICOS1: " + historicos123, 25, 930, paint_texto2);
        canvas.drawText("HISTORICOS4: " + historicos456, 25, 942, paint_texto2);

        canvas.drawText(admin.formatoSalida2(String.valueOf(aseo)), 323, 894, paint_texto);
        canvas.drawText("-" + admin.formatoSalida2(String.valueOf(subsidio)), 323, 906, paint_texto2);
        canvas.drawText(aseoMora, 343, 918, paint_texto2);
        canvas.drawText(ajusteD, 343, 930, paint_texto2);
        canvas.drawText(ajusteC, 343, 940, paint_texto2);
        canvas.drawText(admin.formatoSalida2(String.valueOf(s)), 343, 952, paint_texto2);
//------------------------------------------------------------------------------------------------------------------------------


        canvas.drawText("RESUMEN FACTURA", 180, 618, paint_titulo);
        canvas.drawText("Acueducto", 20, 631, paint_texto);
        canvas.drawText("Alcantarillado", 20, 643, paint_texto);
        canvas.drawText("Otros Cobros", 20, 655, paint_texto);
        canvas.drawText("Deuda Anterior", 200, 631, paint_texto);
        canvas.drawText("Interes", 200, 643, paint_texto);
        canvas.drawText("Ajuste", 200, 655, paint_texto);


        canvas.drawText(admin.formatoSalida2(String.valueOf(tot)), 157, 631, paint_num); //TOTAL ACUEDUCTO
        canvas.drawText(admin.formatoSalida2(String.valueOf(tot2)), 157, 643, paint_num); //TOTAL ALCANTARILLADO
        canvas.drawText((financiacion), 167, 655, paint_num);  //TOTAL ASEO
        canvas.drawText(admin.formatoSalida2(String.valueOf(deudaAnterior)), 367, 631, paint_num); //TOTAL DEUDA ANTERIOR
        canvas.drawText(acueductoMora, 367, 643, paint_num); //TOTAL Interes
        canvas.drawText(String.valueOf(aux), 367, 655, paint_num);//TOTAL AJUSTE


//-------------------------------------------------------------------------------------------------------------------------------------------

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);


        canvas.drawText("TOTAL ACUEDUTO Y", 20, 680, paint_texto2);
        canvas.drawText("ALCANTARILLADO", 20, 688, paint_texto2);
        canvas.drawText("CONSUMO MES M3", 200, 686, paint_texto2);


        canvas.drawText(admin.formatoSalida2(String.valueOf(TOTAL)), 150, 686, paint_num);
        canvas.drawText(String.valueOf(total), 367, 686, paint_num);

//-------------------------------------------------------------------------------------------------------------------------------------------


        String fecha_suspension = usuario.get("fecha_suspension").toString();
        if (fecha_suspension.equals("")) {
            fecha_suspension = "";
        }

        canvas.drawText("PAGAR ANTES DE:", 25, 725, paint_total2);
        canvas.drawText("Fecha Suspension:", 25, 760, paint_total2);
        canvas.drawText("TOTAL FACTURA:", 280, 760, paint_final);
        canvas.drawText(fecha(fechaVencimiento), 25, 735, paint_texto);
        canvas.drawText(fecha(fecha_suspension), 25, 770, paint_texto);
        canvas.drawText("Total Aseo: " + admin.formatoSalida2(s), 330, 735, paint_final);

        canvas.drawText(admin.formatoSalida2(String.valueOf(Math.round(TOTAL2))), 356, 760, paint_final);


        //canvas.drawBitmap(scaledbmp,20,815, paint); //IMAGEN
        //ESTE ES canvas.drawBitmap(scaledbmp2,20,815, paint); //IMAGEN
        //canvas.drawText("EMPRESA DE SERVICIOS PÚBLICOS DE PAZ DE RÍO",pagewidth-20,812, paint); //TEXTO PAZ DE RÍO
        canvas.drawText("FACTURA N° ", 48, 824, paint_texto2);
        canvas.drawText(numeroFactura, 270, 824, paint_texto2);
        canvas.drawText("Código Ruta ", 48, 836, paint_texto2);
        canvas.drawText(codigoRuta, 48, 848, paint_texto2);
        canvas.drawText("Periodo Facturación ", 270, 836, paint_texto2);
        canvas.drawText(fecha(fecha) + " al " + fecha(fe1), 270, 848, paint_texto2);
        canvas.drawText("TOTAL ACUEDUCTO/ALCANTARILLADO", 50, 860, paint_texto2);


        canvas.drawText(admin.formatoSalida2(String.valueOf(Math.round(TOTAL))), 270, 860, paint_num);

        paint_color.setColor(Color.rgb(255, 255, 255));
        paint_color.setStrokeWidth(1);


        pdfdocument.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory() + "/Sigiep/Factura.pdf");

        if (file.exists()) {
            file.delete();
            //Toast.makeText(getActivity(), "SE ELIMINÓ EL ARCHIVO", Toast.LENGTH_SHORT).show();
            System.out.println("SE ELIMINÓ EL ARCHIVO");
        }

        try {
            pdfdocument.writeTo(new FileOutputStream(file));
            //Toast.makeText(getActivity(), "SE GENERÓ LA FACTURA", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfdocument.close();
        mostrarPDF();
    }

    public void onBackPressed() {

        MainActivity.reload = 1;
        MainActivity.factur = false;
        if (MainActivity.main) {
            try {
                sector = usuarioActual.getString("sector");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(RegistrarFactura.this, MainActivity.class));
        } else {
            startActivity(new Intent(RegistrarFactura.this, FacturasImpresas.class));
            FacturasImpresas.listaRuta = null;
        }
    }
}
