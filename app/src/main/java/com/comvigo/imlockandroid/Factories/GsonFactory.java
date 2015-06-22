package com.comvigo.imlockandroid.Factories;

import com.google.gson.Gson;

/**
 * Created by Dmitry on 15.06.2015.
 */
public class GsonFactory {
    private static final Gson gson = new Gson();

    public static Gson getGsonInstance() {
        return gson;
    }
}