package com.github.anniepank.hability;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LaterReceiver extends BroadcastReceiver {
    public LaterReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("habitNumber", 0));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent laterIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent laterPendingIntent = PendingIntent.getBroadcast(context, 104, laterIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, 5000, laterPendingIntent);
    }
}
