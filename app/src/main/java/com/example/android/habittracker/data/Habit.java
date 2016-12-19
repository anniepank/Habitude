package com.example.android.habittracker.data;

import com.example.android.habittracker.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anya on 11/6/16.
 */

public class Habit {
    public String habitName;
    public List<Long> days;
    public String type = "Other";
    public final static HashMap<String, HabitType> namesAndImages = new HashMap<>();

    static {
        namesAndImages.put("other", new HabitType("Other", R.drawable.cat));
        namesAndImages.put("sport", new HabitType("Sport", R.drawable.cat));
    }
    public void toggleDay(long day){
        if(days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }
    }
}
