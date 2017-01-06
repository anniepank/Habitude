package com.github.anniepank.hability;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

import java.util.Calendar;

/**
 * Created by anya on 1/3/17.
 */

public class Reminder {
    public static void scheduleNotifications(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int n = Settings.getSettings(context).habits.size();
        for (int i = 0; i < n; i++) {

            Habit habit = Settings.getSettings(context).habits.get(i);

            Intent intentAlarm = new Intent(context, AlarmReceiver.class);
            intentAlarm.putExtra("habitNumber", i);

            //new GregorianCalendar().getTimeInMillis() + 2000 + new Random().nextInt() % 1000;
            //Log.i("CURRENT TIME!!!!!!!!!", Calendar.getInstance().getTimeInMillis() + " " + time + " " + (time - Calendar.getInstance().getTimeInMillis()));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            if (habit.remind) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.SECOND, 0);
                date.set(Calendar.HOUR_OF_DAY, (int) habit.reminderHours);
                date.set(Calendar.MINUTE, (int) habit.reminderMinutes);
                if (date.before(Calendar.getInstance())) {
                    date.add(Calendar.DAY_OF_MONTH, 1);
                }
                long time = date.getTimeInMillis();
                //убрать
                time = Calendar.getInstance().getTimeInMillis() + 1000;

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
    }
}
