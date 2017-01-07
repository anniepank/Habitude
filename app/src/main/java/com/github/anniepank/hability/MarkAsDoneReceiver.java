package com.github.anniepank.hability;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.anniepank.hability.activities.MainActivity;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

public class MarkAsDoneReceiver extends BroadcastReceiver {
    public MarkAsDoneReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Habit habit = Settings.getSettings(context).habits.get(intent.getIntExtra("habitNumber", 0));
        long day = DateUtilities.getToday();
        habit.addDay(day);
        Settings.getSettings(context).save(context);

        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent pendingActivity = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentTitle("Hability")
                .setContentText("Great! ")
                .setContentIntent(pendingActivity);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getIntExtra("habitNumber", 0), notification);

    }
}
