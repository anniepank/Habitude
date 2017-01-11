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
    private TextView nameView;
    private Habit currentHabit;
    private CircleCheckBox[] checkboxes;
    private CoordinatorLayout coordinatorLayout;
    private TextView streakView;

    public HabitLineView(Context context, Habit habit, CoordinatorLayout coordinatorLayout) {
        super(context);
        currentHabit = habit;
        this.coordinatorLayout = coordinatorLayout;
        loadView();
    }

    public void loadView() {
        LayoutInflater.from(getContext()).inflate(habit_line_view, this, true);
        this.setPadding(0, 0, 0, 10);
        nameView = (TextView) findViewById(R.id.name);
        streakView = (TextView) findViewById(R.id.streak);
        findViewById(R.id.name_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditHabitActivity.class);
                int habitNumber = Settings.get(HabitLineView.this.getContext()).habits.indexOf(currentHabit);
                intent.putExtra(EditHabitActivity.HABIT_NUMBER_EXTRA, habitNumber);
                ((Activity) getContext()).startActivityForResult(intent, 0);
            }
        });
        checkboxes = new CircleCheckBox[5];
        checkboxes[0] = (CircleCheckBox) findViewById(R.id.checkbox1);
        checkboxes[1] = (CircleCheckBox) findViewById(R.id.checkbox2);
        checkboxes[2] = (CircleCheckBox) findViewById(R.id.checkbox3);
        checkboxes[3] = (CircleCheckBox) findViewById(R.id.checkbox4);
        checkboxes[4] = (CircleCheckBox) findViewById(R.id.checkbox5);

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            checkboxes[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkboxes[finalI].setChecked(!checkboxes[finalI].isChecked);
                    long day = DateUtilities.getToday() - (4 - finalI);
                    currentHabit.toggleDay(day);
                    Settings.get(getContext()).save(getContext());

                    if (checkboxes[finalI].isChecked) {
                        Snackbar.make(coordinatorLayout, Motivation.getShortMotivation(getContext()), Snackbar.LENGTH_SHORT).show();
                    }
                    updateStreak();
                }
            });
        }
    }

    public void update() {
        nameView.setText(currentHabit.habitName);
        updateStreak();
        for (int i = 0; i < 5; i++) {
            checkboxes[i].setChecked(currentHabit.days.contains(DateUtilities.getToday() - 4 + i));
        }
    }

    private void updateStreak() {
        String streakString = getContext().getResources().getQuantityString(R.plurals.streak, currentHabit.getStreak());
        if (currentHabit.getStreak() == 0) {
            streakView.setText(R.string.zero_streak);
        } else {
            streakView.setText(streakString.replace("^", currentHabit.getStreak() + ""));
        }
    }
}
