package com.github.anniepank.hability.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

import java.util.LinkedList;

public class NewHabitActivity extends AppCompatActivity {
    public static final String FIRST_START_EXTRA = "firstStart";
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        container = (LinearLayout) findViewById(R.id.activity_new_habit);
        for (int i = 0; i < container.getChildCount(); i++) {
            final String key = (String) Habit.namesAndImages.keySet().toArray()[i];
            container.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createHabit(key);
                    finish();
                }
            });

            final RelativeLayout element = (RelativeLayout) container.getChildAt(i);
            final ImageView image = (ImageView) element.getChildAt(0);

            final TextView text = (TextView) element.getChildAt(1);
            text.setText(Habit.namesAndImages.get(key).name);

            image.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(NewHabitActivity.this)
                            .load(Habit.namesAndImages.get(key).image)
                            .override(element.getMeasuredWidth(), element.getMeasuredHeight())
                            .centerCrop()
                            .into(image);
                }
            });
        }

        if (getIntent().getBooleanExtra(FIRST_START_EXTRA, false)) {
            Toast.makeText(this, R.string.greeting, Toast.LENGTH_LONG).show();
        }
    }

    private void createHabit(String key) {
        Habit habit = new Habit();
        habit.days = new LinkedList<>();
        habit.type = key;
        habit.habitName = getResources().getString(Habit.namesAndImages.get(key).name);
        Settings.get(this).habits.add(habit);
        Settings.get(this).save(this);
    }
}
