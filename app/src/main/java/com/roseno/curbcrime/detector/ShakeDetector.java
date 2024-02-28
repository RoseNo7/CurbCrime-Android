package com.roseno.curbcrime.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.roseno.curbcrime.listener.ShakeDetectListener;

public class ShakeDetector implements SensorEventListener {
    private final String TAG = "ShakeDetector";

    private static final float SHAKE_THRESHOLD_MS = 1.5F;
    private static final int SHAKE_DETECT_TIME_THRESHOLD_MS = 500;
    private static final int SHAKE_COUNT = 5;

    private static final int SHAKE_RESET_INTERVAL_MS = 3000;

    private final SensorManager sensorManager;
    private final Sensor sensor;

    private final ShakeDetectListener shakeDetectListener;

    private long firstShakeTime = 0;
    private long lastShakeTime = 0;
    private int shakeCount = 0;

    public ShakeDetector(Context context, ShakeDetectListener shakeDetectListener) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.shakeDetectListener = shakeDetectListener;
    }

    /**
     * 가속도 센서 실행
     */
    public void startDetection() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 가속도 센서 종료
     */
    public void stopDetection() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0] / SensorManager.GRAVITY_EARTH;
        float y = sensorEvent.values[1] / SensorManager.GRAVITY_EARTH;
        float z = sensorEvent.values[2] / SensorManager.GRAVITY_EARTH;

        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        if (acceleration > SHAKE_THRESHOLD_MS) {
            final long currentTime = System.currentTimeMillis();

            if (shakeCount == 0) {
                firstShakeTime = currentTime;
            }

            // 가속도 탐지 간격
            if (currentTime - lastShakeTime < SHAKE_DETECT_TIME_THRESHOLD_MS) {
                return;
            }

            shakeCount++;
            lastShakeTime = currentTime;

            // 탐지
            if (shakeCount >= SHAKE_COUNT) {
                shakeDetectListener.onDetect();

                shakeCount = 0;
            }

            // 신호 간격
            if (currentTime - firstShakeTime > SHAKE_RESET_INTERVAL_MS) {
                shakeCount = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
