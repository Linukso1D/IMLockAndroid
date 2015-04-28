package com.comvigo.imlockandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        setContentView(R.layout.activity_main);
        startService(new Intent(this,BlockService.class));
        Intent intent = this.getIntent();
        if("android.intent.action.VIEW".equals(intent.getAction())){
            Uri uri = intent.getData();
            Log.i("webSite", String.valueOf(uri));
        }
    }

}
