package com.example.serviciospublicos;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.serviciospublicos.print.PrintUtil;
import com.example.serviciospublicos.util.SettingsHelper;
import com.example.serviciospublicos.util.UIHelper;



public class ConnectionScreen extends AppCompatActivity {

    protected Button testButton;
    protected Button saveButton;
    private EditText macAddress;


    public static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    public static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    public static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    public static final String tcpStatusPortKey = "ZEBRA_DEMO_TCP_STATUS_PORT";
    public static final String PREFS_NAME = "OurSavedAddress";

    private UIHelper helper = new UIHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_screen);

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);

        macAddress = (EditText) this.findViewById(R.id.macInput);
        String mac = settings.getString(bluetoothAddressKey, "");
        macAddress.setText(mac);

        testButton = (Button) this.findViewById(R.id.testButton);
        testButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                performTest();
            }
        });

        saveButton = (Button) this.findViewById(R.id.guardarButton);
        saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveMacAddress();
            }
        });

    }

    private void toggleEditField(EditText editText, boolean set) {
        editText.setEnabled(set);
        editText.setFocusable(set);
        editText.setFocusableInTouchMode(set);
    }

    protected String getMacAddressFieldText() {
        return macAddress.getText().toString();
    }

    public void performTest() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                Looper.prepare();
                sendFile();
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendFile() {
        PrintUtil.sendTestFile(this,getMacAddressFieldText(),helper);
    }



    private void saveMacAddress(){
        SettingsHelper.saveBluetoothAddress(getApplicationContext(), getMacAddressFieldText());
    }

}
