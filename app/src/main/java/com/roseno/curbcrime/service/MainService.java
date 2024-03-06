package com.roseno.curbcrime.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.roseno.curbcrime.detector.LocationDetector;
import com.roseno.curbcrime.detector.ShakeDetector;
import com.roseno.curbcrime.listener.LocationDetectListener;
import com.roseno.curbcrime.listener.ShakeDetectListener;
import com.roseno.curbcrime.manager.AlarmManager;
import com.roseno.curbcrime.provider.NotificationProvider;
import com.roseno.curbcrime.util.LocationGeocoder;
import com.roseno.curbcrime.util.MessageSender;

import java.io.IOException;

public class MainService extends Service implements ShakeDetectListener, LocationDetectListener {
    private final String TAG = "MainService";

    public static final String ACTION_SERVICE_START = "ACTION_SERVICE_START";
    public static final String ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP";

    private ShakeDetector shakeDetector;
    private LocationDetector locationDetector;
    private AlarmManager alarmManager;

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

        if (alarmManager != null) {
            alarmManager.stopAlarm();
        }
    }

    /**
     * 가속도 탐지
     */
    @Override
    public void onDetect() {
        alarmManager = new AlarmManager(this);
        alarmManager.startAlarm();

        locationDetector = new LocationDetector(this, this);
        locationDetector.startDetection();
    }

    /**
     * 위치 탐지 성공
     * @param location      위치
     */
    @Override
    public void onDetect(Location location) {
        locationDetector.stopDetection();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        try {
            String address = LocationGeocoder.reverseGeocode(this, latitude, longitude);

            StringBuilder sb = new StringBuilder();
            sb.append("[CurbCrime] 도움 요청").append("\n");
            sb.append(String.format("[%s]에서 위험에 처해 있습니다.", address));

            String message = sb.toString();

            sendLocationMessage(message);
        } catch (IOException e) {
            onFailure();
        }
    }

    /**
     * 위치 탐지 실패
     */
    @Override
    public void onFailure() {
        StringBuilder sb = new StringBuilder();
        sb.append("[CurbCrime] 도움 요청").append("\n");
        sb.append("지금 위험에 처해 있습니다.");

        String failedMessage = sb.toString();

        sendLocationMessage(failedMessage);
    }

    /**
     * 위치 메시지 전송
     * @param message   메세지
     */
    public void sendLocationMessage(String message) {
        // TODO: 설정된 값에서 가져와야 한다.
        String target = "01030384537";
        
        MessageSender.sendMessage(target, message);
    }
}
