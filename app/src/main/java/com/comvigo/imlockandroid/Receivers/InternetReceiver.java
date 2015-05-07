package com.comvigo.imlockandroid.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.comvigo.imlockandroid.Services.BlockService;

/**
 * Created by Dmitry on 29.04.2015.
 */
public class InternetReceiver extends BroadcastReceiver{

    private boolean hasInternet;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager CManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {
            hasInternet = true;
        }else{
            hasInternet = false;
        }
        Intent i = new Intent(context, BlockService.class);
        i.putExtra("internet_state",hasInternet);
        context.startService(i);
    }
}
