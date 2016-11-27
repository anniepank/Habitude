package com.example.android.habittracker.data;

import java.util.List;

/**
 * Created by anya on 11/6/16.
 */

public class Habit {
    public String habitName;
    public List<Long> days;
    public void toggleDay(long day){
        if(days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }
    }
}
