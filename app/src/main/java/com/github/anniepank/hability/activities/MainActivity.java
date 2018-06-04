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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.anniepank.hability.HabitLineView;
import com.github.anniepank.hability.ImageOfTheDay;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.Reminder;
import com.github.anniepank.hability.Synchronizer;
import com.github.anniepank.hability.data.Settings;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mainScroll;
    private FloatingActionButton newHabitButton, bigNewButton;
    private RelativeLayout websiteLink;
    private final int RQSC_Login = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mainScroll = (LinearLayout) findViewById(R.id.mainScroll);
        newHabitButton = (FloatingActionButton) findViewById(R.id.new_habit);
        bigNewButton = (FloatingActionButton) findViewById(R.id.new_habit_big);
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        websiteLink = (RelativeLayout) findViewById(R.id.websiteLink);
        final ImageView closeLinkButton = (ImageView) findViewById(R.id.closeButton);

        ImageOfTheDay.loadImage(imageView);

        refreshView();

        View.OnClickListener newHabitButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
                startActivityForResult(intent, 0);
            }
        };

        newHabitButton.setOnClickListener(newHabitButtonClickListener);
        bigNewButton.setOnClickListener(newHabitButtonClickListener);

        if (!Settings.get(this).webIntroClosed) {
            websiteLink.setVisibility(View.VISIBLE);
        } else {
            websiteLink.setVisibility(View.GONE);
        }

        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SyncIntroActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        closeLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup) websiteLink.getParent()).removeView(websiteLink);

                Settings settings = Settings.get(MainActivity.this);
                settings.webIntroClosed = true;
                settings.save(MainActivity.this);

            }
        });

        if (Settings.get(this).habits.size() == 0) {
            Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
            intent.putExtra(NewHabitActivity.FIRST_START_EXTRA, true);
            startActivityForResult(intent, 0);
            Intent intentSlider = new Intent(this, IntroActivity.class);
            startActivity(intentSlider);
        }
        Reminder.scheduleNotifications(this);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        updateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login: {
                Intent intent = new Intent(this, SyncIntroActivity.class);
                startActivityForResult(intent, 2);
                return true;
            }
            case R.id.logout: {
                Settings settings = Settings.get(this);
                settings.syncKey = null;
                settings.save(this);
                refreshView();
                break;
            }

            case R.id.sync: {
                Synchronizer.sync(this, new Synchronizer.ISyncCallback() {
                    @Override
                    public void onFinished() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Synchronized", Toast.LENGTH_SHORT).show();
                                refreshView();
                            }
                        });
                    }
                });
                break;
            }
        }
        return false;
    }

    public void refreshView() {
        mainScroll.removeAllViews();
        Settings settings = Settings.get(this);
        bigNewButton.setVisibility(settings.habits.size() == 0 ? View.VISIBLE : View.GONE);
        newHabitButton.setVisibility(settings.habits.size() != 0 ? View.VISIBLE : View.GONE);
        if (settings.webIntroClosed) {
            websiteLink.setVisibility(View.GONE);
        }
        for (int i = 0; i < settings.habits.size(); i++) {
            if (settings.habits.get(i).deleted) continue;

            HabitLineView hlv = new HabitLineView(this, settings.habits.get(i), (CoordinatorLayout) findViewById(R.id.coordinator));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            hlv.setLayoutParams(params);
            mainScroll.addView(hlv);
            hlv.update();
        }
        invalidateOptionsMenu();
    }

    public void updateMenu(Menu mainMenu) {
        Settings settings = Settings.get(this);
        MenuItem loginItem = mainMenu.findItem(R.id.login);
        MenuItem logoutItem = mainMenu.findItem(R.id.logout);
        MenuItem syncItem = mainMenu.findItem(R.id.sync);

        logoutItem.setVisible(settings.syncKey != null);
        loginItem.setVisible(settings.syncKey == null);
        syncItem.setVisible(settings.syncKey != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
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
                break;
            }
            case 2: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 1);
                break;
            }
        }
        refreshView();
    }


}