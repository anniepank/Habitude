package com.github.anniepank.hability.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.anniepank.hability.DateUtilities;
import com.github.anniepank.hability.Motivation;
import com.github.anniepank.hability.R;
import com.github.anniepank.hability.activities.MainActivity;
import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.Settings;

public class MarkAsDoneReceiver extends BroadcastReceiver {

    public static final String HABIT_NUMBER_EXTRA = "habitNumber";

    @Override
    public void onReceive(Context context, Intent intent) {
        Habit habit = Settings.get(context).habits.get(intent.getIntExtra(HABIT_NUMBER_EXTRA, 0));
        long day = DateUtilities.getToday();
        habit.addDay(day);
        Settings.get(context).save(context);

        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent pendingActivity = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(Motivation.getShortMotivation(context))
                .setContentIntent(pendingActivity);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getIntExtra(HABIT_NUMBER_EXTRA, -1), notification);

    }
}
