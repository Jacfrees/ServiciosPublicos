package com.sigiep.serviciospublicos.controllers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.sigiep.serviciospublicos.models.CompaniaEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FacturaController  {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPDF(String ruta){

        MainController admin = new MainController(null, "Servicios_publicos", null, 1);
        List<CompaniaEntity> obj = admin.findAllCompania();


        int pagewidth = 389;

        PdfDocument pdfdocument = new PdfDocument();
        Paint paint = new Paint();
        Paint paint_table = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(389,2010,1).create();
        PdfDocument.Page page = pdfdocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        paint.setColor(Color.rgb(53,168,243));
        paint.setTextSize(10);
        paint.setTextAlign(Paint.Align.RIGHT);

        paint.setColor(Color.rgb(53,168,243));
        paint_table.setStyle(Paint.Style.STROKE);
        paint_table.setStrokeWidth(2);
        canvas.drawRect(20,100,pagewidth-20,200, paint_table);

        canvas.drawText("EMPRESA DE SERVICIOS PÚBLICOS DE PAZ DE RÍO",350,40, paint);
        canvas.drawText("NUIR " + obj.get(0).getNuir(),350,50, paint);

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
