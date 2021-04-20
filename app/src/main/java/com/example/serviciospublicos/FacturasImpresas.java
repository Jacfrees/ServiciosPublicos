package com.example.serviciospublicos;

import android.annotation.SuppressLint;
import android.content.ClipData;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
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

public class FacturasImpresas extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

    static public String identificador = "";
    static public String rut = "";
    static public String cantidad;
    String val_sector = null;
    ListView listView;
    ClipData.Item item;
    CustomArrayAdapter<ItemLista> adaptador;
    EditText codRuta;
    Maincontroller admin;
    Spinner spinner_sector;
    ArrayList<ClSector> ListSector;
    ArrayList<String> Sector;
    public static ArrayList<ItemLista> listaRuta = new ArrayList<>();
    ArrayList<String> ListRuta;
    ArrayList<String> Ruta;
    private JSONObject sector;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas_impresas);
            MainActivity.main = false;
            admin = new Maincontroller(FacturasImpresas.this, "Servicios_publicos", null, 1);
            ArrayList<String> listaSector = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(this);
            codRuta = (EditText) findViewById(R.id.edtRuta2);
            spinner_sector = (Spinner) findViewById(R.id.spinner_sector2);
            //spinner_ruta = (Spinner) findViewById(R.id.spinner_ruta);
            listView = (ListView) findViewById(R.id.list2);
           
            usuarioF= SettingsHelper.verNombre(getApplicationContext());
            asignarSectorDesdeDB();
           MainActivity.factur = true;
            MainActivity.facturado="1";
            codRuta.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (adaptador!=null) {
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
            ListSector = new ArrayList<ClSector>();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT DISTINCT sector FROM lectura", null);
            while (cursor.moveToNext()) {
                sectorEntity = new ClSector();
                sectorEntity.setSector(cursor.getString(0));
                Log.i("sector", sectorEntity.getSector().toString());
                ListSector.add(sectorEntity);
            }
            obtenerLista();
            ArrayAdapter<CharSequence> adaptador = new ArrayAdapter
                    (FacturasImpresas.this, R.layout.custom_spinner, Sector);
            spinner_sector.setAdapter(adaptador);
            spinner_sector.setOnItemSelectedListener(this);
        }


        private void obtenerLista() {
            Sector = new ArrayList<String>();
            for (int i = 0; i < ListSector.size(); i++) {
                Sector.add(ListSector.get(i).getSector());
            }
        }

        public static String SelectedSector;


        @RequiresApi(api = Build.VERSION_CODES.M)
        public void asignarRutaDesdeDB() {
            listaRuta = new ArrayList<>();
            SQLiteDatabase db = admin.getReadableDatabase();

            SelectedSector = spinner_sector.getSelectedItem().toString();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT identificador,codigo_ruta,direccion,codigo_interno,usuario,numero_medidor,orden FROM lectura WHERE sector = ? and lectura_actual is not null and aforador = ? order by orden asc", new String[]{SelectedSector,usuarioF});

            while (cursor.moveToNext()) {
                String identifica = cursor.getString(0);
                String codRuta = cursor.getString(1);
                String direccion = cursor.getString(2);
                String codigo_interno = cursor.getString(3);
                String usuario = cursor.getString(4);
                String numero_medidor = cursor.getString(5);
                int orden = cursor.getInt(6);
                direccion = direccion.replace(" ", "");
                listaRuta.add(new ItemLista(codRuta + "  -  " + direccion + "  -  " + codigo_interno + "  -  " + identifica + "  -  " + usuario + "  -  " + numero_medidor, orden));
            }
            //  obtenerRuta();

            adaptador = new CustomArrayAdapter(FacturasImpresas.this, android.R.layout.simple_list_item_1, listaRuta);
            listView.setAdapter(adaptador);
            listView.setOnItemClickListener(this);

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

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                asignarRutaDesdeDB();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String p = listaRuta.get(position).texto;

                Bundle datosAEnviar = new Bundle();

                val_sector = spinner_sector.getSelectedItem().toString();
                MainActivity.SelectedSector = spinner_sector.getSelectedItem().toString();


                datosAEnviar.putString("valor_sector", val_sector);
                datosAEnviar.putString("item", parent.getAdapter().getItem(position).toString());
                datosAEnviar.putString("activity",this.getClass().toString());

                //ENVÍA DATOS CLAVE-VALOR A RegistrarLecturaFragment
                Intent intent = new Intent(FacturasImpresas.this, RegistrarFactura.class);
                intent.putExtras(datosAEnviar);
                startActivity(intent);

        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }

        @Override
        public boolean onCreateOptionsMenu(Menu mimenu) {
            getMenuInflater().inflate(R.menu.menu, mimenu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem opcionMenu) {
            int id = opcionMenu.getItemId();
            if (id == R.id.salir) {
                Login.Estado(FacturasImpresas.this, false);
                startActivity(new Intent(FacturasImpresas.this, Login.class));
                finish();
                return true;
            } else if (id == R.id.confImpresora) {
                startActivity(new Intent(FacturasImpresas.this, ConnectionScreen.class));
                return true;
            }else if (id == R.id.retomar){
                String ruta = SettingsHelper.verRutaActual(getApplicationContext());
                String registro = SettingsHelper.verRegistroActual(getApplicationContext());
                if(!registro.isEmpty()){
                    MainActivity.SelectedSector=ruta;
                   MainActivity.factur=false;
                    MainActivity.redirecionarRegistrarFactura(registro,this);
                    finish();

                }
                return  true;
            }
            return super.onOptionsItemSelected(opcionMenu);
        }


    @Override
    public void onBackPressed() {
        MainActivity.factur=false;
        startActivity(new Intent(FacturasImpresas.this, MainActivity.class));
    }
    }