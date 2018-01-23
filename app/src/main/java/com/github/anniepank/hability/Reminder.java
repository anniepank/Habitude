package com.github.anniepank.hability;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;
import com.github.anniepank.hability.receivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by anya on 1/3/17.
 */

public class Reminder {
    public static void scheduleNotifications(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int nHabits = Settings.get(context).habits.size();
        for (int i = 0; i < nHabits; i++) {
            Habit habit = Settings.get(context).habits.get(i);

            Intent intentAlarm = new Intent(context, AlarmReceiver.class);
            intentAlarm.putExtra(AlarmReceiver.HABIT_NUMBER_EXTRA, i);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);

            if (habit.remind) {
                Calendar date = getNearestReminderDate(habit);
                if (date == null) {
                    continue;
                }
                long time = date.getTimeInMillis();
                Log.d("Hability", "Planned " + habit.name + " in " + (time - Calendar.getInstance().getTimeInMillis()) / 1000 / 60 / 60 + " hours");

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
    }

    private static Calendar getNearestReminderDate(Habit habit) {
        Calendar minDate = null;
        for (int i = 0; i < 7; i++) {
            if (habit.remindDays[i]) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.SECOND, 0);
                date.set(Calendar.HOUR_OF_DAY, habit.reminderHours);
                date.set(Calendar.MINUTE, habit.reminderMinutes);
                date.set(Calendar.DAY_OF_WEEK, (i + 1) % 7 + 1);
                if (date.before(Calendar.getInstance())) {
                    date.add(Calendar.DAY_OF_MONTH, 7);
                }
                if (minDate == null || date.before(minDate)) {
                    minDate = date;
                }
            }
        }
        return minDate;
    }
}
