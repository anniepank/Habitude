package com.github.anniepank.hability.data;

import com.github.anniepank.hability.DateUtilities;
import com.github.anniepank.hability.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by anya on 11/6/16.
 */

public class Habit {
    public String id;
    public String name;
    public List<HabitDate> days;
    public String type = "other";
    public boolean deleted;
    public boolean[] remindDays = {false, false, false, false, false, false, false};
    public boolean remind;
    public int reminderHours;
    public int reminderMinutes;
    public long updatedAt;


    public Habit() {
        deleted = false;
        id = UUID.randomUUID().toString();
        days = new ArrayList<>();
    }

    public Habit(String id, String name, boolean deleted) {
        days = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.deleted = deleted;
    }

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
        if (hasDate(day)) {
            for (HabitDate date : days) {
                if (date.date == day) {
                    date.deleted = true;
                    return;
                }
            }
        } else {
            addDay(day);
        }
    }

    public boolean hasDate(long day) {
        for (HabitDate date : days) {
            if (date.date == day && !date.deleted) {
                return true;
            }
        }
        return false;
    }

    public void addDay(long day) {
        for (HabitDate date : days) {
            if (date.date == day) {
                date.deleted = false;
                return;
            }
        }
        days.add(new HabitDate(day));
    }

    public int getStreak() {
        int streak = 0;
        if (hasDate(DateUtilities.getToday())) {
            streak = 1;
        }
        long k = 1;
        while (hasDate(DateUtilities.getToday() - k++)) {
            streak++;
        }
        return streak;
    }

    public void bump() {
        updatedAt = System.currentTimeMillis();
    }
}
