package com.sigiep.serviciospublicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sigiep.serviciospublicos.controllers.MainController;

public class MainActivity extends AppCompatActivity {

    MainController objuser = new MainController(this, "Servicios_publicos", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
