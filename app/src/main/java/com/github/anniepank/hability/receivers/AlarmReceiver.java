package com.github.anniepank.hability.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.anniepank.hability.R;
import com.github.anniepank.hability.activities.MainActivity;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String HABIT_NUMBER_EXTRA = "habitNumber";

    @Override
    public void onReceive(Context context, Intent intent) {
        int habitNumber = intent.getIntExtra(HABIT_NUMBER_EXTRA, -1);
        Habit habit = Settings.get(context).habits.get(habitNumber);

        Intent markAsDoneIntent = new Intent(context, MarkAsDoneReceiver.class);
        markAsDoneIntent.putExtra(MarkAsDoneReceiver.HABIT_NUMBER_EXTRA, habitNumber);
        PendingIntent markAsDonePendingIntent = PendingIntent.getBroadcast(context, 102, markAsDoneIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action markAsDoneAction = new Notification.Action.Builder(R.drawable.ic_done_black_24dp, context.getString(R.string.done_title), markAsDonePendingIntent).build();

        Intent laterIntent = new Intent(context, LaterReceiver.class);
        laterIntent.putExtra(LaterReceiver.HABIT_NUMBER_EXTRA, habitNumber);
        PendingIntent laterPendingIntent = PendingIntent.getBroadcast(context, 103, laterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action laterAction = new Notification.Action.Builder(R.drawable.ic_watch_later_black_24dp, context.getString(R.string.fifteen_minutes), laterPendingIntent).build();

        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent pendingActivity = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.time_for) + habit.habitName)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingActivity)
                .addAction(markAsDoneAction)
                .addAction(laterAction);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(habitNumber, notification);
    }
}
