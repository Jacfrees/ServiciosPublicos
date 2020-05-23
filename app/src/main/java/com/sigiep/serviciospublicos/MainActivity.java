package com.sigiep.serviciospublicos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sigiep.serviciospublicos.controllers.MainController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    MainController objuser = new MainController(this, "Servicios_publicos", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedirPermisos();

        boolean a = objuser.numeroRegistos("usuario");
        if (a){
            objuser.agregar("Administrador", "12345", "Admin", "Grupo123456");
            Toast.makeText(this, "SE CREÓ EL ADMINISTRADOR", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "YA EXISTE EL ADMINISTRADOR", Toast.LENGTH_SHORT).show();
        }
        boolean b = objuser.numeroRegistos("compania");
        if (b){
            objuser.agregar_compania("MUNICIPIO PAZ DE RIO","891855015","alcaldia@pazderio-boyaca.gov.co",
                    "Carrera 3 No 7-50 Barrio Colonial","7865133", null,null,null,null);
            Toast.makeText(this, "SE CREÓ LA COMPAÑÍA", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "YA EXISTE LA COMPAÑÍA", Toast.LENGTH_SHORT).show();
        }

        Button btn = (Button) findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtusu = (EditText) findViewById(R.id.usuario);
                EditText txtcon = (EditText) findViewById(R.id.contrasena);

                try {
                    Cursor cursor = objuser.validarLogin(txtusu.getText().toString(),txtcon.getText().toString());
                    if(cursor.getCount()>0){
                        startActivity(new Intent(MainActivity.this, HomeActivity.class)); //REDIRIGE AL HOME
                    }else{
                        Toast.makeText(MainActivity.this, "USUARIO Y/O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                    }
                    txtcon.setText("");
                    txtcon.findFocus();

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);

        }
    }

    public void importarArchivo(){

        //limpiarTablas("lectura");

        //RUTA DONDE ESTÁ EL ARCHIVO A LEER
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/ExportarSQLiteCSV");

        String archivo = carpeta.toString() + "/" + "Lectura.csv";

        boolean isCreate = false;
        if (!carpeta.exists()){
            //Toast.makeText(this, "NO EXISTE LA CARPETA", Toast.LENGTH_SHORT).show();
            carpeta.mkdirs();

            System.out.println("NO EXISTE LA CARPETA");

        }else{
            String cadena;
            String[] arreglo;

            try{
                FileReader fileReader = new FileReader(archivo);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while ((cadena = bufferedReader.readLine()) != null){
                    arreglo = cadena.split(";"); //CAMBIAR A ENTITY

                    MainController admin = new MainController(MainActivity.this, "Servicios_publicos", null, 1);
                    SQLiteDatabase db = admin.getWritableDatabase();

                    ContentValues registro = new ContentValues();

                    registro.put("identificador", arreglo[0]);
                    registro.put("fecha", arreglo[1]);
                    registro.put("fecha_vencimiento", arreglo[2]);
                    registro.put("periodo", arreglo[3]);
                    registro.put("numero_factura", arreglo[4]);
                    registro.put("sector", arreglo[5]);
                    registro.put("codigo_ruta", arreglo[6]);
                    registro.put("codigo_interno", arreglo[7]);
                    registro.put("usuario", arreglo[8]);
                    registro.put("direccion", arreglo[9]);
                    registro.put("estrato", arreglo[10]);
                    registro.put("uso", arreglo[11]);
                    registro.put("consumo_mes_6", arreglo[12]);
                    registro.put("consumo_mes_5", arreglo[13]);
                    registro.put("consumo_mes_4", arreglo[14]);
                    registro.put("consumo_mes_3", arreglo[15]);
                    registro.put("consumo_mes_2", arreglo[16]);
                    registro.put("consumo_mes_1", arreglo[17]);
                    registro.put("promedio", arreglo[18]);
                    registro.put("consumo_basico", arreglo[19]);
                    registro.put("mtrs_max_subsidio", arreglo[20]);
                    registro.put("deuda_anterior", arreglo[21]);
                    registro.put("atraso", arreglo[22]);
                    registro.put("estado_medidor", arreglo[23]);
                    registro.put("casa_vacia", arreglo[24]);
                    registro.put("lectura_anterior", arreglo[25]);
                    registro.put("lectura_actual", arreglo[26]);
                    registro.put("lectura", arreglo[27]);
                    registro.put("valor_mtr3_acueducto", arreglo[28]);
                    registro.put("cargo_fijo_acueducto", arreglo[39]);
                    registro.put("consumo_acueducto", arreglo[30]);
                    registro.put("contribucion_acueducto", arreglo[31]);
                    registro.put("intereses_mora_de_acueducto", arreglo[32]);
                    registro.put("subsidio_acueducto", arreglo[33]);
                    registro.put("acueducto_concepto1", arreglo[34]);
                    registro.put("acueducto_concepto2", arreglo[35]);
                    registro.put("acueducto_concepto3", arreglo[36]);
                    registro.put("valor_mtr3_alcantarillado", arreglo[37]);
                    registro.put("cargo_fijo_alcantarillado", arreglo[38]);
                    registro.put("consumo_alcantarillado", arreglo[39]);
                    registro.put("contribucion_alcantarillado", arreglo[40]);
                    registro.put("intereses_mora_de_alcantarillado", arreglo[41]);
                    registro.put("subsidio_alcantarillado", arreglo[42]);
                    registro.put("alcantarillado_concepto1", arreglo[43]);
                    registro.put("alcantarillado_concepto2", arreglo[44]);
                    registro.put("alcantarillado_concepto3", arreglo[45]);
                    registro.put("valor_mtr3_aseo", arreglo[46]);
                    registro.put("cargo_fijo_aseo", arreglo[47]);
                    registro.put("subsidio_aseo", arreglo[48]);
                    registro.put("intereses_de_mora_aseo", arreglo[49]);
                    registro.put("contribucion_aseo", arreglo[50]);
                    registro.put("aseo_concepto1", arreglo[51]);
                    registro.put("aseo_concepto2", arreglo[52]);
                    registro.put("aseo_concepto3", arreglo[53]);

                    /*listaLectura.add(
                            new LecturaEntity(
                                    arreglo[0],
                                    arreglo[1],
                            )
                    ); */ //LLENAR ENTIDAD SI SE NECESITA

                    db.insert("lectura", null, registro);
                    db.close();

                    System.out.println("SE IMPORTÓ CORRECTAMENTE");
                }

            }catch (Exception e){
                System.out.println("NO SE IMPORTÓ CORRECTAMENTE");
            }
        }

    }

}
