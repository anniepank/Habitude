package com.github.anniepank.hability.activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.github.anniepank.hability.CircleCheckBox;
import com.github.anniepank.hability.CustomCalendarView;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.Reminder;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;
import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.DayView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EditHabitActivity extends AppCompatActivity {
    Habit currentHabit;
    ImageView imageView;
    CustomCalendarView calendarView;
    private FloatingActionButton button;
    private CollapsingToolbarLayout collapsingToolbar;
    private CircleCheckBox[] checkBoxes;
    public EditText timeReminder;
    public SwitchCompat reminderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        button = (FloatingActionButton) findViewById(R.id.editButton);
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        imageView = (ImageView) findViewById(R.id.backdrop);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        timeReminder = (EditText) findViewById(R.id.time_reminder);
        reminderSwitch = (SwitchCompat) findViewById(R.id.reminderSwitch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        int habit_number = intent.getIntExtra("habit_number", -1);
        currentHabit = Settings.getSettings(this).habits.get(habit_number);
        imageView.setImageDrawable(getResources().getDrawable(Habit.namesAndImages.get(currentHabit.type).image));
        collapsingToolbar.setTitle(currentHabit.habitName);

        DayDecorator dayDecorator = new DayDecorator() {
            @Override
            public void decorate(@NonNull DayView dayView) {
                Date date = dayView.getDate();
                long date_long = date.getTime() / (24 * 60 * 60 * 1000);
                dayView.setBackgroundColor(0xffffffff);
                dayView.setTextColor(0xff000000);
                if (currentHabit.days.contains(date_long)) {
                    Drawable drawable = getResources().getDrawable(R.drawable.calendar_highlight);
                    drawable.setBounds(0, 0, dayView.getMeasuredHeight(), dayView.getMeasuredHeight());
                    dayView.setBackground(drawable);
                    dayView.setTextColor(0xffffffff);
                }


            }
        };
        List<DayDecorator> listOfDayDecoratorsForThecalendarViewOfmyProjectInAndroidStudio = new LinkedList<>();
        listOfDayDecoratorsForThecalendarViewOfmyProjectInAndroidStudio.add(dayDecorator);
        calendarView.setDecoratorsList(listOfDayDecoratorsForThecalendarViewOfmyProjectInAndroidStudio);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nameView = new EditText(EditHabitActivity.this);
                new AlertDialog.Builder(EditHabitActivity.this)
                        .setTitle("Edit name")
                        .setView(nameView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String habitName = nameView.getText().toString();
                                rename(habitName);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                nameView.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        checkBoxes = new CircleCheckBox[7];
        checkBoxes[0] = (CircleCheckBox) findViewById(R.id.checkbox1);
        checkBoxes[1] = (CircleCheckBox) findViewById(R.id.checkbox2);
        checkBoxes[2] = (CircleCheckBox) findViewById(R.id.checkbox3);
        checkBoxes[3] = (CircleCheckBox) findViewById(R.id.checkbox4);
        checkBoxes[4] = (CircleCheckBox) findViewById(R.id.checkbox5);
        checkBoxes[5] = (CircleCheckBox) findViewById(R.id.checkbox6);
        checkBoxes[6] = (CircleCheckBox) findViewById(R.id.checkbox7);

        for (int i = 0; i < 7; i++) {
            final int i2 = i;
            checkBoxes[i2].setChecked(currentHabit.remindDays[i2]);
            checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBoxes[i2].setChecked(!checkBoxes[i2].isChecked);
                    currentHabit.remindDays[i2] = checkBoxes[i2].isChecked;
                    Settings.getSettings(EditHabitActivity.this).save(EditHabitActivity.this);
                }
            });
        }
        timeReminder.setText(currentHabit.reminderHours + ":" + (currentHabit.reminderMinutes < 10 ? "0" : "") + currentHabit.reminderMinutes);
        timeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(EditHabitActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeReminder.setText(selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute);
                        currentHabit.reminderHours = selectedHour;
                        currentHabit.reminderMinutes = selectedMinute;
                        Settings.getSettings(EditHabitActivity.this).save(EditHabitActivity.this);
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
                Settings.getSettings(EditHabitActivity.this).save(EditHabitActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            /*
            a = new B().c.d.e
            a = new B()
            .c
            .d
            .e

            B().c

            (new B()).c.d.e
             */
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Do you want to delete your habit?")

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Settings.getSettings(EditHabitActivity.this).habits.remove(currentHabit);
                            Settings.getSettings(EditHabitActivity.this).save(EditHabitActivity.this);
                            finish();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    public void rename(String habitName) {
        currentHabit.habitName = habitName;
        Settings.getSettings(this).save(this);
        collapsingToolbar.setTitle(currentHabit.habitName);
    }

}


