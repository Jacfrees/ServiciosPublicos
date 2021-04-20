package com.example.serviciospublicos.Controlador;
import android.content.Context;
import android.os.CountDownTimer;

import java.util.Date;
public class TimerEnvio extends CountDownTimer {
    private static TimerEnvio INSTANCE;
    SincronizacionController sincro;
    Context context;
    private long millisInFuture;
    private long countDownIntervallong;
    public static TimerEnvio initInstance(Context context, long millisInFuture, long countDownIntervallong){
        if(INSTANCE==null){
            INSTANCE= new TimerEnvio(millisInFuture, countDownIntervallong);
            INSTANCE.context= context;
            INSTANCE.sincro = new SincronizacionController(context);
        }
        return INSTANCE;
    }
    public static TimerEnvio getInstance(){
        return INSTANCE;
    }
    private TimerEnvio(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }
    @Override
    public void onTick(long millisUntilFinished) { }
    @Override
    public void onFinish() {
        System.out.println("Entro +"+new Date());
    sincro.sincronizarSubida();
    this.start();
    }
}