package com.comvigo.imlockandroid.Factories;

import com.comvigo.imlockandroid.SettingsDAO;

/**
 * Created by Dmitry on 15.06.2015.
 */
public class SettingsDAOFactory {
    private static final SettingsDAO settingsDAO = new SettingsDAO();

    public static SettingsDAO getSettingsDAOInstance() {
        return settingsDAO;
    }
}
