package com.example.serviciospublicos.util;

import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class AppUtil {

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    public static String getUrlEncode(String url){
           return Uri.encode(url,ALLOWED_URI_CHARS);
    }

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }


}
