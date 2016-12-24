package com.example.android.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.habittracker.data.Habit;
import com.example.android.habittracker.data.Settings;

public class EditHabitActivity extends AppCompatActivity {
    EditText editHabitNameView;
    Habit currentHabit;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        editHabitNameView = (EditText) findViewById(R.id.edit_habit_name);
        imageView = (ImageView) findViewById(R.id.backdrop);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editHabitNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0 || actionId == 6) {
                    saveAndFinish();
                }
                return false;
            }
        });
        Intent intent = getIntent();
        int habit_number = intent.getIntExtra("habit_number", -1);
        currentHabit = Settings.global.habits.get(habit_number);
        editHabitNameView.setText(currentHabit.habitName);
        imageView.setImageDrawable(getResources().getDrawable(Habit.namesAndImages.get(currentHabit.type).image));
        collapsingToolbar.setTitle(currentHabit.habitName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Settings.global.habits.remove(currentHabit);
            Settings.global.save(this);
            finish();
        }
        if (item.getItemId() == android.R.id.home) {
            saveAndFinish();
        }
        return true;
    }

    public void saveAndFinish() {
        currentHabit.habitName = editHabitNameView.getText().toString();
        EditHabitActivity.this.finish();
        Settings.global.save(EditHabitActivity.this);
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
    }
}


