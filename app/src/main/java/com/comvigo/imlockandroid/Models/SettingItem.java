package com.comvigo.imlockandroid.Models;

/**
 * Created by Dmitry on 15.06.2015.
 */
public class SettingItem {
    private String isDefault;
    private String settingID;
    private String settingName;
    private String uploadDate;

    public SettingItem(){}

    public SettingItem(String isDefault, String settingID, String settingName, String uploadDate) {
        this.isDefault = isDefault;
        this.settingID = settingID;
        this.settingName = settingName;
        this.uploadDate = uploadDate;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getSettingID() {
        return settingID;
    }

    public void setSettingID(String settingID) {
        this.settingID = settingID;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "SettingItem{" +
                "isDefault='" + isDefault + '\'' +
                ", settingID='" + settingID + '\'' +
                ", settingName='" + settingName + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                '}';
    }
}
