package com.example.android.habittracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.habittracker.data.Habit;
import com.example.android.habittracker.data.Settings;

import java.util.LinkedList;

public class HabitsFragment extends Fragment {
    private LinearLayout mainScroll;
    private Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.habits_fragment, container, false);
        mainScroll  = (LinearLayout) rootView.findViewById(R.id.mainScroll);
        button = (Button) rootView.findViewById(R.id.new_habit);
        //create new object in settings with an array "one"
        //there are 3 habits in array one
        Settings one = new Settings();
        one.habits = new LinkedList<> ();
        Habit habit_a = new Habit();
        Habit habit_b = new Habit();
        Habit habit_c = new Habit();
        habit_a.habitName = "habit_a";
        habit_b.habitName = "habit_b";
        habit_c.habitName = "habit_c";
        one.habits.add(habit_a);
        one.habits.add(habit_b);
        one.habits.add(habit_c);
        refreshView(one);

        Settings.global = one;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit habit = new Habit();
                habit.habitName = "New habit";
                Settings.global.habits.add(habit);
                refreshView(Settings.global);
            }
        });
        return rootView;
    }

    public void refreshView(Settings settings) {
        mainScroll.removeAllViews();
        for (int i = 0; i < settings.habits.size(); i++) {
            HabitLineView hlv = new HabitLineView(getContext(), settings.habits.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            hlv.setLayoutParams(params);
            mainScroll.addView(hlv);
            hlv.update();
        }
    }




}


