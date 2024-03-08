package com.roseno.curbcrime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.roseno.curbcrime.manager.AlarmManager;
import com.roseno.curbcrime.provider.NotificationProvider;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (AlarmManager.ACTION_ALARM_STOP.equals(action)) {
            AlarmManager.stop();

            NotificationProvider.cancelNotification(context, NotificationProvider.NOTIFICATION_ID_ALARM);
        }
    }
}
