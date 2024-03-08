package com.roseno.curbcrime.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.roseno.curbcrime.listener.ShakeDetectListener;
import com.roseno.curbcrime.manager.SharedPreferenceManager;

public class ShakeDetector implements SensorEventListener {
    private final String TAG = "ShakeDetector";

    public static final String PREFERENCE_KEY_SHAKE_DETECT_COUNT = "SHAKE_DETECT_COUNT";
    public static final String PREFERENCE_KEY_SHAKE_DETECT_THRESHOLD_RATE = "SHAKE_DETECT_THRESHOLD_RATE";

    public static final String DEFAULT_SHAKE_DETECT_THRESHOLD_RATE = "1.6";     // arrays.xml 에서 String만 지원
    public static final String DEFAULT_SHAKE_DETECT_COUNT = "6";
    
    private static final int SHAKE_DETECT_TIME_THRESHOLD_MS = 300;
    private static final int SHAKE_RESET_INTERVAL_MS = 3000;

    private static final float EARTH_GRAVITY = 1F;

    private final SensorManager sensorManager;
    private final Sensor sensor;

    private final ShakeDetectListener shakeDetectListener;

    private float shakeDetectThreshold;                 // 흔드는 세기 기준
    private int shakeDetectCount;                       // 흔드는 횟수 기준

    private long firstShakeTime = 0;
    private long lastShakeTime = 0;
    private int shakeCount = 0;

    public ShakeDetector(final Context context, ShakeDetectListener shakeDetectListener) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.shakeDetectListener = shakeDetectListener;

        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(context);
        float thresholdRate = Float.parseFloat(sharedPreferenceManager.getString(PREFERENCE_KEY_SHAKE_DETECT_THRESHOLD_RATE));

        shakeDetectThreshold = EARTH_GRAVITY * thresholdRate;
        shakeDetectCount = Integer.parseInt(sharedPreferenceManager.getString(PREFERENCE_KEY_SHAKE_DETECT_COUNT));
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

    public void setShakeDetectThreshold(float shakeDetectThreshold) {
        this.shakeDetectThreshold = shakeDetectThreshold;
    }

    public void setShakeDetectCount(int shakeDetectCount) {
        this.shakeDetectCount = shakeDetectCount;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0] / SensorManager.GRAVITY_EARTH;
        float y = sensorEvent.values[1] / SensorManager.GRAVITY_EARTH;
        float z = sensorEvent.values[2] / SensorManager.GRAVITY_EARTH;

        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        if (acceleration > shakeDetectThreshold) {
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
            if (shakeCount >= shakeDetectCount) {
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
