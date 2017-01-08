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

        Intent markAsDoneIntent = new Intent(context, MarkAsDoneReceiver.class);
        markAsDoneIntent.putExtra("habitNumber", intent.getIntExtra("habitNumber", 0));
        PendingIntent markAsDonePendingIntent = PendingIntent.getBroadcast(context, 102, markAsDoneIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action markAsDoneAction = new Notification.Action.Builder(R.drawable.ic_done_black_24dp, "Done!", markAsDonePendingIntent).build();

        Intent laterIntent = new Intent(context, LaterReceiver.class);
        PendingIntent laterPendingIntent = PendingIntent.getBroadcast(context, 103, laterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action laterAction = new Notification.Action.Builder(R.drawable.ic_watch_later_black_24dp, "15 minutes", laterPendingIntent).build();

        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent pendingActivity = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentTitle("Hability")
                .setContentText("It's time for " + habit.habitName)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingActivity)
                .addAction(markAsDoneAction)
                .addAction(laterAction);
        Notification notification = builder.build();

        Log.i("Notify", intent.getIntExtra("habitNumber", 0) + "");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getIntExtra("habitNumber", 0), notification);
    }
}
