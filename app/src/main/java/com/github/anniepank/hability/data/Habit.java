package com.github.anniepank.hability.data;

import com.github.anniepank.hability.DateUtilities;
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
    public boolean[] remindDays = {false, false, false, false, false, false, false};
    public boolean remind;
    public int reminderHours;
    public int reminderMinutes;

    public final static LinkedHashMap<String, HabitType> namesAndImages = new LinkedHashMap<>();
    static {
        namesAndImages.put("other", new HabitType(R.string.type_other, R.drawable.other));
        namesAndImages.put("sport", new HabitType(R.string.type_sport, R.drawable.sport));
        namesAndImages.put("nutrition", new HabitType(R.string.type_nutrition, R.drawable.nutrition));
        namesAndImages.put("jogging", new HabitType(R.string.type_jogging, R.drawable.jogging));
        namesAndImages.put("meditation", new HabitType(R.string.type_meditiation, R.drawable.meditation));
        namesAndImages.put("workout", new HabitType(R.string.type_workout, R.drawable.workout));
        namesAndImages.put("reading", new HabitType(R.string.type_reading, R.drawable.reading));
    }

    public void toggleDay(long day) {
        if (days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }
    }

    public void addDay(long day) {
        if (!days.contains(day)) {
            days.add(day);
        }
    }

    public int getStreak() {
        int streak = 0;
        if (days.contains(DateUtilities.getToday())) {
            streak = 1;
        }
        long k = 1;
        while (days.contains(DateUtilities.getToday() - k++)) {
            streak++;
        }
        return streak;
    }
}
