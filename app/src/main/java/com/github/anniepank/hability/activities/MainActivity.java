package com.github.anniepank.hability.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.anniepank.hability.HabitLineView;
import com.github.anniepank.hability.ImageOfTheDay;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.Reminder;
import com.github.anniepank.hability.Synchronizer;
import com.github.anniepank.hability.data.Settings;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainScroll;
    private FloatingActionButton newHabitButton, bigNewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mainScroll = (LinearLayout) findViewById(R.id.mainScroll);
        newHabitButton = (FloatingActionButton) findViewById(R.id.new_habit);
        bigNewButton = (FloatingActionButton) findViewById(R.id.new_habit_big);
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);

        ImageOfTheDay.loadImage(imageView);

        refreshView();

        //function inside the variable
        View.OnClickListener newHabitButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
                startActivityForResult(intent, 0);
            }
        };

        newHabitButton.setOnClickListener(newHabitButtonClickListener);
        bigNewButton.setOnClickListener(newHabitButtonClickListener);

        if (Settings.get(this).habits.size() == 0) {
            Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
            intent.putExtra(NewHabitActivity.FIRST_START_EXTRA, true);
            startActivityForResult(intent, 0);
            Intent intentSlider = new Intent(this, IntroActivity.class);
            startActivity(intentSlider);
        }
        Reminder.scheduleNotifications(this);

        if (Settings.get(this).syncKey != null) {
            Synchronizer.sync(this, new Synchronizer.ISyncCallback() {
                @Override
                public void onFinished() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshView();
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sync) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void refreshView() {
        mainScroll.removeAllViews();
        Settings settings = Settings.get(this);
        bigNewButton.setVisibility(settings.habits.size() == 0 ? View.VISIBLE : View.GONE);
        newHabitButton.setVisibility(settings.habits.size() != 0 ? View.VISIBLE : View.GONE);
        for (int i = 0; i < settings.habits.size(); i++) {
            HabitLineView hlv = new HabitLineView(this, settings.habits.get(i), (CoordinatorLayout) findViewById(R.id.coordinator));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            hlv.setLayoutParams(params);
            mainScroll.addView(hlv);
            hlv.update();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshView();
    }


}