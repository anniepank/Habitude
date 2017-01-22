package com.github.anniepank.hability.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class LaterReceiver extends BroadcastReceiver {

    public static final String HABIT_NUMBER_EXTRA = "habitNumber";
    public static final int LATER_TIMEOUT = 15 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra(HABIT_NUMBER_EXTRA, -1));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent laterIntent = new Intent(context, AlarmReceiver.class);
        laterIntent.putExtra(AlarmReceiver.HABIT_NUMBER_EXTRA, intent.getIntExtra(HABIT_NUMBER_EXTRA, -1));
        PendingIntent laterPendingIntent = PendingIntent.getBroadcast(context, 104, laterIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, LATER_TIMEOUT + SystemClock.elapsedRealtime(), laterPendingIntent);
    }
}
