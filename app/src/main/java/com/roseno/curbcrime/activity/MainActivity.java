package com.roseno.curbcrime.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.splashscreen.SplashScreen;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.manager.PermissionsManager;
import com.roseno.curbcrime.provider.NotificationProvider;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final int SPLASH_SCREEN_DURATION_MS = 1500;

    private boolean isSplashVisible = true;

    ToggleButton mAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 권한 요청
        PermissionsManager.requestPermissions(this);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(new KeepOnScreenCondition());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler splashDelayHandler = new Handler(getMainLooper());
        splashDelayHandler.postDelayed(new SplashDelayRunnable(), SPLASH_SCREEN_DURATION_MS);

        mAlarmButton = findViewById(R.id.toggleButton_activity_main_alarm);
        mAlarmButton.setOnCheckedChangeListener(this);
    }

    /**
     * 토글 버튼 이벤트
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int buttonId = buttonView.getId();
        
        if (buttonId == mAlarmButton.getId()) {
            if (isChecked) {
                // 안전 시스템 실행
                Notification notification = NotificationProvider.createNotification(this);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(NotificationProvider.NOTIFICATION_ID, notification);
            } else {
                // 안전 시스템 종료
                NotificationProvider.cancelNotification(this, NotificationProvider.NOTIFICATION_ID);
            }
        }
    }

    /**
     * Splash Screen 표시 조건
     */
    private class KeepOnScreenCondition implements SplashScreen.KeepOnScreenCondition {

        @Override
        public boolean shouldKeepOnScreen() {
            return isSplashVisible;
        }
    }

    /**
     * Splash Screen 표시 조건 변경
     */
    private class SplashDelayRunnable implements Runnable {

        @Override
        public void run() {
            isSplashVisible = false;
        }
    }
}