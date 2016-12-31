package com.github.anniepank.hability;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.anniepank.hability.data.Settings;

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

        mainScroll = (LinearLayout) findViewById(R.id.mainScroll);
        button = (FloatingActionButton) findViewById(R.id.new_habit);

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        ImageOfTheDay.loadImage(imageView);

        Settings settings = Settings.load(this);
        Settings.global = settings;

        refreshView(settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
                startActivityForResult(intent, 0);
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