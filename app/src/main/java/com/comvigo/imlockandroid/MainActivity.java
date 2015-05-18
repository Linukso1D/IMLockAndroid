package com.comvigo.imlockandroid;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
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
import com.google.common.io.BaseEncoding;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.*;


public class MainActivity extends ActionBarActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://imlockusers.blockinternet.net/Service1.svc?wsdl";
    private final String URL1 = "http://webservice.blockinternet.net/Service1.svc?wsdl";

    Button send;
    EditText login, password;
    final int PROGRESS_DLG_ID = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                AsyncCallValidateUser task = new AsyncCallValidateUser();
//                task.execute(login.getText().toString(), password.getText().toString());

                AsyncCallGetSettings task1 = new AsyncCallGetSettings();
                task1.execute();

            }
        });
    }

    public String validateUser(String login, String password) {
        String serverResult = "0";
        SoapObject request = new SoapObject(NAMESPACE, "ValidateUser");
        request.addProperty("userName", login);
        request.addProperty("password", password);
        request.addProperty("token", "imlu$$$$");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL);
        try {
            transportSE.call("http://tempuri.org/IService1/ValidateUser", envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            serverResult = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResult;
    }

    public void getUser(String login) {
        String serverResult = "0";
        SoapObject request = new SoapObject(NAMESPACE, "GetUser");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        String model = android.os.Build.MODEL;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");
        String formattedDate = df.format(c.getTime());
        String comuterID = formattedDate + model.replaceAll(" ", "") + imei;
        request.addProperty("username", login);
        request.addProperty("computerID", comuterID);
        request.addProperty("token", "imlu$$$$");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL);
        try {
            transportSE.call("http://tempuri.org/IService1/GetUser", envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            serverResult = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("getUser", serverResult);
    }

    public void getSettingsList() {
        String serverResult = "0";
        SoapObject request = new SoapObject(NAMESPACE, "GetAllSettingsByUserID");
        request.addProperty("Userid", "101678");
        request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL1);
        try {
            transportSE.call("http://tempuri.org/IService1/GetAllSettingsByUserID", envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            serverResult = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("!!!", serverResult);
    }

    public void getSettings() {
        String serverResult = "0";
        SoapObject request = new SoapObject(NAMESPACE, "GetSettings");
        request.addProperty("UserID", "101678");
        request.addProperty("SettingID", "6333");
        request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL1);
        try {
            transportSE.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            transportSE.call("http://tempuri.org/IService1/GetSettings", envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Log.d("lockData", String.valueOf(response.getProperty("lockData")));
            serverResult = response.toString();

            //write response to file
            PrintWriter writer = new PrintWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My Documents/11.txt", "UTF-8");
            writer.println(response.getProperty("lockData"));
            writer.close();

            byte[] decodedPhraseAsBytes = BaseEncoding.base64().decode(String.valueOf(response.getProperty("lockData")));

            //uzip string
            byte[] buffer = new byte[1024];
            ZipInputStream zis =
                    new ZipInputStream(
                           new ByteArrayInputStream(decodedPhraseAsBytes));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = new String(ze.getName().getBytes("UTF-8"));
                Log.d("filename", fileName);
                File newFile =
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fuck1.txt");
                System.out.println("file unzip : " + newFile.getAbsoluteFile());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("!!!", serverResult);
    }

    @Override
    protected Dialog onCreateDialog(int dialogId) {
        ProgressDialog progress = null;
        switch (dialogId) {
            case PROGRESS_DLG_ID:
                progress = new ProgressDialog(this);
                progress.setMessage("Loading...");

                break;
        }
        return progress;
    }

    private class AsyncCallValidateUser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            publishProgress(new String[]{});
            String result = validateUser(params[0], params[1]);
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            showDialog(PROGRESS_DLG_ID);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissDialog(PROGRESS_DLG_ID);
            switch (result) {
                case "-1":
                    Toast.makeText(getApplication(), "Wrong login or password", Toast.LENGTH_LONG).show();
                    break;
                case "0":
                    Toast.makeText(getApplication(), "Server connection error", Toast.LENGTH_LONG).show();
                    break;
                case "1":
                    AsyncCallGetUser getUser = new AsyncCallGetUser();
                    getUser.execute(login.getText().toString());
                    startService(new Intent(getApplicationContext(), BlockService.class));
                    startService(new Intent(getApplicationContext(), WhiteListCreatorService.class));
                    PackageManager p = getPackageManager();
                    ComponentName componentName = new ComponentName(getApplication(), com.comvigo.imlockandroid.MainActivity.class);
                    p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    finish();
                    break;
            }
        }
    }

    private class AsyncCallGetUser extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getUser(params[0]);
            return null;
        }
    }

    private class AsyncCallGetSettings extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getSettings();
            return null;
        }
    }

}