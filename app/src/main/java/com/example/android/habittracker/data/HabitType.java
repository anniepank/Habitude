package com.example.android.habittracker.data;

import com.example.android.habittracker.R;

/**
 * Created by anya on 12/19/16.
 */

public class HabitType {
    public String name;
    public int image = R.drawable.cat;

    public HabitType(String name, int image) {
        this.name = name;
        this.image = image;
    }
}
