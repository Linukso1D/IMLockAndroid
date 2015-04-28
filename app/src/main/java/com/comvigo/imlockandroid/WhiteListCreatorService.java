package com.comvigo.imlockandroid;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * Created by Влад on 28.04.2015.
 */
public class WhiteListCreatorService extends Service {

    public static final String APP_PREFERENCES = "WhiteList";
    public static final String APP_PREFERENCES_NAME = "";
    SharedPreferences mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, getApplicationContext().MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("https://www.yahoo.com/", APP_PREFERENCES_NAME);
        editor.putString("https://www.gogle.com/", APP_PREFERENCES_NAME);
        editor.apply();
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
