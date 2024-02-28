package com.roseno.curbcrime.detector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.roseno.curbcrime.listener.LocationDetectListener;

public class LocationDetector implements LocationListener, Runnable {
    private final String TAG = "LocationDetect";

    private static final int DETECT_TIMEOUT_MS = 5000;

    private final LocationDetectListener locationDetectListener;

    private final LocationManager locationManager;
    private Handler handler;

    public LocationDetector(final Context context, LocationDetectListener locationDetectListener) {
        this.locationDetectListener = locationDetectListener;

        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 위치 센서 실행
     */
    @SuppressLint("MissingPermission")
    public void startDetection() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0.0f,
                this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0,
                0.0f,
                this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER,
                    0,
                    0.0f,
                    this);
        }

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this, DETECT_TIMEOUT_MS);
    }

    /**
     * 위치 센서 종료
     */
    public void stopDetection() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        locationDetectListener.onDetect(location);
    }

    /**
     * 탐지 시간 초과 시, 탐지 실패 처리
     */
    @Override
    public void run() {
        locationManager.removeUpdates(this);

        locationDetectListener.onFailure();
    }
}
