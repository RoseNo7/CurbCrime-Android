package com.roseno.curbcrime.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;
import android.os.Handler;

import com.roseno.curbcrime.R;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_SCREEN_DURATION_MS = 1500;

    private boolean isSplashVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(new KeepOnScreenCondition());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler splashDelayHandler = new Handler(getMainLooper());
        splashDelayHandler.postDelayed(new SplashDelayRunnable(), SPLASH_SCREEN_DURATION_MS);
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