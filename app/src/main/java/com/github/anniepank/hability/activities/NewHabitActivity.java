package com.github.anniepank.hability.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.github.anniepank.hability.R;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

import java.util.LinkedList;

public class NewHabitActivity extends AppCompatActivity {

    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        container = (LinearLayout) findViewById(R.id.activity_new_habit);
        for (int i = 0; i < container.getChildCount(); i++) {
            final int j = i;
            container.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = (String) Habit.namesAndImages.keySet().toArray()[j];
                    createHabit(key);
                    finish();
                }
            });
        }
    }

    private void createHabit(String key) {
        Habit habit = new Habit();
        habit.days = new LinkedList<>();
        habit.type = key;
        habit.habitName = Habit.namesAndImages.get(key).name;
        Settings.getSettings(this).habits.add(habit);
        Settings.getSettings(this).save(this);
    }
}
