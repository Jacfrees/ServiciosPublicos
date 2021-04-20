package com.example.serviciospublicos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.serviciospublicos.Adapter.CustomArrayAdapter;
import com.example.serviciospublicos.Controlador.Maincontroller;
import com.example.serviciospublicos.modelo.ClSector;
import com.example.serviciospublicos.modelo.ItemLista;

import com.example.serviciospublicos.util.SettingsHelper;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.serviciospublicos.Login.urlServidor;
import static com.example.serviciospublicos.Login.usuarioF;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    static public String cantidad;
    static public String facturado = "0";
    static public int reload = 0;

    String val_codRuta = null;
    String identi = null;
    ListView listView;
    CustomArrayAdapter<ItemLista> adaptador;
    String urlCargarSector = urlServidor + "SpinnerSector.php";
    static public Integer offline2 = 0;
    static public Integer N;
    static public boolean main = true;


    private EditText codRuta;
    private Maincontroller admin;
    private Spinner spinner_sector;
    private ArrayList<String> listaSector = new ArrayList<>();
    private ArrayAdapter<String> SectorAdapter;
    private ArrayAdapter<String> RutaAdapter;
    private ArrayList<String> Sector = new ArrayList<>();
    public static ArrayList<ItemLista> listaRuta = new ArrayList<>();
    public static boolean factur = false;
    private ArrayList<String> Ruta;
    private JSONObject sector;
    private RequestQueue requestQueue;
    public static String SelectedSector;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        admin = new Maincontroller(MainActivity.this, "Servicios_publicos", null, 1);
        ArrayList<String> listaSector = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        codRuta = (EditText) findViewById(R.id.edtRuta);
        spinner_sector = (Spinner) findViewById(R.id.spinner_sector);
        usuarioF = SettingsHelper.verNombre(getApplicationContext());
        listView = (ListView) findViewById(R.id.list);
        asignarSectorDesdeDB();

        codRuta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adaptador != null) {
                    adaptador.getFilter().filter(s);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void asignarSectorDesdeDB() {

        SQLiteDatabase db = admin.getReadableDatabase();
        ClSector sectorEntity = null;
        Cursor cursor = db.rawQuery("SELECT DISTINCT sector FROM lectura", null);
        while (cursor.moveToNext()) {
            Sector.add(cursor.getString(0));
        }
        if (Sector != null && !Sector.isEmpty()) {
            ArrayAdapter<CharSequence> adaptador = new ArrayAdapter
                    (MainActivity.this, R.layout.custom_spinner, Sector);
            spinner_sector.setAdapter(adaptador);
            spinner_sector.setOnItemSelectedListener(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void asignarRutaDesdeDB(String SelectedSector, final String registro) {
        listaRuta = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();
        SelectedSector = spinner_sector.getSelectedItem().toString();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT identificador,codigo_ruta,direccion,codigo_interno,usuario,numero_medidor,orden,lectura_actual FROM lectura WHERE sector = ? and aforador = ?  and lectura_actual is null order by orden asc", new String[]{SelectedSector, usuarioF});
        while (cursor.moveToNext()) {
            String identifica = cursor.getString(0);
            String codRuta = cursor.getString(1);
            String direccion = cursor.getString(2);
            String codigo_interno = cursor.getString(3);
            String usuario = cursor.getString(4);
            String numero_medidor = cursor.getString(5);
            int orden = cursor.getInt(6);
            String lectura = cursor.getString(7);
            direccion = direccion.replace(" ", "");
            listaRuta.add(new ItemLista(codRuta + "  -  " + direccion + "  -  " + codigo_interno + "  -  " + identifica + "  -  " + usuario + "  -  " + numero_medidor, orden));
        }
        adaptador = new CustomArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listaRuta);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);

        if (registro != null) {
            int position = listaRuta.indexOf(registro);
            if (position >= 0) {
                redirecionarRegistrarFactura(registro, MainActivity.this);
            } else {
                Toast.makeText(MainActivity.this, "No se puede cargar registro", Toast.LENGTH_SHORT).show();
            }

        }

    }


    /*
        private void obtenerRuta() {
            Ruta = new ArrayList<String>();
            Ruta.add("Seleccione");
            for (int i = 0; i < ListRuta.size(); i++) {
                Ruta.add(ListRuta.get(i).getCodigo_ruta() + " - " + ListRuta.get(i).getDireccion() + " - " + ListRuta.get(i).getIdentificador());
            }
        }
    */
    public static void redirecionarRegistrarFactura(String item, Activity activity) {
        Bundle datosAEnviar = new Bundle();
        datosAEnviar.putString("valor_sector", SelectedSector);
        datosAEnviar.putString("item", item);
        datosAEnviar.putString("activity", activity.getClass().toString());

        //ENVÍA DATOS CLAVE-VALOR A RegistrarLecturaFragment
        Intent intent = new Intent(activity, RegistrarFactura.class);
        intent.putExtras(datosAEnviar);
        activity.startActivity(intent);
    }

    public void seleccionarRuta(int position, AdapterView<?> parent) {
        ItemLista item = listaRuta.get(position);
        String p = item.texto;
        String val_sector = spinner_sector.getSelectedItem().toString();
        SelectedSector = spinner_sector.getSelectedItem().toString();
        redirecionarRegistrarFactura(parent.getAdapter().getItem(position).toString(), this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        asignarRutaDesdeDB(SelectedSector, null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Seleccionar Registro")
                .setMessage("Se retomara el recorrido desde el registro seleccionado")
                .setPositiveButton(R.string.continuarUltimoRegistro, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ruta = SettingsHelper.verRutaActual(getApplicationContext());
                        String registro = SettingsHelper.verRegistroActual(getApplicationContext());
                        if (registro.isEmpty()) {
                            seleccionarRuta(position, parent);
                        } else {
                            irUltimoRegistroVisualizado(ruta, registro);
                        }
                    }
                })
                .setNegativeButton(R.string.continuarRegistroSeleccionado, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        seleccionarRuta(position, parent);

                    }
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void irUltimoRegistroVisualizado(String ruta, String registro) {

        SelectedSector = spinner_sector.getSelectedItem().toString();
        if (SelectedSector.equals(ruta)) {
            redirecionarRegistrarFactura(registro, this);
        } else {
            SelectedSector = ruta;
            asignarRutaDesdeDB(ruta, registro);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem opcionMenu) {
        int id = opcionMenu.getItemId();
        if (id == R.id.salir) {
            Login.Estado(MainActivity.this, false);
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return true;
        } else if (id == R.id.confImpresora) {
            startActivity(new Intent(MainActivity.this, ConnectionScreen.class));
            return true;
        } else if (id == R.id.facturasImpresas) {
            startActivity(new Intent(MainActivity.this, FacturasImpresas.class));
            return true;
        } else if (id == R.id.sincronizar) {
            startActivity(new Intent(getApplicationContext(), Sincronizar.class));
            finish();
            return true;
        } else if (id == R.id.retomar) {
            String ruta = SettingsHelper.verRutaActual(getApplicationContext());
            String registro = SettingsHelper.verRegistroActual(getApplicationContext());
            irUltimoRegistroVisualizado(ruta, registro);
            return true;
        }
        return super.onOptionsItemSelected(opcionMenu);
    }

    @Override
    public void onBackPressed() {
        MainActivity.super.onBackPressed();
        recreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onResume() {
        super.onResume();
        if (reload == 1) {
            asignarRutaDesdeDB(RegistrarFactura.sector, null);
            main = true;
            reload = 0;
        }
    }


}


