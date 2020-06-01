package com.sigiep.serviciospublicos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RegistrarLecturaFragment extends Fragment {

    TextView textoTitulo;
    TextView codRuta;

    public RegistrarLecturaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registrar_lectura, container, false);

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            System.out.println("NO HAY DATOS EN EL BUNDDLE");
        }

        String nombre = datosRecuperados.getString("valor_sector");
        textoTitulo = root.findViewById(R.id.textViewTitulo);
        codRuta = root.findViewById(R.id.txt_codigo_ruta);
        textoTitulo.setText(nombre);
        codRuta.setHint(nombre);

        return root;
    }
}
