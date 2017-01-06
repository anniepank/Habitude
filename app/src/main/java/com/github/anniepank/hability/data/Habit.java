package com.github.anniepank.hability.data;

import com.github.anniepank.hability.R;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by anya on 11/6/16.
 */

public class Habit {
    public String habitName;
    public List<Long> days;
    public String type = "other";
    public final static LinkedHashMap<String, HabitType> namesAndImages = new LinkedHashMap<>();
    public boolean[] remindDays = {false, false, false, false, false, false, false};
    public boolean remind;
    public long reminderHours;
    public long reminderMinutes;

    static {
        namesAndImages.put("other", new HabitType("Other", R.drawable.other));
        namesAndImages.put("sport", new HabitType("Sport", R.drawable.sport));
        namesAndImages.put("nutrition", new HabitType("Nutrition", R.drawable.nutrition));
        namesAndImages.put("jogging", new HabitType("Jogging", R.drawable.jogging));
        namesAndImages.put("meditation", new HabitType("Meditation", R.drawable.meditation));
    }

    public void toggleDay(long day) {
        if (days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }
    }
}
