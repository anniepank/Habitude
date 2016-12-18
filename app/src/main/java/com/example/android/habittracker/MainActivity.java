package com.example.android.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android.habittracker.data.Habit;
import com.example.android.habittracker.data.Settings;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainScroll;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        mainScroll  = (LinearLayout) findViewById(R.id.mainScroll);
        button = (FloatingActionButton) findViewById(R.id.new_habit);
        //create new object in settings with an array "one"
        //there are 3 habits in array one

        Settings settings = Settings.load(this);
        Settings.global = settings;

        refreshView(settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit habit = new Habit();
                habit.days = new LinkedList<>();
                habit.habitName = "New habit";
                Settings.global.habits.add(habit);
                Settings.global.save(MainActivity.this);
                refreshView(Settings.global);
            }
        });



    }

    public void refreshView(Settings settings) {
        mainScroll.removeAllViews();
        for (int i = 0; i < settings.habits.size(); i++) {
            HabitLineView hlv = new HabitLineView(this, settings.habits.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            hlv.setLayoutParams(params);
            mainScroll.addView(hlv);
            hlv.update();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshView(Settings.global);
    }




}