package com.comvigo.imlockandroid.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.comvigo.imlockandroid.Connection;
import com.comvigo.imlockandroid.ParseXML;
import com.comvigo.imlockandroid.SettingsDAO;

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
    public static final String APP_PREFERENCES_NAME = "";
    SharedPreferences mSettingsBlack, mSettingsWhite;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        final SettingsDAO settingsDAO = new SettingsDAO(getApplicationContext());
        Connection connection = new Connection();
        connection.getSettings(settingsDAO.getUserID(),settingsDAO.getSettingsID());
        connection.makeforThisComputer(settingsDAO.getUserID(),settingsDAO.getComuterID());
        timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            boolean isRunning = false;

            @Override
            public void run() {

                //Get black and white lists
                ParseXML parseXML = new ParseXML();
                List<String> block = parseXML.getBlockList();
                List<String> white = parseXML.getWhiteList();
                try {
                    List<String> other = parseXML.stringList();
                    settingsDAO.setBlockAllOthers(other.get(0));
                    settingsDAO.setRedirectURL(other.get(1));
                    settingsDAO.setIsShowNotification(other.get(2));
                    settingsDAO.setNotificationMessage1(other.get(3));
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