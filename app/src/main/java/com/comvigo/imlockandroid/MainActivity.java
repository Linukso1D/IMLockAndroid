package com.comvigo.imlockandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comvigo.imlockandroid.Services.BlockService;
import com.comvigo.imlockandroid.Services.WhiteListCreatorService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends ActionBarActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://imlockusers.blockinternet.net/Service1.svc?wsdl";
    private final String SOAP_ACTION = "http://tempuri.org/IService1/ValidateUser";
    private final String METHOD_NAME = "ValidateUser";

    Button send;
    EditText login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BlockService.class));
        startService(new Intent(this, WhiteListCreatorService.class));

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncCallWS task = new AsyncCallWS();
                String result = String.valueOf(task.execute(login.getText().toString(), password.getText().toString()));
            }
        });
    }

    public String validateUser(String login, String password) {
        String serverResult = "0";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("userName", login);
        request.addProperty("password", password);
        request.addProperty("token", "imlu$$$$");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL);
        try{
            transportSE.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            serverResult =response.toString();
            Log.d("RESPONSE",serverResult);
        }catch (Exception e){
            e.printStackTrace();
        }
        return serverResult;
    }

    private class AsyncCallWS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = validateUser(params[0], params[1]);
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            switch (result){
                case "-1": Toast.makeText(getApplication(), "Wrong login or password" , Toast.LENGTH_LONG).show();
                    break;
                case "0": Toast.makeText(getApplication(), "Server connection error" , Toast.LENGTH_LONG).show();
                    break;
                case "1": Toast.makeText(getApplication(), "Success" , Toast.LENGTH_LONG).show();
                    break;
            }
            super.onPostExecute(result);
        }
    }

}