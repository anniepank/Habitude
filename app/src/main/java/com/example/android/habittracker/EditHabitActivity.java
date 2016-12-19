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
import android.widget.TextView;

import com.example.android.habittracker.data.Habit;
import com.example.android.habittracker.data.Settings;

public class EditHabitActivity extends AppCompatActivity {
    EditText editHabitName;
    Habit edit_habit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        editHabitName = (EditText) findViewById(R.id.edit_habit_name);
        editHabitName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        edit_habit = Settings.global.habits.get(habit_number);
        editHabitName.setText(edit_habit.habitName);

        collapsingToolbar.setTitle(edit_habit.habitName);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Settings.global.habits.remove(edit_habit);
            Settings.global.save(this);
            finish();
        }
        if(item.getItemId() == android.R.id.home) {
            saveAndFinish();
        }
        return true;
    }

    public void saveAndFinish() {
        edit_habit.habitName = editHabitName.getText().toString();
        EditHabitActivity.this.finish();
        Settings.global.save(EditHabitActivity.this);
    }
    @Override
    public void onBackPressed() {
        saveAndFinish();
    }
}


