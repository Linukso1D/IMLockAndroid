package com.comvigo.imlockandroid.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.comvigo.imlockandroid.DAO;
import com.comvigo.imlockandroid.ParseXML;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dmitry on 28.04.2015.
 */
public class WhiteListCreatorService extends Service {

    Timer timer;

    public static final String APP_PREFERENCES_WHITE = "WhiteList";
    public static final String APP_PREFERENCES_BLACK = "BlackList";
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    public static final String APP_PREFERENCES_NAME = "";
    SharedPreferences mSettingsBlack, mSettingsWhite, mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //Create user settings file
        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.putString("settingsID", "6333");
        editor.putString("userID", intent.getStringExtra("userID"));
        editor.putString("comuterID", intent.getStringExtra("comuterID"));
        editor.apply();
        Log.d("COMPID", mSettings.getString("comuterID", ""));

//        new DAO().getSettings(mSettings.getString("userID", ""), mSettings.getString("settingsID", ""));
//        new DAO().makeforThisComputer(mSettings.getString("comuterID", ""));

        DAO dao = new DAO();
        dao.getDefaultSettingsForUser(mSettings.getString("userID", ""), mSettings.getString("comuterID", ""));
        dao.makeforThisComputer(mSettings.getString("userID", ""), mSettings.getString("comuterID", ""));
        dao.getSettingsList(mSettings.getString("userID", ""));

        timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            boolean isRunning = false;


            @Override
            public void run() {

                Log.d("WhiteListCreatorService","timerTask");

                //Get black and white lists

                ParseXML parseXML = new ParseXML();
                List<String> block = parseXML.getBlockList();
                List<String> white = parseXML.getWhiteList();

                //getSettings
                SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
                mSettings = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = mSettings.edit();
                try {
                    List<String> other = parseXML.stringList();
                    editor.clear();
                    editor.commit();
                    editor.putString("blockAllOthers", other.get(0));
                    editor.putString("RedirectURL", other.get(1));
                    editor.putString("IsShowNotification", other.get(2));
                    editor.putString("NotificationMessage1", other.get(3));
                    editor.apply();
                }catch (Exception e){
                    e.printStackTrace();
                }

                //Create blacklist file
                SharedPreferences blackSharedPreferences = getSharedPreferences(APP_PREFERENCES_BLACK, getApplicationContext().MODE_PRIVATE);
                mSettingsBlack = getSharedPreferences(APP_PREFERENCES_BLACK, getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editorBlack = mSettingsBlack.edit();
                editorBlack.clear();
                editorBlack.commit();
                for (int i = 0; i < block.size(); i++) {
                    editorBlack.putString(block.get(i), APP_PREFERENCES_NAME);
                }
                editorBlack.apply();

                //create whitelist file
                SharedPreferences whiteSharedPreferences = getSharedPreferences(APP_PREFERENCES_WHITE, getApplicationContext().MODE_PRIVATE);
                mSettingsWhite = getSharedPreferences(APP_PREFERENCES_WHITE, getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editorWhite = mSettingsWhite.edit();
                editorWhite.clear();
                editorWhite.commit();
                for (int i = 0; i < white.size(); i++) {
                    editorWhite.putString(white.get(i), APP_PREFERENCES_NAME);
                }
                editorWhite.apply();

                //check if BlockService is running
                final ActivityManager activityManager =
                        (ActivityManager) getApplicationContext().getSystemService(getApplicationContext().ACTIVITY_SERVICE);
                final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
                for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
                    if (runningServiceInfo.service.getClassName().equals("com.comvigo.imlockandroid.Services.BlockService")) {
                        isRunning = true;
                    }
                }
                if (!isRunning) {
                    startService(new Intent(getApplicationContext(), BlockService.class));
                }
            }
        };
        timer.schedule(timerTask, 0, 60000);
        return START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}