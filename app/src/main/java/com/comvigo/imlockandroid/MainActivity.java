package com.comvigo.imlockandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comvigo.imlockandroid.Services.BlockService;
import com.comvigo.imlockandroid.Services.WhiteListCreatorService;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    Button send;
    EditText login, password;
    String login_value, password_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login_value = String.valueOf(login.getText());
                password_value = String.valueOf(password.getText());
                DAO dao = new DAO();
                String result = dao.validateUser(login_value,password_value);
                switch (result) {
                    case "-1":
                        Toast.makeText(getApplication(), "Wrong login or password", Toast.LENGTH_LONG).show();
                        break;
                    case "0":
                        Toast.makeText(getApplication(), "Server connection error", Toast.LENGTH_LONG).show();
                        break;
                    case "1":
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String imei = telephonyManager.getDeviceId();
                        String model = android.os.Build.MODEL;
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");
                        String formattedDate = df.format(c.getTime());
                        String comuterID = formattedDate + model.replaceAll(" ", "") + imei;
                        String userID = dao.getUser(login_value, comuterID);
                        Log.d("userID", userID);
                        startService(new Intent(getApplicationContext(), BlockService.class));
                        Intent intent = new Intent(getApplicationContext(), WhiteListCreatorService.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("comuterID", comuterID);
                        startService(intent);
                        PackageManager p = getPackageManager();
                        ComponentName componentName = new ComponentName(getApplication(),
                                com.comvigo.imlockandroid.MainActivity.class);
                        p.setComponentEnabledSetting(componentName,
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        finish();
                        break;
                }
            }
        });
    }

}