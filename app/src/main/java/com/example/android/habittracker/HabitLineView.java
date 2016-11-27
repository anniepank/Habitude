package com.example.android.habittracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.habittracker.data.Habit;
import com.example.android.habittracker.data.Settings;

import java.util.Date;

import static com.example.android.habittracker.R.layout.habit_line_view;

/**
 * Created by anya on 11/2/16.
 */

public class HabitLineView extends LinearLayout {
    private TextView textView;
    private Habit _habit;
    private CheckBox[] checkBoxes;

    public HabitLineView(Context context, Habit habit){
        super(context);
        _habit = habit;
        loadView();
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
        checkBoxes = new CheckBox[5];
        checkBoxes[0] = (CheckBox)findViewById(R.id.checkbox1);
        checkBoxes[1] = (CheckBox)findViewById(R.id.checkbox2);
        checkBoxes[2] = (CheckBox)findViewById(R.id.checkbox3);
        checkBoxes[3] = (CheckBox)findViewById(R.id.checkbox4);
        checkBoxes[4] = (CheckBox)findViewById(R.id.checkbox5);

        for(int i = 0; i < 5; i++) {
            final int i2 = i;
            checkBoxes[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    long day = getToday()  - (4 - i2);
                    _habit.toggleDay(day);
                    Settings.global.save(getContext());
                }
            });
        }
    }

    private long getToday() {
        return new Date().getTime() / (24*60*60*1000);
    }

    //update habit in Habit Line View
    public void update() {
        textView.setText(_habit.habitName);
        for(int i = 0; i < 5; i++) {
            checkBoxes[i].setChecked(_habit.days.contains(getToday() - 4 + i));
        }

    }
}


