package com.github.anniepank.hability.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.anniepank.hability.Reminder;

public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Reminder.scheduleNotifications(context);
    }
}
