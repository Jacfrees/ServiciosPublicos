package com.sigiep.serviciospublicos.ui.archivo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sigiep.serviciospublicos.MainActivity;
import com.sigiep.serviciospublicos.R;
import com.sigiep.serviciospublicos.controllers.MainController;
import com.sigiep.serviciospublicos.models.LecturaEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ArchivoFragment extends Fragment {

    private ArchivoViewModel archivoViewModel;
    Button btnImportar;
    Button btnExportar;

    private String archivo = "miarchivo";
    private String carpeta = "/Sigiep/";
    String contenido;
    File file;
    String file_path = "";
    String name = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        archivoViewModel =
                ViewModelProviders.of(this).get(ArchivoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_archivo, container, false);

        archivoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        btnImportar = root.findViewById(R.id.btn_importar_archivo); //ENLAZA BOTÓN CON FRAGMENT

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                importarArchivo();
            }
        });
        btnExportar = root.findViewById(R.id.btn_exportar_archivo); //ENLAZA BOTÓN CON FRAGMENT

        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportarArchivo();
            }
        });
        return root;
    }

    List<LecturaEntity> listaLectura = new ArrayList<>();

    private void importarArchivo(){

        //RUTA DONDE ESTÁ EL ARCHIVO A LEER
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Sigiep");
        String archivo = carpeta.toString() + "/" + "Lectura.csv";

        File archivo_1 = new File(Environment.getExternalStorageDirectory() + "/Sigiep/Lectura.csv");

        boolean isCreate = false;
        if (!carpeta.exists()){
            isCreate =  carpeta.mkdir();
            Toast.makeText(getActivity(), "POR FAVOR VUELVE A INTERTAR", Toast.LENGTH_SHORT).show();
        }else {
            if(archivo_1.exists()){

                String cadena;
                String[] arreglo;

                try{

                    FileReader fileReader = new FileReader(archivo);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    MainController admin = new MainController(getActivity(), "Servicios_publicos", null, 1);
                    admin.borrarRegistros("lectura");
                    long numLine = 0;

                    while ((cadena = bufferedReader.readLine()) != null){
                        if(numLine > 0) {
                            cadena += ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL" + ";NULL";

                            arreglo = cadena.split(";");
                            //System.out.println("ARRAY  "+Arrays.toString(arreglo));

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
                            registro.put("numero_medidor", arreglo[12]);
                            registro.put("consumo_mes_6", arreglo[13]);
                            registro.put("consumo_mes_5", arreglo[14]);
                            registro.put("consumo_mes_4", arreglo[15]);
                            registro.put("consumo_mes_3", arreglo[16]);
                            registro.put("consumo_mes_2", arreglo[17]);
                            registro.put("consumo_mes_1", arreglo[18]);
                            registro.put("promedio", arreglo[19]);
                            registro.put("consumo_basico", arreglo[20]);
                            registro.put("mtrs_max_subsidio", arreglo[21]);
                            registro.put("deuda_anterior", arreglo[22]);
                            registro.put("atraso", arreglo[23]);
                            registro.put("estado_medidor", arreglo[24]);
                            registro.put("casa_vacia", arreglo[25]);
                            registro.put("lectura_anterior", arreglo[26]);
                            registro.put("lectura_actual", arreglo[27]);
                            registro.put("lectura", arreglo[28]);
                            registro.put("valor_mtr3_acueducto", arreglo[29]);
                            registro.put("cargo_fijo_acueducto", arreglo[30]);
                            registro.put("consumo_acueducto", arreglo[31]);
                            registro.put("contribucion_acueducto", arreglo[32]);
                            registro.put("intereses_mora_de_acueducto", arreglo[33]);
                            registro.put("subsidio_acueducto", arreglo[34]);
                            registro.put("acueducto_concepto1", arreglo[35]);
                            registro.put("acueducto_concepto2", arreglo[36]);
                            registro.put("acueducto_concepto3", arreglo[37]);
                            registro.put("valor_mtr3_alcantarillado", arreglo[38]);
                            registro.put("cargo_fijo_alcantarillado", arreglo[39]);
                            registro.put("consumo_alcantarillado", arreglo[40]);
                            registro.put("contribucion_alcantarillado", arreglo[41]);
                            registro.put("intereses_mora_de_alcantarillado", arreglo[42]);
                            registro.put("subsidio_alcantarillado", arreglo[43]);
                            registro.put("alcantarillado_concepto1", arreglo[44]);
                            registro.put("alcantarillado_concepto2", arreglo[45]);
                            registro.put("alcantarillado_concepto3", arreglo[46]);
                            registro.put("valor_mtr3_aseo", arreglo[47]);
                            registro.put("cargo_fijo_aseo", arreglo[48]);
                            registro.put("subsidio_aseo", arreglo[49]);
                            registro.put("intereses_de_mora_aseo", arreglo[50]);
                            registro.put("contribucion_aseo", arreglo[51]);
                            registro.put("aseo_concepto1", arreglo[52]);
                            registro.put("aseo_concepto2", arreglo[53]);
                            registro.put("aseo_concepto3", arreglo[54]);

                            registro.put("matricula", arreglo[55]);
                            registro.put("medidor", arreglo[56]);
                            registro.put("llave_o_tapas", arreglo[57]);
                            registro.put("financiacion", arreglo[58]);
                            registro.put("reconexion", arreglo[59]);
                            registro.put("fecha_lectura", arreglo[60]);
                            registro.put("aforador", arreglo[61]);
                            registro.put("servicio_acueducto", arreglo[62]);
                            registro.put("servicio_alcantarillado", arreglo[63]);
                            registro.put("servicio_aseo", arreglo[64]);
                            registro.put("porcentaje_subsidio_acueducto", arreglo[65]);
                            registro.put("porcentaje_subsidio_alcantarillado", arreglo[66]);


                            db.insert("lectura", null, registro);
                            db.close();
                        }
                        numLine++;
                    }

                    archivo_1.delete();
                    Toast.makeText(getActivity(), "SE IMPORTÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Toast.makeText(getActivity(), "NO SE IMPORTÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "SUBE UN ARCHIVO POR FAVOR", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exportarArchivo() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Sigiep");
        String archivo = carpeta.toString() + "/" + "LecturaExportada.csv";

        boolean isCreate = false;
        if(!carpeta.exists()) {
            isCreate = carpeta.mkdir(); //SI NO EXISTE LA CARPETA LA CREA
        }

        try {
            FileWriter fileWriter = new FileWriter(archivo);

            MainController admin = new MainController(getActivity(), "Servicios_publicos", null, 1);

            SQLiteDatabase db = admin.getWritableDatabase();

            Cursor fila = db.rawQuery("select * from lectura", null);

            if(fila != null && fila.getCount() != 0) {
                fila.moveToFirst();
                do {
                    fileWriter.append(fila.getString(0));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(1));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(2));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(3));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(4));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(5));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(6));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(7));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(8));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(9));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(10));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(11));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(12));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(13));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(14));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(15));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(16));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(17));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(18));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(19));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(20));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(21));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(22));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(23));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(24));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(25));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(26));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(27));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(28));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(29));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(30));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(31));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(32));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(33));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(34));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(35));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(36));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(37));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(38));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(39));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(40));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(41));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(42));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(43));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(44));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(45));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(46));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(47));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(48));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(49));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(50));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(51));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(52));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(53));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(54));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(55));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(56));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(57));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(58));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(59));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(60));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(61));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(62));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(63));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(64));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(65));
                    fileWriter.append(";");
                    fileWriter.append(fila.getString(66));
                    fileWriter.append("\n");

                } while(fila.moveToNext());
            } else {
                Toast.makeText(getActivity(), "No hay registros.", Toast.LENGTH_LONG).show();
            }

            db.close();
            fileWriter.close();
            Toast.makeText(getActivity(), "SE CREO EL ARCHIVO CSV EXITOSAMENTE", Toast.LENGTH_LONG).show();

        } catch (Exception e) { }
    }
}
