package com.roseno.curbcrime.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.manager.PermissionsManager;
import com.roseno.curbcrime.manager.ServiceManager;
import com.roseno.curbcrime.service.MainService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final String TAG = "MainActivity";

    public static final int SPLASH_SCREEN_DURATION_MS = 1500;

    private final Class<SettingsActivity> settingActivity = SettingsActivity.class;
    private final Class<MainService> mainService = MainService.class;

    private boolean isSplashVisible = true;

    private Intent alarmIntent;

    TextView mGuideTextView;

    ImageButton mSettingsButton;
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

        alarmIntent = new Intent(this, mainService);
        alarmIntent.setAction(MainService.ACTION_SERVICE_START);

        mGuideTextView = findViewById(R.id.textView_activity_main_guide);

        mSettingsButton = findViewById(R.id.imageButton_activity_main_settings);
        mSettingsButton.setOnClickListener(this);

        mAlarmButton = findViewById(R.id.toggleButton_activity_main_alarm);
        mAlarmButton.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 서비스 상태에 따라 버튼 상태 유지
        boolean isRunning = ServiceManager.isRunning(this, mainService);

        mAlarmButton.setOnCheckedChangeListener(null);
        mAlarmButton.setChecked(isRunning);

        mAlarmButton.setOnCheckedChangeListener(this);

        String guide = isRunning? "경보기를 눌러 종료하세요" : "경보기를 눌러 실행하세요";
        mGuideTextView.setText(guide);
    }

    /**
     * 버튼 이벤트
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int buttonId = v.getId();

        if (buttonId == mSettingsButton.getId()) {
            // 설정 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), settingActivity);
            startActivity(intent);
        }
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
                if (PermissionsManager.hasPermissions(this)) {
                    startService(alarmIntent);

                    mGuideTextView.setText("경보기를 눌러 종료하세요");
                } else {
                    // 권한 거부 시, 권한 요청 화면으로 이동
                    PermissionsManager.requestPermissionsThroughSettings(this);

                    mAlarmButton.setChecked(false);
                }
            } else {
                // 안전 시스템 종료
                stopService(alarmIntent);

                mGuideTextView.setText("경보기를 눌러 실행하세요");
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