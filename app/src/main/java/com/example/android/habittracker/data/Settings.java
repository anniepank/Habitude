package com.example.android.habittracker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.LinkedList;

/**
 * Created by anya on 11/6/16.
 */
public class Settings {
    public static Settings global;
    public LinkedList<Habit> habits;
    public static final String NAME = "Habits";
    public static final String KEY = "Settings";

    public void save(Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        SharedPreferences prefs = context.getSharedPreferences(NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, json);
        Log.i("habits", json);
        editor.commit();
    }

    public static Settings load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NAME, 0);
        String json = prefs.getString(KEY, null);
        if(json == null) {
            Settings settings = new Settings();
            settings.habits = new LinkedList<>();
            return settings;
        }
        Settings settings = new Gson().fromJson(json, Settings.class);
        return settings;
    }

}
