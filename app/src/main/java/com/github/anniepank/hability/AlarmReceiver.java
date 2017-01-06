package com.github.anniepank.hability;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.anniepank.hability.activities.MainActivity;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Habit habit = Settings.getSettings(context).habits.get(intent.getIntExtra("habitNumber", 0));
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentTitle("Hability")
                .setContentText("It's time for " + habit.habitName)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notification = builder.build();

        Log.i("Notify", intent.getIntExtra("habitNumber", 0) + "");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getIntExtra("habitNumber", 0), notification);
    }
}
