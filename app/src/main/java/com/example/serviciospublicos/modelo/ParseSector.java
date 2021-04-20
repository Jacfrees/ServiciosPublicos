package com.example.serviciospublicos.modelo;

import com.example.serviciospublicos.MainActivity;

import org.json.JSONObject;

public class ParseSector {
    public static ClSector jsonToclSector(JSONObject jsonObject){
        ClSector clSector = new ClSector();
        clSector.setSector(jsonObject.optString("sector"));

        return clSector;
    }
}
