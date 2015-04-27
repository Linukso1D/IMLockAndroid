package com.comvigo.imlockandroid;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dmitry on 27.04.2015.
 */
public class BlockService extends Service {

    private BroadcastReceiver mReceiver;
    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        boolean screenOff = intent.getBooleanExtra("screen_state",false);
        if (!screenOff){
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ActivityManager activityManager =
                            (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
                    for (int i=0;i!=processInfos.size();i++){
                        ActivityManager.RunningAppProcessInfo info = processInfos.get(i);
                    //    Log.d("List", String.valueOf(info.processName));
                        if(processInfos.get(i).processName.equals("com.android.chrome")||
                                processInfos.get(i).processName.equals("com.android.browser")||
                                processInfos.get(i).processName.equals("org.mozilla.firefox")){
                            ActivityManager.RunningTaskInfo foregrountTaskInfo = activityManager.getRunningTasks(1).get(0);
                            String foregroundTaskPackageName = foregrountTaskInfo.topActivity.getPackageName();
                            if(foregroundTaskPackageName.equals(processInfos.get(i).processName)){
                                startActivity(new Intent("android.intent.action.MAIN")
                                .addCategory("android.intent.category.HOME")
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                Message msg = handler.obtainMessage();
                                msg.arg1 = 1;
                                handler.sendMessage(msg);
                            }
                            activityManager.killBackgroundProcesses(info.processName);
                            Log.d("isRunning",processInfos.get(i).processName);
                        }
                    }
                }
            };
            timer.schedule(timerTask,0,10000);
        }else{
            timer.cancel();
        }
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1)
                Toast.makeText(getApplicationContext(),"Site blocked", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}