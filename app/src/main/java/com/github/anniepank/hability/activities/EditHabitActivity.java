package com.github.anniepank.hability.activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.github.anniepank.hability.CircleCheckBox;
import com.github.anniepank.hability.CustomCalendarView;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.Reminder;
import com.github.anniepank.hability.Synchronizer;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;
import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.DayView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EditHabitActivity extends AppCompatActivity {
    public static final String HABIT_NUMBER_EXTRA = "habitNumber";
    private Habit currentHabit;
    private ImageView imageView;
    private CustomCalendarView calendarView;
    private FloatingActionButton editButton;
    private CollapsingToolbarLayout collapsingToolbar;
    private CircleCheckBox[] checkboxes;
    public EditText reminderTime;
    public SwitchCompat reminderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        editButton = (FloatingActionButton) findViewById(R.id.editButton);
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        imageView = (ImageView) findViewById(R.id.backdrop);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        reminderTime = (EditText) findViewById(R.id.time_reminder);
        reminderSwitch = (SwitchCompat) findViewById(R.id.reminderSwitch);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int habitNumber = getIntent().getIntExtra(HABIT_NUMBER_EXTRA, -1);
        currentHabit = Settings.get(this).habits.get(habitNumber);
        imageView.setImageDrawable(getResources().getDrawable(Habit.namesAndImages.get(currentHabit.type).image));
        collapsingToolbar.setTitle(currentHabit.name);

        setupCalendar();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nameView = new EditText(EditHabitActivity.this);
                nameView.setText(currentHabit.name);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                FrameLayout frameLayout = new FrameLayout(EditHabitActivity.this);
                frameLayout.addView(nameView);
                params.setMargins(60, 0, 60, 0);
                nameView.setLayoutParams(params);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                new AlertDialog.Builder(EditHabitActivity.this)
                        .setTitle(R.string.edit_name_title)
                        .setView(frameLayout)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String habitName = nameView.getText().toString();
                                rename(habitName);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                nameView.requestFocus();
            }
        });

        calendarView.setOnDayOfMonthClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayView dayView = (DayView) ((RelativeLayout) view).getChildAt(0);
                long date = dayView.getDate().getTime() / (24 * 60 * 60 * 1000);
                currentHabit.toggleDay(date);
                dayView.decorate();
            }
        });
        calendarView.refreshCalendar(Calendar.getInstance());

        checkboxes = new CircleCheckBox[7];
        checkboxes[0] = (CircleCheckBox) findViewById(R.id.checkbox1);
        checkboxes[1] = (CircleCheckBox) findViewById(R.id.checkbox2);
        checkboxes[2] = (CircleCheckBox) findViewById(R.id.checkbox3);
        checkboxes[3] = (CircleCheckBox) findViewById(R.id.checkbox4);
        checkboxes[4] = (CircleCheckBox) findViewById(R.id.checkbox5);
        checkboxes[5] = (CircleCheckBox) findViewById(R.id.checkbox6);
        checkboxes[6] = (CircleCheckBox) findViewById(R.id.checkbox7);

        for (int i = 0; i < 7; i++) {
            final int finalI = i;
            checkboxes[finalI].setChecked(currentHabit.remindDays[finalI]);
            checkboxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkboxes[finalI].setChecked(!checkboxes[finalI].isChecked);
                    currentHabit.remindDays[finalI] = checkboxes[finalI].isChecked;
                    Settings.get(EditHabitActivity.this).save(EditHabitActivity.this);
                }
            });
        }
        reminderTime.setText(currentHabit.reminderHours + ":" + (currentHabit.reminderMinutes < 10 ? "0" : "") + currentHabit.reminderMinutes);
        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(EditHabitActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        reminderTime.setText(selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute);
                        currentHabit.reminderHours = selectedHour;
                        currentHabit.reminderMinutes = selectedMinute;
                        Settings.get(EditHabitActivity.this).save(EditHabitActivity.this);
                    }
                }, 5, 0, true);
                timePicker.show();
            }
        });
        reminderSwitch.setChecked(currentHabit.remind);
        reminderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentHabit.remind = reminderSwitch.isChecked();
                Settings.get(EditHabitActivity.this).save(EditHabitActivity.this);
            }
        });
    }

    private void setupCalendar() {
        DayDecorator dayDecorator = new DayDecorator() {
            @Override
            public void decorate(@NonNull DayView dayView) {
                Date date = dayView.getDate();
                long dateLong = date.getTime() / (24 * 60 * 60 * 1000);
                dayView.setBackgroundColor(0xffffffff);
                dayView.setTextColor(0xff000000);
                if (currentHabit.hasDate(dateLong)) {
                    Drawable drawable = getResources().getDrawable(R.drawable.calendar_highlight);
                    drawable.setBounds(0, 0, dayView.getMeasuredHeight(), dayView.getMeasuredHeight());
                    dayView.setBackground(drawable);
                    dayView.setTextColor(0xffffffff);
                }
            }
        };
        List<DayDecorator> decorators = new LinkedList<>();
        decorators.add(dayDecorator);
        calendarView.setDecoratorsList(decorators);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.delete_confirmation)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            delete();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            Reminder.scheduleNotifications(this);
        }
        return true;
    }

    private void delete() {
        currentHabit.deleted = true;
        currentHabit.bump();

        Settings.get(EditHabitActivity.this).save(EditHabitActivity.this);
        finish();

        Synchronizer.sync(this, null);
    }

    public void rename(String habitName) {
        currentHabit.name = habitName;
        currentHabit.bump();
        Settings.get(this).save(this);
        collapsingToolbar.setTitle(currentHabit.name);

        Synchronizer.sync(this, null);
    }
}


