package com.comvigo.imlockandroid.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Browser;
import android.util.Log;
import android.widget.Toast;

import com.comvigo.imlockandroid.Receivers.InternetReceiver;
import com.comvigo.imlockandroid.Receivers.ScreenReceiver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dmitry on 27.04.2015.
 */
public class BlockService extends Service {

    private BroadcastReceiver mScreenReceiver;
    private BroadcastReceiver mInternetReceiver;
    private Timer timer;
    boolean screenOff;
    public static final String APP_PREFERENCES_WHITE = "WhiteList";
    public static final String APP_PREFERENCES_BLACK = "BlackList";
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    boolean hasInternet = false;
    SharedPreferences mSettingsBlack, mSettingsWhite, mSettings;
    List<String> browsers;

    @Override
    public void onCreate() {
        super.onCreate();
        //create screen receiver
        browsers = Arrays.asList("com.android.chrome", "com.android.browser",
                "org.mozilla.firefox", "mobi.mgeek.TunnyBrowser", "com.opera.browser:main");
        IntentFilter screenFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenReceiver = new ScreenReceiver();
        registerReceiver(mScreenReceiver, screenFilter);
        //create internet connection receiver
        IntentFilter internetFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mInternetReceiver = new InternetReceiver();
        registerReceiver(mInternetReceiver, internetFilter);
        //read shared preferences
        SharedPreferences whiteSharedPreferences = getSharedPreferences(APP_PREFERENCES_WHITE, getApplicationContext().MODE_PRIVATE);
        mSettingsWhite = getSharedPreferences(APP_PREFERENCES_WHITE, getApplicationContext().MODE_PRIVATE);
        SharedPreferences blackSharedPreferences = getSharedPreferences(APP_PREFERENCES_BLACK, getApplicationContext().MODE_PRIVATE);
        mSettingsBlack = getSharedPreferences(APP_PREFERENCES_BLACK, getApplicationContext().MODE_PRIVATE);
        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES_SETTINGS, getApplicationContext().MODE_PRIVATE);
        //first check internet connection
        ConnectivityManager CManager =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {
            hasInternet = true;
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        try {
            screenOff = intent.getBooleanExtra("screen_state", false);
            hasInternet = intent.getBooleanExtra("internet_state", true);
        } catch (Exception e) {
            screenOff = false;
            hasInternet = true;
        }
        if (!screenOff && hasInternet) {
            timer = new Timer();
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ActivityManager activityManager =
                            (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
                    for (int i = 0; i != processInfos.size(); i++) {
                        ActivityManager.RunningAppProcessInfo info = processInfos.get(i);
                        ActivityManager.RunningTaskInfo foregrountTaskInfo = activityManager.getRunningTasks(1).get(0);
                        String foregroundTaskPackageName;
                        if (Build.VERSION.SDK_INT >= 21) {
                            foregroundTaskPackageName = processInfos.get(0).processName;
                        } else {
                            foregroundTaskPackageName = foregrountTaskInfo.topActivity.getPackageName();
                        }
                        if (browsers.contains(foregroundTaskPackageName)) {
                            //if user have opened default browser or chrome - we take url
                            if (foregroundTaskPackageName.equals(processInfos.get(i).processName) ||
                                    foregroundTaskPackageName.equals("com.opera.browser")) {
                                if (foregroundTaskPackageName.equals("com.android.chrome") ||
                                        foregroundTaskPackageName.equals("com.android.browser")) {
                                    //check the white list
                                    String openedUrl;
                                    try {
                                        openedUrl = getUrl(processInfos.get(i).processName);
                                    }catch (Exception e){
                                        openedUrl = "null";
                                    }
                                    Log.d("openedUrl", openedUrl);
                                    if (!comparator(openedUrl)) {

                                        //get default browser
                                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
                                        ResolveInfo resolveInfo = getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
                                        String defaultBrowser = resolveInfo.activityInfo.packageName;

                                        //Set site for redirect
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://comvigo.com"));
                                        Browser.deleteFromHistory(getContentResolver(), openedUrl);

                                        //open new tab on default browser
                                        if (defaultBrowser.equals("com.android.chrome") || defaultBrowser.equals("com.android.browser")) {
                                            intent.setPackage(defaultBrowser);
                                        } else {
                                            intent.setPackage("com.android.chrome");
                                        }
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        // send a message to user
                                        Message msg = handler.obtainMessage();
                                        msg.arg1 = 1;
                                        handler.sendMessage(msg);
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
                        }
                    }
                }
            };
            timer.schedule(timerTask, 0, 1000);
        } else {
            timer.cancel();
        }
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (mSettings.getString("IsShowNotification","").equals("true"))
                Toast.makeText(getApplicationContext(), mSettings.getString("NotificationMessage1",""), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Get last url from browser bookmark history
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
            mCur.moveToFirst();
            while (mCur.isAfterLast() == false) {
                url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                mCur.moveToNext();
            }
        }
        mCur.moveToFirst();
        mCur.moveToLast();
        url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
        Log.d("URL", url);
        mCur.close();

        return url;
    }

    /**
     * Check if url's domain name in white list
     */
    private boolean comparator(String url) {
        Map white = mSettingsWhite.getAll();
        Map black = mSettingsBlack.getAll();
        for (Object key : white.keySet()) {
            Object value;
            value = key.toString().replaceAll("https://", "");
            value = key.toString().replaceAll("http://", "");
            value = value.toString().replaceAll("www.", "");
            if (url.contains(value.toString())) {
                return true;
            }
        }
        for (Object key : black.keySet()) {
            Object value;
            value = key.toString().replaceAll("https://", "");
            value = key.toString().replaceAll("http://", "");
            value = value.toString().replaceAll("www.", "");
            if (url.contains(value.toString())) {
                return false;
            }
        }
        if (mSettings.getString("blockAllOthers", "").equals("true")) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
        unregisterReceiver(mInternetReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}