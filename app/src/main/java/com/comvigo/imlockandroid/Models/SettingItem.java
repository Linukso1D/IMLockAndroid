package com.comvigo.imlockandroid.Models;

/**
 * Created by Dmitry on 15.06.2015.
 */
public class SettingItem {
    private String SettingID;
    private String SettingName;
    private String UploadDate;
    private String IsDefault;

    public String getSettingID() {
        return SettingID;
    }

    public void setSettingID(String settingID) {
        SettingID = settingID;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) { SettingName = settingName; }

    public String getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }

    @Override
    public String toString() {
        return "SettingItem{" +
                "SettingID='" + SettingID + '\'' +
                ", SettingName='" + SettingName + '\'' +
                ", UploadDate='" + UploadDate + '\'' +
                ", IsDefault='" + IsDefault + '\'' +
                '}';
    }
}
