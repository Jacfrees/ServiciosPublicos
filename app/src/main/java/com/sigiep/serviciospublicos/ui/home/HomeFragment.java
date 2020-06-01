package com.sigiep.serviciospublicos.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sigiep.serviciospublicos.R;
import com.sigiep.serviciospublicos.RegistrarLecturaFragment;
import com.sigiep.serviciospublicos.controllers.ArchivoController;
import com.sigiep.serviciospublicos.controllers.MainController;
import com.sigiep.serviciospublicos.models.LecturaEntity;
import com.sigiep.serviciospublicos.ui.archivo.ArchivoFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View vista;
    Button btnRegistrar;
    Spinner spinner_sector;
    Spinner spinner_ruta;

    ArrayList<String> listaSector;
    ArrayList<LecturaEntity> lecturaList;

    ArrayList<String> listaRuta;
    MainController admin;
    String val_sector = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        admin = new MainController(getActivity(), "Servicios_publicos", null, 1);


        spinner_sector = (Spinner) root.findViewById(R.id.spinner_sector);
        spinner_ruta = root.findViewById(R.id.spinner_ruta);
        btnRegistrar = root.findViewById(R.id.btn_registrar_lectura); //ENLAZA BOTÓN CON FRAGMENT

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "AAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();

                RegistrarLecturaFragment fragment = new RegistrarLecturaFragment();
                FragmentTransaction ft = null;
                if (getFragmentManager() != null) {

                    Bundle datosAEnviar = new Bundle();

                    datosAEnviar.putString("valor_sector", val_sector);
                    // ¡Importante! darle argumentos
                    fragment.setArguments(datosAEnviar);

                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.child_fragment, fragment, "registrarLecturaFragment");
                    ft.addToBackStack(null);  //opcional para agregarlo a la pila
                    ft.commit();
                }

        }
        });

        listarSector();

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), R.layout.design_spinner_home, listaSector); //CARGA SPINNER SECTOR
        spinner_sector.setAdapter(adaptador);

        spinner_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String option = (String) spinner_sector.getAdapter().getItem(position);
                val_sector = option;
                //Toast.makeText(getActivity(), "Seleccionaste: " + option, Toast.LENGTH_SHORT).show();

                //Toast.makeText(getActivity(), "Seleccionaste: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                listarRuta(option);

                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), R.layout.design_spinner_home, listaRuta); //CARGA SPINNER RUTA
                spinner_ruta.setAdapter(adaptador);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_ruta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    private void listarSector() {
        SQLiteDatabase db = admin.getReadableDatabase();

        LecturaEntity objlectura = null;
        lecturaList = new ArrayList<LecturaEntity>();

        Cursor cursor = db.rawQuery("select distinct sector from lectura ", null);

        while(cursor.moveToNext()){
            objlectura = new LecturaEntity();
            objlectura.setSector(cursor.getString(0));
            lecturaList.add(objlectura);
        }
        obtenerListaSector();
    }
    private void obtenerListaSector(){
        listaSector = new ArrayList<String>();
        for (int i = 0; i <lecturaList.size() ; i++) {
            if (i == 0){
                listaSector.add(i, "Sector");
            }else{
                listaSector.add(lecturaList.get(i).getSector());
            }
        }
    }

    private void listarRuta(String paramSector) {
        SQLiteDatabase db = admin.getReadableDatabase();

        LecturaEntity objlectura = null;
        lecturaList = new ArrayList<LecturaEntity>();

        Cursor cursor = db.query("lectura", new String[] {"codigo_ruta"},
                "sector = '"+paramSector+"' ",null, null,null,null);

        while(cursor.moveToNext()){
            objlectura = new LecturaEntity();
            objlectura.setSector(cursor.getString(0));
            lecturaList.add(objlectura);
        }
        obtenerListaRuta();
    }
    private void obtenerListaRuta(){
        listaRuta = new ArrayList<String>();
        for (int i = 0; i <lecturaList.size() ; i++) {
            listaRuta.add(lecturaList.get(i).getSector());
        }
    }

}
