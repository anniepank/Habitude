package com.github.anniepank.hability.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.anniepank.hability.HabitLineView;
import com.github.anniepank.hability.ImageOfTheDay;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.data.Settings;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainScroll;
    private FloatingActionButton button, buttonBig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainScroll = (LinearLayout) findViewById(R.id.mainScroll);

        button = (FloatingActionButton) findViewById(R.id.new_habit);
        buttonBig = (FloatingActionButton) findViewById(R.id.new_habit_big);

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        ImageOfTheDay.loadImage(imageView);

        refreshView();

        //function inside the variable
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
                startActivityForResult(intent, 0);
            }
        };

        button.setOnClickListener(clickListener);
        buttonBig.setOnClickListener(clickListener);
    }

    public void refreshView() {
        mainScroll.removeAllViews();
        Settings settings = Settings.getSettings(this);
        buttonBig.setVisibility(settings.habits.size() == 0 ? View.VISIBLE : View.GONE);
        button.setVisibility(settings.habits.size() != 0 ? View.VISIBLE : View.GONE);
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
        refreshView();
    }


}