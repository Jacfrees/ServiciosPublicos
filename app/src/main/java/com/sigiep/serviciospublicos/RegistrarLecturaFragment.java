package com.sigiep.serviciospublicos;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.sigiep.serviciospublicos.controllers.FacturaController;
import com.sigiep.serviciospublicos.controllers.MainController;
import com.sigiep.serviciospublicos.models.CompaniaEntity;
import com.sigiep.serviciospublicos.models.LecturaEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    Bitmap bmp, scaledbmp; //PARA AGREGAR IMÁGENES
    Bitmap bmp2, scaledbmp2; //PARA AGREGAR IMÁGENES

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
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_paz_de_rio);
        scaledbmp = Bitmap.createScaledBitmap(bmp,55,55,false); //TAMAÑO DEL LOGO PAZ DE RÍO

        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pedregosa);
        scaledbmp2 = Bitmap.createScaledBitmap(bmp2,55,55,false); //TAMAÑO DEL LOGO

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            System.out.println("NO HAY DATOS EN EL BUNDDLE");
        }

        String sector = datosRecuperados.getString("valor_sector");
        String ruta = datosRecuperados.getString("valor_codRuta");

        textoTitulo.setText(sector);

        if (ruta == null){
            Toast.makeText(getActivity(), "NO HAY RUTA", Toast.LENGTH_SHORT).show();
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

                if (txtlecturaActual.equals("")) {
                    Toast.makeText(getActivity(), "Por Favor Ingresa Una Lectura", Toast.LENGTH_SHORT).show();
                } else {

                    if (checkDañado.isChecked()) {
                        admin.checkDanado("1", ruta);
                        Toast.makeText(getActivity(), "CHECK A  ", Toast.LENGTH_SHORT).show();
                    } else {
                        admin.checkDanado("2", ruta);
                    }
                    if (checkCasaVacia.isChecked()) {
                        admin.checkCasaVacia("1", ruta);
                        Toast.makeText(getActivity(), "CHECK B  ", Toast.LENGTH_SHORT).show();
                    } else {
                        admin.checkCasaVacia("2", ruta);
                    }

                    List<LecturaEntity> obj = admin.findAllByCodRuta(ruta);

                    int lecturaAnterior = Integer.valueOf(obj.get(0).getLectura_anterior());
                    int lecturaActual = Integer.valueOf(txtlecturaActual);

                    String consumoMes6 = obj.get(0).getConsumo_mes_6();
                    String consumoMes5 = obj.get(0).getConsumo_mes_5();
                    String consumoMes4 = obj.get(0).getConsumo_mes_4();
                    String consumoMes3 = obj.get(0).getConsumo_mes_3();
                    String consumoMes2 = obj.get(0).getConsumo_mes_2();
                    String consumoMes1 = obj.get(0).getConsumo_mes_1();
                    int promedio = Integer.valueOf(obj.get(0).getPromedio());
                    int estadoMedidor = Integer.valueOf(obj.get(0).getEstado_medidor());
                    int consumoBasico = Integer.valueOf(obj.get(0).getConsumo_basico());
                    int casaVacia = Integer.valueOf(obj.get(0).getCasa_vacia());
                    int valorMtr3Acueducto = Integer.valueOf(obj.get(0).getValor_mtr3_acueducto());
                    int cargoFijoAcueducto = Integer.valueOf(obj.get(0).getCargo_fijo_acueducto());
                    int mtrsMaxSubsidio = Integer.valueOf(obj.get(0).getMtrs_max_subsidio());

                    int valorMtr3Alcantarillado = Integer.valueOf(obj.get(0).getValor_mtr3_alcantarillado());
                    int cargoFijoAlcantarillado = Integer.valueOf(obj.get(0).getCargo_fijo_alcantarillado());
                    double porcentajeSubsidioAcueducto = Double.valueOf(obj.get(0).getPorcentaje_subsidio_acueducto());
                    double porcentajeSubsidioAlcantarillado = Double.valueOf(obj.get(0).getPorcentaje_subsidio_alcantarillado());

                    int servicioAcueducto = Integer.valueOf(obj.get(0).getServicio_acueducto());
                    int servicioAlcantarillado = Integer.valueOf(obj.get(0).getServicio_alcantarillado());
                    int servicioAseo = Integer.valueOf(obj.get(0).getServicio_aseo());

                    //#######################################################################
                    //VARIABLES FINALES
                    int lectura = 0; //DATO A GUARDAR EN EL CAMPO lectura
                    int consumoAcueducto = 0; //DATO A GUARDAR EN EL CAMPO acueductoConsumo
                    int subsidioAcueducto = 0; //DATO A GUARDAR EN EL CAMPO subsidioAcueducto
                    int consumoAlcantarillado = 0; //DATO A GUARDAR EN EL CAMPO consumoAlcantarillado
                    int subsidioAlcantarillado = 0; //DATO A GUARDAR EN EL CAMPO subsidioAlcantarillado

                    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CÁLCULOS PARA EL CAMPO LECTURA
                    if ((lecturaActual < 0 || estadoMedidor == 1) &&
                            (consumoMes6 != "" &&
                                    consumoMes5 != "" &&
                                    consumoMes4 != "" &&
                                    consumoMes3 != "" &&
                                    consumoMes2 != "" &&
                                    consumoMes1 != "")) {

                        lectura = promedio;
                        System.out.println("LA LECTURA ES promedio " + lectura);
                    } else if (lecturaActual > 0 && estadoMedidor == 2) {
                        lectura = lecturaActual - lecturaAnterior;
                        System.out.println("LA LECTURA ES lecturaActual - lecturaAnterior " + lectura);
                    } else {
                        lectura = consumoBasico;
                        System.out.println("LA LECTURA ES consumoBasico " + lectura);
                    }

                    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CÁLCULOS PARA EL CAMPO CONSUMO ACUEDUCTO
                    if (servicioAcueducto == 1) {
                        if (casaVacia == 1) {
                            consumoAcueducto = 0;
                            System.out.println("VALOR consumoAcueducto, casaVacia == 1 " + consumoAcueducto);
                        } else {
                            consumoAcueducto = lectura * valorMtr3Acueducto;
                            System.out.println("VALOR consumoAcueducto casaVacia == 2 " + consumoAcueducto);
                        }
                    } else {
                        consumoAcueducto = 0;
                        System.out.println("NO TIENE SERVICIO DE Acueducto " + consumoAcueducto);
                    }

                    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CÁLCULOS PARA EL CAMPO SUBSIDIO ACUEDUCTO
                    if (servicioAcueducto == 1) {
                        int val = (int) (cargoFijoAcueducto * porcentajeSubsidioAcueducto);
                        if (lectura > 30) {
                            subsidioAcueducto = (int) (val + (valorMtr3Acueducto * mtrsMaxSubsidio) * porcentajeSubsidioAcueducto);
                        } else {
                            subsidioAcueducto = (int) (val + consumoAcueducto * porcentajeSubsidioAcueducto);
                        }
                    } else {
                        subsidioAcueducto = 0;
                        System.out.println("NO TIENE SERVICIO DE Acueducto " + subsidioAcueducto);
                    }

                    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CÁLCULOS PARA EL CAMPO CONSUMO ALCANTARILLADO
                    if (servicioAlcantarillado == 1) {
                        if (casaVacia == 1) {
                            consumoAlcantarillado = 0;
                            System.out.println("VALOR consumoAlcantarillado, casaVacia == 1 " + consumoAlcantarillado);
                        } else {
                            consumoAlcantarillado = lectura * valorMtr3Alcantarillado;
                            System.out.println("VALOR consumoAlcantarillado casaVacia == 2 " + consumoAlcantarillado);
                        }
                    } else {
                        consumoAlcantarillado = 0;
                        System.out.println("NO TIENE SERVICIO DE Alcantarillado " + consumoAlcantarillado);
                    }

                    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CÁLCULOS PARA EL CAMPO SUBSIDIO ALCANTARILLADO
                    if (servicioAlcantarillado == 1) {
                        int val = (int) (cargoFijoAlcantarillado * porcentajeSubsidioAlcantarillado);
                        if (lectura > 30) {
                            subsidioAlcantarillado = (int) (val + (valorMtr3Alcantarillado * mtrsMaxSubsidio) * porcentajeSubsidioAlcantarillado);
                        } else {
                            subsidioAlcantarillado = (int) (val + consumoAlcantarillado * porcentajeSubsidioAlcantarillado);
                        }
                    } else {
                        subsidioAlcantarillado = 0;
                        System.out.println("NO TIENE SERVICIO DE Alcantarillado " + subsidioAlcantarillado);
                    }

                    System.out.println("VALOR DE lectura " + lectura);
                    System.out.println("VALOR DE consumoAcueducto " + consumoAcueducto);
                    System.out.println("VALOR DE subsidioAcueducto " + subsidioAcueducto);
                    System.out.println("VALOR DE consumoAlcantarillado " + consumoAlcantarillado);
                    System.out.println("VALOR DE subsidioAlcantarillado " + subsidioAlcantarillado);

                    admin.guardarLectura(txtlecturaActual,
                            String.valueOf(lectura),
                            String.valueOf(consumoAcueducto),
                            String.valueOf(subsidioAcueducto),
                            String.valueOf(consumoAlcantarillado),
                            String.valueOf(subsidioAlcantarillado),
                            ruta);

                    Toast.makeText(getActivity(), "FACTURA GENERADA EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                }

                FacturaController factura = new FacturaController();
                createPDF(ruta);
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

    private void createPDF(String ruta){

        admin = new MainController(getActivity(), "Servicios_publicos", null, 1);
        List<CompaniaEntity> obj = admin.findAllCompania();
        List<LecturaEntity> lec = admin.findAllByCodRuta(ruta);


        int pagewidth = 390;

        PdfDocument pdfdocument = new PdfDocument();
        Paint paint = new Paint();
        Paint paint_table = new Paint();
        Paint paint_color = new Paint();
        Paint paint_titulo = new Paint();
        Paint paint_texto = new Paint();
        Paint paint_texto_peque = new Paint();
        Paint paint_total = new Paint();
        Paint paint_num = new Paint();
        Paint paint_final = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(390,975,1).create();
        PdfDocument.Page page = pdfdocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        //canvas.drawBitmap(scaledbmp,20,35, paint); //IMAGEN
        canvas.drawBitmap(scaledbmp2,20,35, paint); //IMAGEN

        paint.setColor(Color.rgb(53,168,243));
        paint.setTextSize(11);
        paint.setTextAlign(Paint.Align.RIGHT);

        paint_titulo.setColor(Color.rgb(253,253,253));
        paint_titulo.setTextSize(11);
        paint_titulo.setTextAlign(Paint.Align.CENTER);

        paint_texto.setTextSize(11);
        paint_texto.setTextAlign(Paint.Align.LEFT);
        //paint_texto.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));


        paint_num.setTextSize(11);
        paint_num.setTextAlign(Paint.Align.RIGHT);
        //paint_texto.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));

        paint_texto_peque.setTextSize(8);
        paint_texto_peque.setTextAlign(Paint.Align.LEFT);

        paint_total.setColor(Color.rgb(53,168,243));
        paint_total.setTextSize(17);
        paint_total.setTextAlign(Paint.Align.LEFT);

        paint_final.setTextSize(17);
        paint_final.setTextAlign(Paint.Align.RIGHT);
        paint_final.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        //##########################################################################################

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(250,75,pagewidth-20,95, paint_table); //RECTÁNGULO # FACTURA

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,100,pagewidth-20,115, paint_color); //RECTÁNGULO TÍTULOS

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,100,pagewidth-20,160, paint_table); //RECTÁNGULO DATOS SUSCRIPTOR

        //canvas.drawText("EMPRESA DE SERVICIOS PÚBLICOS DE PAZ DE RÍO",pagewidth-20,30, paint); //TEXTO PAZ DE RÍO
        canvas.drawText("ASOCIACION DE USUARIOS ACUEDUCTO DE LA PEDREGOSA",pagewidth-20,30, paint); //TEXTO
        canvas.drawText("NUIR. " + obj.get(0).getNuir()+ " VIGILADO POR LA SSPD",pagewidth-50,45, paint);
        canvas.drawText("NIT. " + obj.get(0).getNit(),pagewidth-120,60, paint);
        canvas.drawText("FACTURA N° " ,248,90, paint);
        canvas.drawText(lec.get(0).getNumero_factura() ,270,90, paint_texto);
        canvas.drawText("DATOS DEL SUSCRIPTOR ",195,112, paint_titulo);
        canvas.drawText("Código de Ruta:  " + lec.get(0).getCodigo_ruta(),25,128, paint_texto);
        canvas.drawText("Nombre:  " + lec.get(0).getUsuario(),25,140, paint_texto);
        canvas.drawText("Dirección:  " + lec.get(0).getDireccion(),25,152, paint_texto);

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,162,pagewidth-20,177, paint_color); //RECTÁNGULO TÍTULOS

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,162,pagewidth-20,290, paint_table); //RECTÁNGULO INFORMACIÓN TÉCNICA

        canvas.drawLine(107,253,107,290, paint_table);
        canvas.drawLine(194,253,194,290, paint_table);
        canvas.drawLine(281,253,281,290, paint_table);

        canvas.drawLine(20,253,pagewidth-20,253, paint_table);
        canvas.drawLine(20,272,pagewidth-20,272, paint_table); //EJE Y -- HORIZONTAL

        canvas.drawText("INFORMACIÓN TÉCNICA ",195,174, paint_titulo);
        canvas.drawText("Periodo Facturación:  " + lec.get(0).getPeriodo(),25,188, paint_texto);
        canvas.drawText("Estrato:  " + lec.get(0).getEstrato(),25,200, paint_texto);
        canvas.drawText("Uso:  " + lec.get(0).getUso(),25,212, paint_texto);
        canvas.drawText("Fecha de Entrega:  " + lec.get(0).getFecha_lectura(),25,224, paint_texto);
        canvas.drawText("Nro. Medidor:  " + lec.get(0).getNumero_medidor(),25,237, paint_texto);

        canvas.drawText("Mes6   "+ lec.get(0).getConsumo_mes_6(),200,200, paint_texto_peque);
        canvas.drawText("Mes5   "+ lec.get(0).getConsumo_mes_5(),200,210, paint_texto_peque);
        canvas.drawText("Mes4   "+ lec.get(0).getConsumo_mes_4(),200,220, paint_texto_peque);
        canvas.drawText("Mes3   "+ lec.get(0).getConsumo_mes_3(),200,230, paint_texto_peque);
        canvas.drawText("Mes2   "+ lec.get(0).getConsumo_mes_2(),200,240, paint_texto_peque);
        canvas.drawText("Mes1   "+ lec.get(0).getConsumo_mes_1(),200,250, paint_texto_peque);

        int barras = 251;
        if(!lec.get(0).getConsumo_mes_6().equals("")){

            int dt = barras-Integer.parseInt(lec.get(0).getConsumo_mes_6()) /2;
            paint_color.setColor(Color.rgb(53,168,243));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(260,dt,285,270, paint_color);
        }
        if(!lec.get(0).getConsumo_mes_5().equals("")){

            int dt = barras-Integer.parseInt(lec.get(0).getConsumo_mes_5()) /2;
            paint_color.setColor(Color.rgb(53,168,243));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(275,dt,285,251, paint_color);
        }
        if(!lec.get(0).getConsumo_mes_4().equals("")){

            int dt = barras-Integer.parseInt(lec.get(0).getConsumo_mes_4()) /2;
            paint_color.setColor(Color.rgb(53,168,243));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(290,dt,300,251, paint_color);
        }
        if(!lec.get(0).getConsumo_mes_3().equals("")){

            int dt = barras-Integer.parseInt(lec.get(0).getConsumo_mes_3()) /2;
            paint_color.setColor(Color.rgb(53,168,243));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(305,dt,315,251, paint_color);
        }
        if(!lec.get(0).getConsumo_mes_2().equals("")){

            int dt = barras-Integer.parseInt(lec.get(0).getConsumo_mes_2()) /2;
            paint_color.setColor(Color.rgb(53,168,243));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(320,dt,330,251, paint_color);
        }
        if(!lec.get(0).getConsumo_mes_1().equals("")){

            int dt = barras-Integer.parseInt(lec.get(0).getConsumo_mes_1()) /2;
            paint_color.setColor(Color.rgb(53,168,243));
            paint_color.setStrokeWidth(1);
            canvas.drawRect(335,dt,345,251, paint_color);
        }

        int dt = barras-Integer.parseInt(lec.get(0).getConsumo_basico()) /2;
        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(350,dt,360,251, paint_texto);

        canvas.drawText("Lectura Anterior",106,267, paint);
        canvas.drawText("Lectura Actual",190,267, paint);
        canvas.drawText("Consumo M³",271,267, paint);
        canvas.drawText("Promedio",350,267, paint);
        canvas.drawText(lec.get(0).getLectura_anterior(),60,285, paint_texto);
        canvas.drawText(lec.get(0).getLectura_actual(),145,285, paint_texto);
        canvas.drawText(lec.get(0).getConsumo_basico(),235,285, paint_texto);
        canvas.drawText(lec.get(0).getPromedio(),320,285, paint_texto);


        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,292,pagewidth-20,307, paint_color); //RECTÁNGULO TÍTULOS

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,292,pagewidth-20,350, paint_table); //RECTÁNGULO OBSERVACIONES

        canvas.drawText("AVISO AL SUSCRIPTOR Y/O USUARIO",195,304, paint_titulo);

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,352,pagewidth-20,367, paint_color); //RECTÁNGULO TÍTULOS

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,369,194,384, paint_color); //RECTÁNGULO A

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(196,369,pagewidth-20,384, paint_color); //RECTÁNGULO B

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,485,194,500, paint_color); //RECTÁNGULO A TOTAL

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(196,485,pagewidth-20,500, paint_color); //RECTÁNGULO B TOTAL

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,352,pagewidth-20,520, paint_table); //RECTÁNGULO DETALLE DE FACTURA

        canvas.drawLine(130,369,130,399, paint_table);
        canvas.drawLine(195,369,195,520, paint_table);
        canvas.drawLine(305,369,305,399, paint_table);
        canvas.drawLine(20,399,pagewidth-20,399, paint_table); //EJE Y -- HORIZONTAL

        canvas.drawText("DETALLE DE FACTURACIÓN DEL PERIODO",195,364, paint_titulo);
        canvas.drawText("ACUEDUCTO",105,381, paint_titulo);
        canvas.drawText("ALCANTARILLADO",285,381, paint_titulo);
        canvas.drawText("Concepto",100,396, paint);
        canvas.drawText("Valor",175,396, paint);
        canvas.drawText("Concepto",275,396, paint);
        canvas.drawText("Valor",350,396, paint);
        canvas.drawText("TOTAL ACUEDUCTO",105,497, paint_titulo);
        canvas.drawText("TOTAL ALCANTARILLADO",285,497, paint_titulo);

        canvas.drawText("Cargo Fijo",25,415, paint_texto);
        canvas.drawText("Valor M³",25,427, paint_texto);
        canvas.drawText(admin.formatoSalida(lec.get(0).getCargo_fijo_acueducto()),193,415, paint_num);
        canvas.drawText(admin.formatoSalida(lec.get(0).getValor_mtr3_acueducto()),193,427, paint_num);


        double cargoA = admin.formato(lec.get(0).getCargo_fijo_acueducto());
        double m3A = admin.formato(lec.get(0).getValor_mtr3_acueducto());
        double totalA = cargoA + m3A;
        canvas.drawText(admin.formatoSalida(String.valueOf(totalA))+".00",90,514, paint_texto); //TOTAL ACUEDUCTO

        canvas.drawText("Cargo Fijo",200,415, paint_texto);
        canvas.drawText("Valor M³",200,427, paint_texto);
        canvas.drawText(admin.formatoSalida(lec.get(0).getCargo_fijo_alcantarillado()),367,415, paint_num);
        canvas.drawText(admin.formatoSalida(lec.get(0).getValor_mtr3_alcantarillado()),367,427, paint_num);

        double cargoB = admin.formato(lec.get(0).getCargo_fijo_alcantarillado());
        double m3B = admin.formato(lec.get(0).getValor_mtr3_alcantarillado());
        double totalB = cargoB + m3B;
        canvas.drawText(admin.formatoSalida(String.valueOf(totalB))+".00",270,514, paint_texto); //TOTAL ALCANTARILLADO


        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,522,pagewidth-20,537, paint_color); //RECTÁNGULO TÍTULOS

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,522,pagewidth-20,604, paint_table); //RECTÁNGULO FINANCIACIÓN

        canvas.drawLine(20,557,pagewidth-20,557, paint_table); //EJE Y -- HORIZONTAL

        canvas.drawText("FINANCIACIÓN",195,534, paint_titulo);
        canvas.drawText("Concepto",78,552, paint);
        canvas.drawText("Valor",150,552, paint);
        canvas.drawText("# Cuotas",205,552, paint);
        canvas.drawText("Saldo",245,552, paint);
        canvas.drawText("C. Pend.",300,552, paint);
        canvas.drawText("Valor Cuota",pagewidth-22,552, paint);
        canvas.drawText("Subtotal",72,600, paint);

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(20,606,194,621, paint_color); //RECTÁNGULO TÍTULOS

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,606,194,700, paint_table); //RECTÁNGULO SERVICIO A PAGAR ASEO

        canvas.drawText("SERVICIO A PAGAR ASEO",110,618, paint_titulo);
        canvas.drawText("Aseo",25,632, paint_texto);
        canvas.drawText("Subsidio",25,644, paint_texto);
        canvas.drawText("Interes Mora",25,656, paint_texto);
        canvas.drawText("Subtotal:",25,690, paint_texto);
        canvas.drawText(admin.formatoSalida(lec.get(0).getCargo_fijo_aseo()),193,632, paint_num);
        canvas.drawText("-"+admin.formatoSalida(lec.get(0).getSubsidio_aseo()),193,644, paint_num);
        canvas.drawText(admin.formatoSalida(lec.get(0).getIntereses_de_mora_aseo()),193,656, paint_num);

        double cpto1 = admin.formato(lec.get(0).getCargo_fijo_aseo()); //RECIBE VALOR CON FORMATO
        double cpto2 = admin.formato(lec.get(0).getSubsidio_aseo());
        double cpto3 = admin.formato(lec.get(0).getIntereses_de_mora_aseo());

        double totalCptoAseo = cpto1 - cpto2 + cpto3;
        canvas.drawText(admin.formatoSalida(String.valueOf(totalCptoAseo)),193,690, paint_num);


        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(196,606,pagewidth-20,621, paint_color); //RECTÁNGULO TÍTULOS

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(196,606,pagewidth-20,700, paint_table); //RECTÁNGULO RESUMEN FACTURA

        canvas.drawText("RESUMEN FACTURA",280,618, paint_titulo);
        canvas.drawText("Acueducto",200,631, paint_texto);
        canvas.drawText("Alcantarillado",200,643, paint_texto);
        canvas.drawText("Aseo",200,655, paint_texto);
        canvas.drawText("Ajuste",200,667, paint_texto);
        canvas.drawText("Deuda Anterior",200,679, paint_texto);
        canvas.drawText("TOTAL",200,698, paint_texto);

        double TOTAL =  admin.formato(String.valueOf(totalA)) +
                        admin.formato(String.valueOf(totalB)) +
                        admin.formato(String.valueOf(totalCptoAseo)) +
                        admin.formato(lec.get(0).getDeuda_anterior());

        double fn = Math.round(TOTAL);
        String nn = String.valueOf(fn)+"0";
        double AJUSTE = fn - TOTAL;
        System.out.println("TOTAL CALCULADO "+ Math.round(AJUSTE * 100.0) / 100.0);

        canvas.drawText(admin.formatoSalida(String.valueOf(totalA))+".00",367,631, paint_num); //TOTAL ACUEDUCTO
        canvas.drawText(admin.formatoSalida(String.valueOf(totalB))+".00",367,643, paint_num); //TOTAL ALCANTARILLADO
        canvas.drawText(admin.formatoSalida(String.valueOf(totalCptoAseo)),367,655, paint_num); //TOTAL ASEO
        canvas.drawText(admin.formatoSalida(String.valueOf(AJUSTE)),367,667, paint_num); //TOTAL AJUSTE
        canvas.drawText(admin.formatoSalida(lec.get(0).getDeuda_anterior()),367,679, paint_num); //TOTAL DEUDA ANTERIOR

        canvas.drawText(admin.formatoSalida(String.valueOf(Math.round(TOTAL))),367,698, paint_num);


        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(20,702,pagewidth-20,780, paint_table); //RECTÁNGULO TOTAL FACTURA

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(60,712,160,729, paint_table);

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(60,742,200,765, paint_table);

        canvas.drawText("PAGAR ANTES DE:",158,725, paint);
        canvas.drawText("TOTAL FACTURA:",65,760, paint_total);
        canvas.drawText(lec.get(0).getFecha_vencimiento(),200,725, paint_texto);
        canvas.drawText(admin.formatoSalida(String.valueOf(Math.round(TOTAL))),340,760, paint_final);

        paint_table.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(1);
        canvas.drawRect(250,857,pagewidth-20,877, paint_table); //RECTÁNGULO # FACTURA

        //canvas.drawBitmap(scaledbmp,20,815, paint); //IMAGEN
        canvas.drawBitmap(scaledbmp2,20,815, paint); //IMAGEN
        //canvas.drawText("EMPRESA DE SERVICIOS PÚBLICOS DE PAZ DE RÍO",pagewidth-20,812, paint); //TEXTO PAZ DE RÍO
        canvas.drawText("ASOCIACION DE USUARIOS ACUEDUCTO DE LA PEDREGOSA",pagewidth-20,812, paint); //TEXTO
        canvas.drawText("NUIR. " + obj.get(0).getNuir()+ " VIGILADO POR LA SSPD",pagewidth-50,827, paint);
        canvas.drawText("NIT. " + obj.get(0).getNit(),pagewidth-120,842, paint);
        canvas.drawText("FACTURA N° " ,248,872, paint);
        canvas.drawText(lec.get(0).getNumero_factura() ,270,872, paint_texto);
        canvas.drawText("Código Ruta ",120,890, paint);
        canvas.drawText(lec.get(0).getCodigo_ruta(),70,905, paint_texto);
        canvas.drawText("Periodo Facturación " ,300,890, paint);
        canvas.drawText(lec.get(0).getPeriodo(),155,905, paint_texto);
        canvas.drawText("TOTAL ACUEDUCTO/ALCANTARILLADO/ASEO",260,925, paint);

        double TOTAL_SERVICIOS =  admin.formato(String.valueOf(totalA)) +
                admin.formato(String.valueOf(totalB)) +
                admin.formato(String.valueOf(totalCptoAseo));

        canvas.drawText(admin.formatoSalida(String.valueOf(Math.round(TOTAL_SERVICIOS))),340,925, paint_num);

        paint_color.setColor(Color.rgb(53,168,243));
        paint_color.setStrokeWidth(1);
        canvas.drawRect(30,940,210,960, paint_color); //RECTÁNGULO TÍTULOS

        canvas.drawText("VALOR A PAGAR",120,955, paint_titulo);
        canvas.drawText(admin.formatoSalida(String.valueOf(Math.round(TOTAL))),345,957, paint_final);




        pdfdocument.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory() + "/Sigiep/Factura.pdf");

        if (file.exists()){
            file.delete();
            //Toast.makeText(getActivity(), "SE ELIMINÓ EL ARCHIVO", Toast.LENGTH_SHORT).show();
            System.out.println("SE ELIMINÓ EL ARCHIVO");
        }

        try {
            pdfdocument.writeTo(new FileOutputStream(file));
            //Toast.makeText(getActivity(), "SE GENERÓ LA FACTURA", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfdocument.close();

    }
}
