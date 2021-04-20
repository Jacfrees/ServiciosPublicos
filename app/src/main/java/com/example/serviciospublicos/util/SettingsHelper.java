/***********************************************
 * CONFIDENTIAL AND PROPRIETARY 
 * 
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * This source code, and any other information contained herein, shall not be copied, reproduced, published, 
 * displayed or distributed, in whole or in part, in any medium, by any means, for any purpose except as
 * expressly permitted under such license agreement.
 * 
 * Copyright ZIH Corp. 2012
 * 
 * ALL RIGHTS RESERVED
 ***********************************************/

package com.example.serviciospublicos.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsHelper {

    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    private static final String tcpStatusPortKey = "ZEBRA_DEMO_TCP_STATUS_PORT";
    private static final String NameUsuario = "SavedName";
    private static final String rutaActual = "rutaActual";
    private static final String registroActual = "registroActual";
    private static final String ultimoRegistroImpreso = "ultimoRegistroImpreso";



    public static String getIp(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(tcpAddressKey, "");
    }

    public static String getPort(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(tcpPortKey, "");
    }

    public static String getStatusPort(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(tcpStatusPortKey, "");
    }

    public static String getBluetoothAddress(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(bluetoothAddressKey, "");
    }

    public static void saveIp(Context context, String ip) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(tcpAddressKey, ip);
        editor.commit();
    }

    public static void savePort(Context context, String port) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(tcpPortKey, port);
        editor.commit();
    }

    public static void saveStatusPort(Context context, String port) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(tcpStatusPortKey, port);
        editor.commit();
    }

    public static void saveBluetoothAddress(Context context, String address) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(bluetoothAddressKey, address);
        editor.commit();
    }

    public static void saveName(Context context, String address) {
        SharedPreferences settings = context.getSharedPreferences(NameUsuario, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NameUsuario, address);
        editor.commit();
    }

    public static String verNombre(Context context){
        SharedPreferences preferences = context.getSharedPreferences(NameUsuario,0);
        return preferences.getString(NameUsuario,"DEFAULT");

    }

    public static void rutaActual(Context context, String address) {
        SharedPreferences settings = context.getSharedPreferences(rutaActual, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(rutaActual, address);
        editor.commit();
    }

    public static String verRutaActual(Context context){
        SharedPreferences preferences = context.getSharedPreferences(rutaActual,0);
        return preferences.getString(rutaActual,"");

    }

    public static void registroActual(Context context, String address) {
        SharedPreferences settings = context.getSharedPreferences(registroActual, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(registroActual, address);
        editor.commit();
    }

    public static String verRegistroActual(Context context){
        SharedPreferences preferences = context.getSharedPreferences(registroActual,0);
        return preferences.getString(registroActual,"");

    }

    public static void ultimoRegistroImpreso(Context context, String address) {
        SharedPreferences settings = context.getSharedPreferences(ultimoRegistroImpreso, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ultimoRegistroImpreso, address);
        editor.commit();
    }

    public static String verUltimoRegistroImpreso(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ultimoRegistroImpreso,0);
        return preferences.getString(ultimoRegistroImpreso,"");

    }
}
