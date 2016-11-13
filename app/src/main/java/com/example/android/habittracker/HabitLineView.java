package com.example.android.habittracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.habittracker.data.Habit;
import com.example.android.habittracker.data.Settings;

import static com.example.android.habittracker.R.layout.habit_line_view;

/**
 * Created by anya on 11/2/16.
 */

public class HabitLineView extends LinearLayout {
    private TextView textView;
    private Habit _habit;

    public HabitLineView(Context context, Habit habit){
        super(context);
        _habit = habit;
        loadView(); // Input == load
    }


    public void loadView() {
        LayoutInflater.from(getContext()).inflate(habit_line_view, this, true);
        this.setPadding(0, 0, 0, 10);
        textView = (TextView)findViewById(R.id.name);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditHabitActivity.class);
               // EditText editText = (EditText) findViewById(R.id.edit_message);
               // String message = editText.getText().toString();
                int i = Settings.global.habits.indexOf(_habit);
                intent.putExtra("habit_number", i);
                ((Activity)getContext()).startActivityForResult(intent, 0);
            }
        });

    }

    //update habit in Habit Line View
    public void update() {
        textView.setText(_habit.habitName);
    }
}


