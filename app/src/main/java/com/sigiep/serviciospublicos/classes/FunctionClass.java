package com.sigiep.serviciospublicos.classes;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.sigiep.serviciospublicos.BuildConfig;

public class FunctionClass {

    /*
    private int getFirstTimeRun() {
        SharedPreferences sp = getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }

    private SharedPreferences getSharedPreferences(String myapp, int i) {
        String  = "1.0.0";

        return
    } */

    // CONSULTA NUMERO DE REGITROS EN BD
    public static boolean numeroRegistos(SQLiteDatabase db,String tabla){
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + tabla, null);

        try {
            if(cursor != null)
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    count = cursor.getInt(0);
                }
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if(count>0)
            return false;
        else
            return true;
    }
}
