package com.github.anniepank.hability.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.anniepank.hability.Reminder;
import com.google.gson.Gson;

import java.util.LinkedList;

/**
 * Created by anya on 11/6/16.
 */
public class Settings {
    public LinkedList<Habit> habits;
    public String cachedImageOfTheDayUrl;
    public String syncKey;

    private static final String NAME = "Habits";
    private static final String KEY = "Settings";
    private static boolean gotSettings = false;
    private static Settings global;

    public static Settings get(Context context) {
        if (!gotSettings) {
            global = load(context);
            gotSettings = true;
        }
        return global;
    }

    public void save(Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        SharedPreferences prefs = context.getSharedPreferences(NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, json);
        Log.i("habits", json);
        editor.apply();
        Reminder.scheduleNotifications(context);
    }

    private static Settings load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NAME, 0);
        String json = prefs.getString(KEY, null);
        if (json == null) {
            Settings settings = new Settings();
            settings.habits = new LinkedList<>();
            return settings;
        }
        return new Gson().fromJson(json, Settings.class);
    }
}
