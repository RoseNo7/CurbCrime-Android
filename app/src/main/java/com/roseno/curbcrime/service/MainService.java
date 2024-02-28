package com.roseno.curbcrime.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.roseno.curbcrime.detector.ShakeDetector;
import com.roseno.curbcrime.listener.ShakeDetectListener;
import com.roseno.curbcrime.provider.NotificationProvider;

public class MainService extends Service implements ShakeDetectListener {
    private final String TAG = "MainService";

    public static final String ACTION_SERVICE_START = "ACTION_SERVICE_START";
    public static final String ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP";

    private ShakeDetector shakeDetector;
    
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        switch (action) {
            case ACTION_SERVICE_START:
                // 서비스 시작 요청
                Notification notification = NotificationProvider.createNotification(this);
                startForeground(NotificationProvider.NOTIFICATION_ID, notification);

                shakeDetector = new ShakeDetector(this, this);
                shakeDetector.startDetection();
                
                break;

            case ACTION_SERVICE_STOP:
                // 서비스 종료 요청
                stopSelf();

                break;
        }
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (shakeDetector != null) {
            shakeDetector.stopDetection();
            shakeDetector = null;
        }
    }

    /**
     * 가속도 탐지
     */
    @Override
    public void onDetect() {

    }
}
