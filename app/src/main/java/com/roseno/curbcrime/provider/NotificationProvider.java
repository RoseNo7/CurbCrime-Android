package com.roseno.curbcrime.provider;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.manager.AlarmManager;
import com.roseno.curbcrime.receiver.AlarmBroadcastReceiver;
import com.roseno.curbcrime.service.MainService;

public class NotificationProvider extends Application {
    private final String TAG = "NotificationProvider";

    public static final int NOTIFICATION_ID_APP = 1;
    public static final int NOTIFICATION_ID_ALARM = 2;

    public static final String CHANNEL_ID = "APPLICATION";
    public static final String CHANNEL_NAME = "CurbCrime";

    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_LOW;

    /**
     * 알림 채널 생성
     * @param context
     */
    private static void createAppChannel(final Context context) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    /**
     * 애플리케이션 알림 생성
     * @param context
     * @return
     */
    public static Notification createAppNotification(final Context context) {
        createAppChannel(context);

        Intent intent = new Intent(context, MainService.class);
        intent.setAction(MainService.ACTION_SERVICE_STOP);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String title = "안전 시스템 가동";
        String content = "안전 시스템을 사용 중지하려면 탭하세요.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_notification_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent)
                .setOngoing(true)
                .setShowWhen(false)
                .setAutoCancel(false);

        return builder.build();
    }

    /**
     * 경보기 알림 생성
     * @param context
     * @return
     */
    public static Notification createAlarmNotification(final Context context) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.setAction(AlarmManager.ACTION_ALARM_STOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String title = "긴급 경보음 작동";
        String content = "긴급 경보음 작동을 중지하려면 탭하세요.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_notification_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent)
                .setShowWhen(false)
                .setAutoCancel(false);

        return builder.build();
    }

    /**
     * 알림 생성
     * @param context
     * @param notificationId    알림 아이디
     * @param notification      알림
     */
    public static void show(final Context context, int notificationId, Notification notification) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(notificationId, notification);
    }

    /**
     * 알림 제거
     * @param context
     * @param notificationId    알림 아이디
     */
    public static void cancelNotification(final Context context, int notificationId) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancel(notificationId);
    }
}
