package com.comvigo.imlockandroid.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.comvigo.imlockandroid.DAO;
import com.comvigo.imlockandroid.ParseXML;

import java.util.List;

/**
 * Created by Dmitry on 28.04.2015.
 */
public class WhiteListCreatorService extends Service {

    public static final String APP_PREFERENCES_WHITE = "WhiteList";
    public static final String APP_PREFERENCES_BLACK = "BlackList";
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    public static final String APP_PREFERENCES_NAME = "";
    SharedPreferences mSettingsBlack, mSettingsWhite ,mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //Create user settings file
        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("settingsID","6333");
        editor.putString("userID", intent.getStringExtra("userID"));
        editor.putString("comuterID", intent.getStringExtra("comuterID"));
        editor.apply();

        Log.d("eee", mSettings.getString("comuterID", ""));

        //Get black and white lists
        new DAO().getSettings(mSettings.getString("userID", ""), mSettings.getString("settingsID", ""));
        new DAO().makeforThisComputer(mSettings.getString("comuterID", ""));
        new DAO().getSettingsList();
        new DAO().getDefaultSettingsForUser(mSettings.getString("comuterID", ""));
        List<String> block = new ParseXML().getBlockList();
        List<String> white = new ParseXML().getWhiteList();

        //Create blacklist file
        SharedPreferences blackSharedPreferences = getSharedPreferences(APP_PREFERENCES_BLACK, getApplicationContext().MODE_PRIVATE);
        mSettingsBlack = getSharedPreferences(APP_PREFERENCES_BLACK, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editorBlack = mSettingsBlack.edit();
        editorBlack.clear();
        for (int i = 0; i<block.size();i++){
            editorBlack.putString(block.get(i),APP_PREFERENCES_NAME);
        }
        editorBlack.apply();

        //create whitelist file
        SharedPreferences whiteSharedPreferences = getSharedPreferences(APP_PREFERENCES_WHITE, getApplicationContext().MODE_PRIVATE);
        mSettingsWhite = getSharedPreferences(APP_PREFERENCES_WHITE, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editorWhite = mSettingsWhite.edit();
        editorWhite.clear();
        for (int i = 0; i<white.size();i++){
            editorWhite.putString(white.get(i),APP_PREFERENCES_NAME);
        }
        editorWhite.apply();
        return START_REDELIVER_INTENT;
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
