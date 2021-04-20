package com.example.serviciospublicos.print;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.serviciospublicos.R;
import com.example.serviciospublicos.RegistrarFactura;
import com.example.serviciospublicos.modelo.ByteArrayRequest;
import com.example.serviciospublicos.util.SettingsHelper;
import com.example.serviciospublicos.util.UIHelper;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

public class PrintUtil {

    public static final String FILE_RES_NAME="facturaRes.lbl";

    private static String FILE_TEMPLATE;
    private static Request<byte[]> request;

    private static Connection connection = null;

    private static Connection getConnection(String macAddress) throws ConnectionException {
        if(connection==null || !connection.isConnected()){
            connection= new BluetoothConnection(macAddress);
            connection.open();
        }
        return  connection;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getTemplateFile(Activity activity) throws IOException {
        if (FILE_TEMPLATE==null) {
            InputStream inss = activity.getResources().openRawResource(R.raw.factura);
            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (inss, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }
            FILE_TEMPLATE=textBuilder.append("^XA^PH^XZ").toString();
            //FILE_TEMPLATE=textBuilder.toString();
        }
        return FILE_TEMPLATE;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createResFile(Activity activity, String template) throws IOException {
        FileOutputStream os = activity.openFileOutput(FILE_RES_NAME, Context.MODE_PRIVATE);
        os.write(template.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void createTestFile(Activity activity, ZebraPrinter printer, String macAddress, UIHelper helper) {
        try {
            File filepath = activity.getFileStreamPath(PrintUtil.FILE_RES_NAME);
            String template=PrintUtil.getTemplateFile(activity);
            template=template.replace("802000000000>8","802001112325>8");
            template=template.replaceFirst(">839000000000000>8",">839000000024004>8");
            template=template.replaceFirst(">839000000000000>8",">839000000021996>8");
            template=template.replace(">89600000000",">89620201208");

            createResFile(activity,template);
            printer.sendFileContents(filepath.getAbsolutePath());
            SettingsHelper.saveBluetoothAddress(activity, macAddress);
        } catch (ConnectionException e1) {
            helper.showErrorDialogOnGuiThread("Error sending file to printer");
        } catch (IOException e) {
            helper.showErrorDialogOnGuiThread("Error creating file");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String createFile(Activity activity, UIHelper helper, Map<String,Object> usuario) {
        try {
            File filepath = activity.getFileStreamPath(PrintUtil.FILE_RES_NAME);
            String template = PrintUtil.getTemplateFile(activity);
            Iterator<String> keys = usuario.keySet().iterator();
            String key;
            String value;
            Object valueOb;
            while (keys.hasNext()) {
                key = keys.next();
                valueOb= usuario.get(key);
                value=valueOb==null?"":valueOb.toString();
                template = template.replace("&" + key + "&", value);
            }

            template = dibujarBarras(template,usuario);
            template = template.replace("802000000000>8", "8020"+usuario.get("codigo_referencia_f")+">8");//replazar por referencia
            template = template.replace(">839000000000000>8", ">83900"+ usuario.get("TOTAL_ff")+">8");//valor acueducto y alcantarillado
            template = template.replace(">839009999999999>8", ">83900" + usuario.get("totalCptoAseo_ff") + ">8");//valor de aseo
                template = template.replace(">89600000000", ">896"+usuario.get("fecha_vencimiento_f")+"");//fecha limite de pago
            createResFile(activity, template);
            return filepath.getAbsolutePath();
        } catch (IOException e) {
            helper.showErrorDialogOnGuiThread("Error creando archivo");
        }
        return null;
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);
        return sb.toString();
    }

    public static String dibujarBarras(String template,Map<String,Object> usuario){
        int indiceLineas= template.indexOf("#lineas#");
        String lineaOrigen= template.substring(template.substring(0,indiceLineas).lastIndexOf("\n"),template.indexOf("\n",indiceLineas));
        int separacion=28;
        int tamMaximo=120;
        int xInicial=Integer.parseInt(lineaOrigen.substring(4, lineaOrigen.indexOf(',')));
        int yInicial=Integer.parseInt(lineaOrigen.substring(lineaOrigen.indexOf(',') + 1, lineaOrigen.indexOf('^', 4)));
        String plantillaBarra="^FOX,Y^GB0,T,16^FS";
        int lecturaMayor=0;
        int []lecturas=new int[6];
        for (int i=6; i>0; i--){
            try {
                lecturas[i-1]= Integer.parseInt(usuario.get("consumoMes"+i).toString());
                if(lecturas[i-1]>lecturaMayor){
                    lecturaMayor=lecturas[i-1];
                }
            } catch (NumberFormatException e) {
                lecturas[i-1]=0;
            }
        }
        int multiplo = (lecturaMayor % 12 == 0) ? lecturaMayor / 12 : (lecturaMayor / 12 + 1);
        double escala = 10d / (double) multiplo;
        int valor;
        int xActual=xInicial;
        StringBuilder barras= new StringBuilder("");
        for (int i=5; i>=0; i--){
            valor=lecturas[i];
            if(valor>0){
                valor=(int) Math.round(valor * escala);
                barras.append(
                        plantillaBarra.replace("X",String.valueOf(xActual)).
                                replace("Y",String.valueOf(yInicial)).
                                replace("T",String.valueOf(valor)));
                if(i>0){
                    barras.append("\r\n");
                }
            }
            xActual=xActual-separacion;
        }
        if (barras.toString().isEmpty()){
            template = template.replace("#lineas#", "");
        }else {
            template = template.replace(lineaOrigen.trim(), barras.toString());
        }
        return template;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void sendTestFile(Activity activity, String macAddress, UIHelper helper) {
        try {
         //   helper.showLoadingDialog("Enviando impresion ...");
            Connection connection =getConnection(macAddress);
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            createTestFile(activity,printer,macAddress,helper);
        } catch (ConnectionException e) {
            if (connection.isConnected()){
                try {
                    connection.close();
                } catch (ConnectionException connectionException) {
                    connectionException.printStackTrace();
                }
                connection = null;
                sendTestFile(activity, macAddress, helper);
            }else{
                helper.showErrorDialogOnGuiThread(e.getMessage());
                e.printStackTrace();
            }
        } catch (ZebraPrinterLanguageUnknownException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
            e.printStackTrace();
        } finally {
           // helper.dismissLoadingDialog();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void sendFile(Activity activity, String macAddress, UIHelper helper, Map<String,Object> usuario) {

        try {
            Connection connection = getConnection(macAddress);
            //helper.showLoadingDialog("Enviando impresion ...");
            String filepath=createFile(activity,helper,usuario);
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            printer.sendFileContents(filepath);
            SettingsHelper.saveBluetoothAddress(activity, macAddress);
        } catch (ConnectionException e) {
            if (connection.isConnected()){
                try {
                    connection.close();
                } catch (ConnectionException connectionException) {
                    connectionException.printStackTrace();
                }
                connection = null;
                sendFile(activity, macAddress, helper, usuario);
            }else{
                helper.showErrorDialogOnGuiThread(e.getMessage());
                e.printStackTrace();
            }
        } catch (ZebraPrinterLanguageUnknownException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
            e.printStackTrace();
        }
        finally {
            //helper.dismissLoadingDialog();
        }
    }


    public static void pdf(final Context context){

        /*
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//for POST requests, only the following line should be changed to

                 request = new ByteArrayRequest(Request.Method.POST, "http://api.labelary.com/v1/printers/8dpmm/labels/8x15/0/", new Response.Listener<byte[]>() {
                     @Override
                     public void onResponse(byte[] response) {
                         Log.i("getBilletCard", response.toString());
                         try {
                             byte[] bytes = response;
                             saveToFile(bytes, "label.pdf",context);
                         } catch (Exception e) {
                             //Toast.makeText(this, "Erro ao converter resposta", Toast.LENGTH_LONG).show();
                         }
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         // As of f605da3 the following should work
                         NetworkResponse response = error.networkResponse;
                         if (error instanceof ServerError && response != null) {
                             try {
                                 String res = new String(response.data,
                                         HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                 // Now you can use any deserializer to make sense of data
                                 JSONObject obj = new JSONObject(res);
                             } catch (UnsupportedEncodingException e1) {
                                 // Couldn't properly decode data to string
                                 e1.printStackTrace();
                             } catch (JSONException e2) {
                                 // returned data is not JSONObject?
                                 e2.printStackTrace();
                             }
                         }
                         error.printStackTrace();
                     }
                 }){

                     @Override
                     public Map<String, String> getHeaders() throws AuthFailureError {
                         HashMap<String, String> headers = new HashMap<String, String>();
                         //headers.put("Content-Type", "application/json");
                         headers.put("label", ByteString.encodeUtf8(FILE_TEMPLATE).base64());
                         return headers;
                     }
                 };
                 requestQueue.add(request);
  */  }

    public static void saveToFile(byte[] byteArray, String pFileName, Context context){
        File f = new File(Environment.getExternalStorageDirectory() + "/myappname");
        if (!f.isDirectory()) {
            f.mkdir();
        }

        String fileName = Environment.getExternalStorageDirectory() + "/myappname/" + pFileName;

        try {

            FileOutputStream fPdf = new FileOutputStream(fileName);

            fPdf.write(byteArray);
            fPdf.flush();
            fPdf.close();
            Toast.makeText(context.getApplicationContext(), "File successfully saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(context.getApplicationContext(), "File create error", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "File write error", Toast.LENGTH_LONG).show();
        }

    }
}
