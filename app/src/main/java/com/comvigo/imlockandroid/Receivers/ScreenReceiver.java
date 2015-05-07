package com.comvigo.imlockandroid.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.comvigo.imlockandroid.Services.BlockService;

/**
 * Created by Dmitry on 27.04.2015.
 */
public class ScreenReceiver extends BroadcastReceiver {

    private boolean screenOff = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            screenOff =false;
            Log.d("screenState","ON");
        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            screenOff=true;
            Log.d("screenState","OFF");
        }
        Intent i = new Intent(context, BlockService.class);
        i.putExtra("screen_state",screenOff);
        context.startService(i);
    }
}
