package com.comvigo.imlockandroid;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.comvigo.imlockandroid.Models.SettingItem;
import com.google.common.io.BaseEncoding;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by Dmitry on 19.05.2015.
 */
public class Connection extends ActionBarActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String LOGIN = "http://imlockusers.blockinternet.net/Service1.svc?wsdl";
    private final String SETTINGS = "http://webservice.blockinternet.net/Service1.svc?wsdl";

    public String validateUser(String login, String pass) {
        String str = "";
        try {
            str = new ValidateUser().execute(login, pass).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getUser(String login, String comuterID) {
        String userID = "";
        try {
            userID = String.valueOf(new GetUser().execute(login, comuterID).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userID;
    }

    public String makeforThisComputer(String userID, String comuterID) {
        try {
            userID = String.valueOf(new MakeforThisComputer().execute(userID, comuterID).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userID;
    }

    public ArrayList getSettingsList(String userID) {
        try {
            String settings = new GetSettingsList().execute(userID).get();
            String settingsUtf8 = settings.replace("utf-16", "utf-8");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(settingsUtf8.getBytes("utf-8"))));
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("SettingItem");
            ArrayList<SettingItem> settingsList = new ArrayList<SettingItem>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    SettingItem settingItem = new SettingItem();
                    Element eElement = (Element) nNode;
                    settingItem.setSettingID(eElement.getElementsByTagName("SettingID").item(0).getTextContent());
                    settingItem.setSettingName(eElement.getElementsByTagName("SettingName").item(0).getTextContent());
                    settingItem.setUploadDate(eElement.getElementsByTagName("UploadDate").item(0).getTextContent());
                    settingItem.setIsDefault(eElement.getElementsByTagName("IsDefault").item(0).getTextContent());
                    settingItem.toString();
                    settingsList.add(settingItem);
                }
            }
            return settingsList;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public void getSettings(String userID, String settingsID) {
        try {
           new GetSettings().execute(userID, settingsID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDefaultSettingsForUser(String userID,String computerID) {
        try {
            new GetDefaultSettingsForUser().execute(userID, computerID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ValidateUser
     */
    private class ValidateUser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
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
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    /**
     * GetUser
     */
    private class GetUser extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String serverResult = "0";
            SoapObject request = new SoapObject(NAMESPACE, "GetUser");
            request.addProperty("username", params[0]);
            request.addProperty("computerID", params[1]);
            request.addProperty("token", "imlu$$$$");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(LOGIN);
            try {
                transportSE.call("http://tempuri.org/IService1/GetUser", envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                serverResult = String.valueOf(response.getProperty("userIDField"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /**
     * GetSettingsList
     */
    private class GetSettingsList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String serverResult = "0";
            SoapObject request = new SoapObject(NAMESPACE, "GetAllSettingsByUserID");
            request.addProperty("Userid", params[0]);
            request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE("http://webservice.blockinternet.net/ServiceforAndroid.svc");
            try {
                transportSE.call("http://tempuri.org/IServiceforAndroid/GetAllSettingsByUserID", envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                serverResult = response.toString();
                Log.d("GetAllSettingsByUserID",serverResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResult;
        }
    }

    /**
     * GetSettings
     */
    private class GetSettings extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, "GetSettings");
            request.addProperty("UserID", params[0]);
            request.addProperty("SettingID", params[1]);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * MakeforThisComputer
     */
    private class MakeforThisComputer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String serverResult = "0";
            SoapObject request = new SoapObject(NAMESPACE, "MakeforThisComputer");
            Log.d("MakeforThisComputer", params[0] + params[1]);
            request.addProperty("settingid", "1");
            request.addProperty("userid", params[0]);
            request.addProperty("computerid", params[1]);
            request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(SETTINGS);
            try {
                transportSE.call("http://tempuri.org/IService1/MakeforThisComputer", envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                serverResult = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * GetDefaultSettingsForUser
     */
    private class GetDefaultSettingsForUser extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, "GetDefaultSettingsForUser");
            request.addProperty("userid", params[0]);
            request.addProperty("computerid", params[1]);
            request.addProperty("token", "Anonymous~XML!for@lock#IM!!");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(SETTINGS);
            try {
                transportSE.call("http://tempuri.org/IService1/GetDefaultSettingsForUser", envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                byte[] decodedPhraseAsBytes = BaseEncoding.base64().decode(
                        String.valueOf(response.getProperty("lockData")));
                writeXML(decodedPhraseAsBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void writeXML(byte[] decodedPhraseAsBytes) {
        Log.d("DAO:", "writeXML");
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(
                    new ByteArrayInputStream(decodedPhraseAsBytes));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                File newFile = new File (Environment.getExternalStorageDirectory() + "/IMLock/IMLockData.txt");
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

}
