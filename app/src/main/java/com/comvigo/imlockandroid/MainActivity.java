package com.comvigo.imlockandroid;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.comvigo.imlockandroid.Fragments.LoginFragment;
import com.comvigo.imlockandroid.Fragments.SettingsFragment;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Log.d("userID", mSettings.getString("userID",""));
        if (!mSettings.getString("userID","").equals("")){
            fragmentTransaction.add(R.id.activity_container, new SettingsFragment());
            fragmentTransaction.commit();
        }else {
            fragmentTransaction.add(R.id.activity_container, new LoginFragment());
            fragmentTransaction.commit();
        }
    }

}