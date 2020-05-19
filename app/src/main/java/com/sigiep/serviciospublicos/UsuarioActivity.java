package com.sigiep.serviciospublicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sigiep.serviciospublicos.controllers.UsuarioController;

public class UsuarioActivity extends AppCompatActivity {

    EditText txtNombre, txtDocumento, txtUsuario, txtContrasena;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        txtNombre = (EditText) findViewById(R.id.txt_nombre);
        txtDocumento = (EditText) findViewById(R.id.txt_documento);
        txtUsuario = (EditText) findViewById(R.id.txt_usuario);
        txtContrasena = (EditText) findViewById(R.id.txt_contrasena);
        btnAgregar = (Button) findViewById(R.id.btn_agregar);

        final UsuarioController usuarioController = new UsuarioController(this, "Servicios_publicos", null, 1);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNombre.getText().toString().isEmpty() ||
                txtDocumento.getText().toString().isEmpty() ||
                txtUsuario.getText().toString().isEmpty() ||
                txtContrasena.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "ESTÁ VACÍO", Toast.LENGTH_SHORT).show();
                }else {
                    usuarioController.agregar(txtNombre.getText().toString(),
                            txtDocumento.getText().toString(),
                            txtUsuario.getText().toString(),
                            txtContrasena.getText().toString());
                    Toast.makeText(getApplicationContext(), "SE AGREGÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
