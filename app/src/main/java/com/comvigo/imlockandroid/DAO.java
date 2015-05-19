package com.comvigo.imlockandroid;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Dmitry on 19.05.2015.
 */
public class DAO extends ActionBarActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String LOGIN = "http://imlockusers.blockinternet.net/Service1.svc?wsdl";
    private final String SETTINGS = "http://webservice.blockinternet.net/Service1.svc?wsdl";
    final int PROGRESS_DLG_ID = 666;

    public String validateUser(String login, String pass){
        String str = "";
        try {
            str = new ValidateUser().execute(login, pass).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("str",str);
        return str;
    }

    public void getUser(String login) {
        try {
            new GetUser().execute(login).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSettingsList() {
        try {
            new GetSettingsList().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSettings() {
        try {
            new GetSettings().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected Dialog onCreateDialog(int dialogId) {
//        ProgressDialog progress = null;
//        switch (dialogId) {
//            case PROGRESS_DLG_ID:
//                progress = new ProgressDialog(getApplicationContext());
//                progress.setMessage("Loading...");
//
//                break;
//        }
//        return progress;
//    }

    /**
     * ValidateUser
     */
    private class ValidateUser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
//            publishProgress(new String[]{});
            String serverResult = "0";
            SoapObject request = new SoapObject(NAMESPACE, "ValidateUser");
            request.addProperty("userName", params[0]);
            request.addProperty("password", params[1]);
            request.addProperty("token", "imlu$$$$");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(LOGIN);
            try {
                transportSE.call("http://tempuri.org/IService1/ValidateUser", envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                serverResult = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResult;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            showDialog(PROGRESS_DLG_ID);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            removeDialog(PROGRESS_DLG_ID);
        }
    }

    /**
     * GetUser
     */
    private class GetUser extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String serverResult = "0";
            SoapObject request = new SoapObject(NAMESPACE, "GetUser");
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            String model = android.os.Build.MODEL;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");
            String formattedDate = df.format(c.getTime());
            String comuterID = formattedDate + model.replaceAll(" ", "") + imei;
            request.addProperty("username", params[0]);
            request.addProperty("computerID", comuterID);
            request.addProperty("token", "imlu$$$$");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(LOGIN);
            try {
                transportSE.call("http://tempuri.org/IService1/GetUser", envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                serverResult = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("getUser", serverResult);
            return null;
        }
    }

    /**
     * GetSettingsList
     */
    private class GetSettingsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String serverResult = "0";
            SoapObject request = new SoapObject(NAMESPACE, "GetAllSettingsByUserID");
            request.addProperty("Userid", "101678");
            request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(SETTINGS);
            try {
                transportSE.call("http://tempuri.org/IService1/GetAllSettingsByUserID", envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                serverResult = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("!!!", serverResult);
            return  null;
        }
    }

    /**
     * GetSettings
     */
    private class GetSettings extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, "GetSettings");
            request.addProperty("UserID", "101678");
            request.addProperty("SettingID", "6333");
            request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(SETTINGS);
            try {
                transportSE.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                transportSE.call("http://tempuri.org/IService1/GetSettings", envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                byte[] decodedPhraseAsBytes = BaseEncoding.base64().decode(
                        String.valueOf(response.getProperty("lockData")));
                writeXML(decodedPhraseAsBytes);
                parseXML();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FUCK";
        }

        private void parseXML() {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new FileInputStream(
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fuck1.xml")));
                NodeList entries = doc.getElementsByTagName("WhiteList");
                Element node1 = (Element) entries.item(0);
                NodeList name = node1.getElementsByTagName("Site");
                int num = name.getLength();
                for (int i = 0; i < num; i++) {
                    Element node = (Element) name.item(i);
                    listAllAttributes(node);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void writeXML(byte[] decodedPhraseAsBytes) {
            byte[] buffer = new byte[1024];
            try {
                ZipInputStream zis = new ZipInputStream(
                        new ByteArrayInputStream(decodedPhraseAsBytes));
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {
                    String fileName = new String(ze.getName().getBytes("UTF-8"));
                    Log.d("filename", fileName);
                    File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + fileName+".txt");
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
        }

        private void listAllAttributes(Element element) {
            System.out.println("List attributes for node: " + element.getNodeName());
            NamedNodeMap attributes = element.getAttributes();
            NodeList name = element.getElementsByTagName("description");
            int numAttrs = attributes.getLength();
            for (int i = 0; i < numAttrs; i++) {
                Attr attr = (Attr) attributes.item(i);
                String attrName = attr.getNodeName();
                String attrValue = attr.getNodeValue();
                String attrContent = attr.getValue();
                System.out.println("Found attribute: " + attrName + " with value: " + attrValue + " and content: " + name.item(0).getFirstChild().getTextContent());
            }
        }
    }

}