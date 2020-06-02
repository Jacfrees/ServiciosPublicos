package com.sigiep.serviciospublicos;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sigiep.serviciospublicos.controllers.MainController;
import com.sigiep.serviciospublicos.models.LecturaEntity;

import java.util.ArrayList;
import java.util.Iterator;

public class RegistrarLecturaFragment extends Fragment {

    TextView textoTitulo;
    TextView txtcodRuta;
    TextView txtdireccion;
    TextView txtnombre;
    TextView txtmedidor;
    TextView txtlectura;
    Button btnAtras;
    Button btnNext;

    MainController admin;

    int index = 0;
    ArrayList<LecturaEntity> obj;

    public RegistrarLecturaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registrar_lectura, container, false);

        admin = new MainController(getActivity(), "Servicios_publicos", null, 1);

        textoTitulo = root.findViewById(R.id.textViewTitulo);
        txtcodRuta = root.findViewById(R.id.txt_codigo_ruta);
        txtdireccion = root.findViewById(R.id.txt_direccion);
        txtnombre = root.findViewById(R.id.txt_nombre);
        txtmedidor = root.findViewById(R.id.txt_medidor);
        btnAtras = root.findViewById(R.id.button_back);
        btnNext = root.findViewById(R.id.button_next);


        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            System.out.println("NO HAY DATOS EN EL BUNDDLE");
        }

        String sector = datosRecuperados.getString("valor_sector");
        String ruta = datosRecuperados.getString("valor_codRuta");

        textoTitulo.setText(sector);

        if (ruta == null){
            Toast.makeText(getActivity(), "NO HAY NADA", Toast.LENGTH_SHORT).show();
        }else{
            obj = datosruta(sector);
            createVista(obj);
        }

        btnAtras.setOnClickListener(new View.OnClickListener() { //BUTTON ATRÁS
            @Override
            public void onClick(View v) {
                index = --index < 0 ? obj.size() - 1 : index;

                for (int i = 0; i <obj.size() ; i++) {
                    if(index == i){
                        txtcodRuta.setHint(obj.get(i).getCodigo_ruta());
                        txtdireccion.setHint(obj.get(i).getDireccion());
                        txtnombre.setHint(obj.get(i).getUsuario());
                        txtmedidor.setHint(obj.get(i).getNumero_medidor());
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() { //BUTTON SIGUIENTE
            @Override
            public void onClick(View v) {
                index = ++index > obj.size() - 1 ? 0 : index;

                for (int i = 0; i <obj.size() ; i++) {
                    if(index == i){
                        txtcodRuta.setHint(obj.get(i).getCodigo_ruta());
                        txtdireccion.setHint(obj.get(i).getDireccion());
                        txtnombre.setHint(obj.get(i).getUsuario());
                        txtmedidor.setHint(obj.get(i).getNumero_medidor());
                    }
                }
            }
        });
        return root;
    }

    private ArrayList<LecturaEntity> datosruta(String paramSector) {
        SQLiteDatabase db = admin.getReadableDatabase();

        ArrayList<LecturaEntity> lecturaList = new ArrayList<LecturaEntity>();

        Cursor cursor = db.query("lectura", new String[] {"codigo_ruta", "direccion", "usuario", "numero_medidor"},
                "sector = '"+paramSector+"' ",null, null,null,null);

        while(cursor.moveToNext()) {
            LecturaEntity objlectura = new LecturaEntity();
            objlectura.setCodigo_ruta(cursor.getString(0));
            objlectura.setDireccion(cursor.getString(1));
            objlectura.setUsuario(cursor.getString(2));
            objlectura.setNumero_medidor(cursor.getString(3));
            lecturaList.add(objlectura);
        }

        cursor.close();

        return lecturaList;
    }

    private void createVista(ArrayList<LecturaEntity> obj){
        Bundle datosRecuperados = getArguments();
        String ruta = datosRecuperados.getString("valor_codRuta");

        for (int i = 0; i <obj.size() ; i++) {
            if(ruta.equals(obj.get(i).getCodigo_ruta())){
                index = i;
                txtcodRuta.setHint(obj.get(i).getCodigo_ruta());
                txtdireccion.setHint(obj.get(i).getDireccion());
                txtnombre.setHint(obj.get(i).getUsuario());
                txtmedidor.setHint(obj.get(i).getNumero_medidor());
            }
        }
    }
}
