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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.sigiep.serviciospublicos.controllers.MainController;
import com.sigiep.serviciospublicos.models.LecturaEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegistrarLecturaFragment extends Fragment {

    TextView textoTitulo;
    TextView txtcodRuta;
    TextView txtdireccion;
    TextView txtnombre;
    TextView txtmedidor;
    TextView txtlectura;
    Button btnAtras;
    Button btnNext;
    Button btnlectura;
    CheckBox checkDañado;
    CheckBox checkCasaVacia;

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
        txtlectura = root.findViewById(R.id.txt_lectura);
        btnAtras = root.findViewById(R.id.button_back);
        btnNext = root.findViewById(R.id.button_next);
        btnlectura = root.findViewById(R.id.btn_guardar_lectura);
        checkDañado = root.findViewById(R.id.checkBox_dañado);
        checkCasaVacia = root.findViewById(R.id.checkBox_casa_vacia);

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
                        txtlectura.setText(obj.get(i).getLectura_actual());
                        if (obj.get(i).getEstado_medidor().equals("1")){
                            checkDañado.setChecked(true);
                        }else {
                            checkDañado.setChecked(false);
                        }
                        if (obj.get(i).getCasa_vacia().equals("1")){
                            checkCasaVacia.setChecked(true);
                        }else{
                            checkCasaVacia.setChecked(false);
                        }
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
                        txtlectura.setText(obj.get(i).getLectura_actual());
                        if (obj.get(i).getEstado_medidor().equals("1")){
                            checkDañado.setChecked(true);
                        }else {
                            checkDañado.setChecked(false);
                        }
                        if (obj.get(i).getCasa_vacia().equals("1")){
                            checkCasaVacia.setChecked(true);
                        }else{
                            checkCasaVacia.setChecked(false);
                        }
                    }
                }
            }
        });

        btnlectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtlecturaActual = txtlectura.getText().toString();
                String ruta = txtcodRuta.getHint().toString();

                if(checkDañado.isChecked()) {
                    admin.checkDanado("1", ruta);
                    Toast.makeText(getActivity(), "CHECK A  ", Toast.LENGTH_SHORT).show();
                }else{
                    admin.checkDanado("2", ruta);
                }
                 if (checkCasaVacia.isChecked()){
                     admin.checkCasaVacia("1", ruta);
                    Toast.makeText(getActivity(), "CHECK B  ", Toast.LENGTH_SHORT).show();
                }else{
                     admin.checkCasaVacia("2", ruta);
                 }

                 List<LecturaEntity> obj = admin.findAllByCodRuta(ruta);
                 int lecturaActual =  Integer.valueOf(txtlecturaActual);
                 int lecturaAnterior =  Integer.valueOf(obj.get(0).getLectura_anterior());

                 int consumoMes6 =  Integer.valueOf(obj.get(0).getConsumo_mes_6());
                 int consumoMes5 =  Integer.valueOf(obj.get(0).getConsumo_mes_5());
                 int consumoMes4 =  Integer.valueOf(obj.get(0).getConsumo_mes_4());
                 int consumoMes3 =  Integer.valueOf(obj.get(0).getConsumo_mes_3());
                 int consumoMes2 =  Integer.valueOf(obj.get(0).getConsumo_mes_2());
                 int consumoMes1 =  Integer.valueOf(obj.get(0).getConsumo_mes_1());
                 int promedio =  Integer.valueOf(obj.get(0).getPromedio());
                 int estadoMedidor =  Integer.valueOf(obj.get(0).getEstado_medidor());
                 int consumoBasico =  Integer.valueOf(obj.get(0).getConsumo_basico());
                 int casaVacia =  Integer.valueOf(obj.get(0).getCasa_vacia());

                 //#######################################################################
                //VARIABLES FINALES
                int lectura = 0; //DATO A GUARDAR EN EL CAMPO lectura
                int acueductoConsumo = 0; //DATO A GUARDAR EN EL CAMPO acueductoConsumo

                if((lecturaActual < 0  || estadoMedidor == 1) &&
                        (consumoMes6 > 0 &&
                         consumoMes5 > 0 &&
                         consumoMes4 > 0 &&
                         consumoMes3 > 0 &&
                         consumoMes2 > 0 &&
                         consumoMes1 > 0)){

                    lectura = promedio;
                    System.out.println("LA LECTURA ES promedio "+ lectura);
                }else if(lecturaActual > 0 && estadoMedidor == 2) {
                    lectura = lecturaActual - lecturaAnterior;
                    System.out.println("LA LECTURA ES lecturaActual - lecturaAnterior "+ lectura);
                }else {
                    lectura = consumoBasico;
                    System.out.println("LA LECTURA ES consumoBasico "+ lectura);
                }

                if(casaVacia == 1){
                    acueductoConsumo = 0;
                    System.out.println("VALOR acueductoConsumo, casaVacia == 2 "+ acueductoConsumo);
                }else {
                    acueductoConsumo = lectura * 988;
                    System.out.println("VALOR acueductoConsumo casaVacia == 1 "+ acueductoConsumo);
                }

                int acueductoSubsidio = (int) ((3854*0.4)+(acueductoConsumo*0.4)); //DATO A GUARDAR EN EL CAMPO acueductoSubsidio
                System.out.println("VALOR acueductoSubsidio  "+ acueductoSubsidio);

                int alcantarilladoConsumo = lectura * 281; //DATO A GUARDAR EN EL CAMPO alcantarilladoConsumo
                System.out.println("VALOR alcantarilladoConsumo  "+ alcantarilladoConsumo);

                int alcantarilladoSudsidio = (int) ((2222*0.4)+(alcantarilladoConsumo*0.4)); //DATO A GUARDAR EN EL CAMPO alcantarilladoSudsidio
                System.out.println("VALOR alcantarilladoSudsidio  "+ alcantarilladoSudsidio);

                admin.guardarLectura(txtlecturaActual,
                        String.valueOf(lectura),
                        String.valueOf(acueductoConsumo),
                        String.valueOf(acueductoSubsidio),
                        String.valueOf(alcantarilladoConsumo),
                        String.valueOf(alcantarilladoSudsidio),
                        ruta);

                Toast.makeText(getActivity(), "LA LECTURA ES " + lectura, Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    private ArrayList<LecturaEntity> datosruta(String paramSector) {
        SQLiteDatabase db = admin.getReadableDatabase();

        ArrayList<LecturaEntity> lecturaList = new ArrayList<LecturaEntity>();

        Cursor cursor = db.query("lectura", new String[] {"codigo_ruta", "direccion", "usuario", "numero_medidor", "lectura_actual","estado_medidor","casa_vacia"},
                "sector = '"+paramSector+"' ",null, null,null,null);

        while(cursor.moveToNext()) {
            LecturaEntity objlectura = new LecturaEntity();
            objlectura.setCodigo_ruta(cursor.getString(0));
            objlectura.setDireccion(cursor.getString(1));
            objlectura.setUsuario(cursor.getString(2));
            objlectura.setNumero_medidor(cursor.getString(3));
            objlectura.setLectura_actual(cursor.getString(4));
            objlectura.setEstado_medidor(cursor.getString(5));
            objlectura.setCasa_vacia(cursor.getString(6));
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
                txtlectura.setText(obj.get(i).getLectura_actual());
                if (obj.get(i).getEstado_medidor().equals("1")){
                    checkDañado.setChecked(true);
                }
                if (obj.get(i).getCasa_vacia().equals("1")){
                    checkCasaVacia.setChecked(true);
                }
                /*if(obj.get(i).getLectura_actual() != "" || obj.get(i).getLectura_actual() != null){
                    btnlectura.setClickable(false);
                }else {
                    btnlectura.setClickable(false);
                }*/
            }
        }
    }
}
