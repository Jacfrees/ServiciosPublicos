package com.example.serviciospublicos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.serviciospublicos.Controlador.Maincontroller;
import com.example.serviciospublicos.Controlador.TimerEnvio;
import com.example.serviciospublicos.util.SettingsHelper;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {
    Maincontroller objuser = new Maincontroller(this, "Servicios_publicos", null, 1);

    RadioButton rdSesion;
    private boolean activar;

    String   usuari, contrasen, fechaactualizacion, observaciones;
    Integer id_unico, rol, tercero, estado;
    //public final static String urlServidor = "http://181.53.62.172/ServiciosPublicos/";//equipo local
    //public final static String urlServidor = "https://serviciospublico.000webhostapp.com/ServiciosPublicos/";
    public final static String urlServidor = "http://31.220.49.57:8080/ServiciosPublicos/"; // vps
    EditText usuario,clave;
    Button btnIngresar,btnSincronizar;
    CheckBox ver;
    public static String usuarioF;

    String URL_SERVIDOR = urlServidor + "loguin.php";

    private static final String STRING_PREFERENCES = "mispreferencias";
    private static final String PREFERENCE_ESTADO_BUTTON_SESION = "estado.button.sesion";
    int a,e;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();


    usuario = (EditText)findViewById(R.id.txtUsuario);
    clave = (EditText)findViewById(R.id.txtClave);
    btnIngresar = (Button)findViewById(R.id.btnlogin);

    ver = (CheckBox) findViewById(R.id.checkBox);

    /*usuario.setText("JJMIRANDA");
    clave.setText("1052384350");*/

    rdSesion = (RadioButton)findViewById(R.id.rdSesion);
    activar = rdSesion.isChecked();

        TimerEnvio timerEnvio = TimerEnvio.initInstance(getApplicationContext(),300000,150000);
        timerEnvio.start();
    pedirPermisos();

    if(verEstado()){
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    rdSesion.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        if (activar){
            rdSesion.setChecked(false);
        }
            activar = rdSesion.isChecked();
        }
    });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                validarUsuario();
            }
        });

    ver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    clave.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ver.setBackgroundResource(R.drawable.ver);
                } else {
                    // hide password
                    clave.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ver.setBackgroundResource(R.drawable.nover);
                }
            }
        });

    }

    public static void Estado(Context c, boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION,b).apply();
    }

    public void guardarEstado() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION,rdSesion.isChecked()).apply();

    }

    public boolean verEstado(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON_SESION,false);

    }



    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Login.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);

        }
    }

    public void validarUsuario (){
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, URL_SERVIDOR,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // En este apartado se programa lo que deseamos hacer en caso de no haber errores

                        if(response.equals("\r\nERROR 1")) {
                            Toast.makeText(Login.this, "Se deben de llenar todos los campos.", Toast.LENGTH_SHORT).show();
                        } else if(response.equals("\r\nERROR 2")) {
                            Toast.makeText(Login.this, "No existe ese registro.", Toast.LENGTH_SHORT).show();
                        } else {
                            SettingsHelper.saveName(getApplicationContext(), String.valueOf(usuario.getText()));

                            guardarEstado();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  seccion offline del inicio de seccion
                Toast.makeText(Login.this, "SIN CONEXION A INTERNET", Toast.LENGTH_LONG).show();

                try {
                    Cursor cursor = objuser.validarLogin(usuario.getText().toString(),clave.getText().toString());
                    if(cursor.getCount()>0){
                        e=0;

                        guardarEstado();
                        startActivity(new Intent(Login.this, MainActivity.class)); //REDIRIGE AL HOME
                    }else{
                        Toast.makeText(Login.this, "USUARIO Y/O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                    }
                    usuario.setText("");
                    clave.findFocus();

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("usuario", usuario.getText().toString().trim());
                parametros.put("contrasen", clave.getText().toString().trim());

                return parametros;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);

    }




    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("¿Salir?")
                .setMessage("¿Realmente desea salir?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Login.super.onBackPressed();

                    }
                }).create().show();
    }

}