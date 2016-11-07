package com.example.android.habittracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HabitsFragment extends Fragment {
    private LinearLayout mainScroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.habits_fragment, container, false);
        mainScroll  = (LinearLayout) rootView.findViewById(R.id.mainScroll);
        //create new object in settings with an array "one"
        //there are 3 habits in array one
        Settings one = new Settings();
        one.habits = new Habit[3];
        Habit habit_a = new Habit();
        Habit habit_b = new Habit();
        Habit habit_c = new Habit();
        habit_a.habitName = "habit_a";
        habit_b.habitName = "habit_b";
        habit_c.habitName = "habit_c";
        one.habits[0] = habit_a;
        one.habits[1] = habit_b;
        one.habits[2] = habit_c;
        refreshView(one);

        return rootView;
    }

    public void refreshView(Settings settings) {
        mainScroll.removeAllViews();
        for (int i = 0; i < settings.habits.length; i++) {
            HabitLineView hlv = new HabitLineView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            hlv.setLayoutParams(params);
            mainScroll.addView(hlv);
            hlv.update(settings.habits[i]);
        }
    }





}


