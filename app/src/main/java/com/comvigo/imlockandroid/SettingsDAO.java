package com.comvigo.imlockandroid;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Dmitry on 15.06.2015.
 */
public class SettingsDAO {

    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Context context;

    public SettingsDAO(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(APP_PREFERENCES_SETTINGS, context.MODE_PRIVATE);
        mSettings = context.getSharedPreferences(APP_PREFERENCES_SETTINGS, context.MODE_PRIVATE);
        editor = mSettings.edit();
    }

    public String getUserID() {
        String userID = mSettings.getString("userID", "");
        return userID;
    }

    public String getComuterID() {
        String comuterID = mSettings.getString("comuterID", "");
        return comuterID;
    }

    public String getSettingsID() {
        String settingsID = mSettings.getString("settingsID", "");
        return settingsID;
    }

    public String getRedirectURL() {
        String RedirectURL = mSettings.getString("RedirectURL", "");
        return RedirectURL;
    }

    public String getIsShowNotification() {
        String IsShowNotification = mSettings.getString("IsShowNotification", "");
        return IsShowNotification;
    }

    public String getNotificationMessage1() {
        String NotificationMessage1 = mSettings.getString("NotificationMessage1", "");
        return NotificationMessage1;
    }

    public String getBlockAllOthers() {
        String blockAllOthers = mSettings.getString("blockAllOthers", "");
        return blockAllOthers;
    }


    public void setUserID(String userID) {
        editor.putString("userID", userID);
        editor.commit();
    }

    public void setComuterID(String comuterID) {
        editor.putString("comuterID", comuterID);
        editor.commit();
    }

    public void setSettingsID(String settingsID) {
        editor.putString("settingsID", settingsID);
        editor.commit();
    }

    public void setBlockAllOthers(String blockAllOthers) {
        editor.putString("blockAllOthers", blockAllOthers);
        editor.commit();
    }

    public void setRedirectURL(String redirectURL) {
        editor.putString("RedirectURL", redirectURL);
        editor.commit();
    }

    public void setIsShowNotification(String isShowNotification) {
        editor.putString("IsShowNotification", isShowNotification);
        editor.commit();
    }

    public void setNotificationMessage1(String notificationMessage1) {
        editor.putString("NotificationMessage1", notificationMessage1);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();

    }

}
