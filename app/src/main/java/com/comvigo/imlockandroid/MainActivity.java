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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;


public class MainActivity extends ActionBarActivity {


    Button send;
    EditText login, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DAO dao = new DAO();
                Log.d("str", dao.validateUser(String.valueOf(login.getText()), String.valueOf(password.getText())));
//            switch (result) {
//                case "-1":
//                    Toast.makeText(getApplication(), "Wrong login or password", Toast.LENGTH_LONG).show();
//                    break;
//                case "0":
//                    Toast.makeText(getApplication(), "Server connection error", Toast.LENGTH_LONG).show();
//                    break;
//                case "1":
//                    AsyncCallGetUser getUser = new AsyncCallGetUser();
//                    getUser.execute(login.getText().toString());
//                    startService(new Intent(getApplicationContext(), BlockService.class));
//                    startService(new Intent(getApplicationContext(), WhiteListCreatorService.class));
//                    PackageManager p = getPackageManager();
//                    ComponentName componentName = new ComponentName(getApplication(),
//                            com.comvigo.imlockandroid.MainActivity.class);
//                    p.setComponentEnabledSetting(componentName,
//                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//                    finish();
//                    break;
//          }
            }
        });
    }


}