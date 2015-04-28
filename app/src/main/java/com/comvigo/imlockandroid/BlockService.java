package com.comvigo.imlockandroid;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Browser;
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
    boolean screenOff;
    public static final String APP_PREFERENCES = "WhiteList";

    SharedPreferences mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, getApplicationContext().MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES, getApplicationContext().MODE_PRIVATE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);


        try {
            screenOff = intent.getBooleanExtra("screen_state", false);
        } catch (Exception e) {
            screenOff = false;
        }
        if (!screenOff) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ActivityManager activityManager =
                            (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
                    for (int i = 0; i != processInfos.size(); i++) {
                        ActivityManager.RunningAppProcessInfo info = processInfos.get(i);
                        if (processInfos.get(i).processName.equals("com.android.chrome") ||
                                processInfos.get(i).processName.equals("com.android.browser")||
                                processInfos.get(i).processName.equals("org.mozilla.firefox")||
                                processInfos.get(i).processName.equals("mobi.mgeek.TunnyBrowse")||
                                processInfos.get(i).processName.contains("com.opera.browser")) {
                            ActivityManager.RunningTaskInfo foregrountTaskInfo = activityManager.getRunningTasks(1).get(0);
                            String foregroundTaskPackageName = foregrountTaskInfo.topActivity.getPackageName();
                            //if user have opened default browser or chrome - we take url
                            if (foregroundTaskPackageName.equals(processInfos.get(i).processName)) {
                                if (foregroundTaskPackageName.equals("com.android.chrome") ||
                                        foregroundTaskPackageName.equals("com.android.browser")) {
                                    //check the white list
                                   if(mSettings.contains(getUrl(processInfos.get(i).processName))) {
                                       //change on ! later
                                       Log.d("congratulation, site in white list", getUrl(processInfos.get(i).processName));
                                   }else {
                                       //open new tab in chrome
                                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://yahoo.com"));
                                       intent.setPackage("com.android.chrome");
                                       intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, "com.android.chrome");
                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(intent);
                                   }
                                } else {
                                    // if user have opened other browser - go to launcher
                                    startActivity(new Intent("android.intent.action.MAIN")
                                            .addCategory("android.intent.category.HOME")
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    // kill background process of browser
                                    activityManager.killBackgroundProcesses(info.processName);
                                    // send a message to user
                                    Message msg = handler.obtainMessage();
                                    msg.arg1 = 1;
                                    handler.sendMessage(msg);
                                }
                            }
                            Log.d("isRunning", processInfos.get(i).processName);
                        }
                    }
                }
            };
            timer.schedule(timerTask, 0, 10000);
        } else {
            timer.cancel();
        }
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1)
                Toast.makeText(getApplicationContext(), "Site blocked", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Get last url from browser bookmark history
     * @param browser
     * @return
     */
    private String getUrl(String browser) {
        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
        String url = "";
        Cursor mCur;
        Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
        if (browser.equals("com.android.chrome")) {
            mCur = getContentResolver().query(uriCustom, proj, sel, null, Browser.BookmarkColumns.DATE + " ASC");
        } else {
            mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj, sel, null, null);
        }
        mCur.moveToFirst();
        mCur.moveToLast();
        url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
        Log.i("URL = ", url);
        return url;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}