package com.github.anniepank.hability;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.GregorianCalendar;

/**
 * Created by anya on 1/3/17.
 */

public class Reminder {
    public static void scheduleNotifications(Context context) {
        Long time = new GregorianCalendar().getTimeInMillis() + 5000;

        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
