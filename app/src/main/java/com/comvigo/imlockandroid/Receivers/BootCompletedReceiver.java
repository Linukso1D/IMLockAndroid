package com.comvigo.imlockandroid.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.comvigo.imlockandroid.Services.BlockService;
import com.comvigo.imlockandroid.Services.WhiteListCreatorService;

/**
 * Created by Dmitry on 28.04.2015.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    final static String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.w(TAG, "starting service...");
        context.startService(new Intent(context, BlockService.class));
        context.startService(new Intent(context, WhiteListCreatorService.class));
    }
}
