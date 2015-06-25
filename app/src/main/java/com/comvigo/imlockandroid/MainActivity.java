package com.comvigo.imlockandroid;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.comvigo.imlockandroid.Fragments.LoginFragment;
import com.comvigo.imlockandroid.Fragments.SettingsFragment;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        SettingsDAO settingsDAO = new SettingsDAO(getApplicationContext());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (!settingsDAO.getUserID().equals("")){
            fragmentTransaction.add(R.id.activity_container, new SettingsFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        }else {
            fragmentTransaction.add(R.id.activity_container, new LoginFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }
}