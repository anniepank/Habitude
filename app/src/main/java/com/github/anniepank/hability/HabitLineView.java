package com.github.anniepank.hability;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.anniepank.hability.activities.EditHabitActivity;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

import static com.github.anniepank.hability.R.layout.habit_line_view;

/**
 * Created by anya on 11/2/16.
 */

public class HabitLineView extends LinearLayout {
    private TextView textView;
    private Habit _habit;
    private CircleCheckBox[] checkBoxes;
    private CoordinatorLayout coordinatorLayout;
    private TextView streakView;

    public HabitLineView(Context context, Habit habit, CoordinatorLayout _coordinatorLayout) {
        super(context);
        _habit = habit;
        coordinatorLayout = _coordinatorLayout;
        loadView();
    }

    public void loadView() {
        LayoutInflater.from(getContext()).inflate(habit_line_view, this, true);
        this.setPadding(0, 0, 0, 10);
        textView = (TextView) findViewById(R.id.name);
        streakView = (TextView) findViewById(R.id.streak);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditHabitActivity.class);
                // EditText editText = (EditText) findViewById(R.id.edit_message);
                // String message = editText.getText().toString();
                int i = Settings.getSettings(HabitLineView.this.getContext()).habits.indexOf(_habit);
                intent.putExtra("habit_number", i);
                ((Activity) getContext()).startActivityForResult(intent, 0);
            }
        });
        checkBoxes = new CircleCheckBox[5];
        checkBoxes[0] = (CircleCheckBox) findViewById(R.id.checkbox1);
        checkBoxes[1] = (CircleCheckBox) findViewById(R.id.checkbox2);
        checkBoxes[2] = (CircleCheckBox) findViewById(R.id.checkbox3);
        checkBoxes[3] = (CircleCheckBox) findViewById(R.id.checkbox4);
        checkBoxes[4] = (CircleCheckBox) findViewById(R.id.checkbox5);

        for (int i = 0; i < 5; i++) {
            final int i2 = i;
            checkBoxes[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBoxes[i2].setChecked(!checkBoxes[i2].isChecked);
                    long day = DateUtilities.getToday() - (4 - i2);
                    _habit.toggleDay(day);
                    Settings.getSettings(getContext()).save(getContext());

                    if (checkBoxes[i2].isChecked) {
                        Snackbar.make(coordinatorLayout, "Great!", Snackbar.LENGTH_SHORT).show();
                    }
                    updateStreak();
                }
            });
        }

    }

    //update habit in Habit Line View
    public void update() {
        textView.setText(_habit.habitName);
        for (int i = 0; i < 5; i++) {
            checkBoxes[i].setChecked(_habit.days.contains(DateUtilities.getToday() - 4 + i));
        }
    }

    private void updateStreak() {
        String streakString = getContext().getResources().getString(R.string.streak);
        streakView.setText(streakString.replace("^", _habit.getStreak() + ""));
    }
}


