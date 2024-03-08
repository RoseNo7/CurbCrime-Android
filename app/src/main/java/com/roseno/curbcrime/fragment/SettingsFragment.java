package com.roseno.curbcrime.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.detector.ShakeDetector;
import com.roseno.curbcrime.manager.ServiceManager;
import com.roseno.curbcrime.manager.SharedPreferenceManager;
import com.roseno.curbcrime.preference.MessageTargetPreference;
import com.roseno.curbcrime.service.MainService;
import com.roseno.curbcrime.util.MessageSender;

/**
 * /xml/settings_preferences.xml 에서 설정 목록 구성
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private static String TAG = "SettingsFragment";

    private final Class<MainService> mainService = MainService.class;

    ListPreference mShakeCountPreference;
    ListPreference mShakeThresholdPreference;

    SwitchPreference mMessageEnablePreference;
    MessageTargetPreference mMessageTargetPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShakeCountPreference = findPreference(ShakeDetector.PREFERENCE_KEY_SHAKE_DETECT_COUNT);
        mShakeCountPreference.setOnPreferenceChangeListener(this);

        mShakeThresholdPreference = findPreference(ShakeDetector.PREFERENCE_KEY_SHAKE_DETECT_THRESHOLD_RATE);
        mShakeThresholdPreference.setOnPreferenceChangeListener(this);

        mMessageEnablePreference = findPreference(MessageSender.PREFERENCE_KEY_SEND_ENABLE);
        mMessageEnablePreference.setOnPreferenceChangeListener(this);

        mMessageTargetPreference = findPreference(MessageSender.PREFERENCE_KEY_SEND_TARGET);

        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(getContext());
        boolean isEnableSendMessage = sharedPreferenceManager.getBoolean(MessageSender.PREFERENCE_KEY_SEND_ENABLE);

        mMessageTargetPreference.setEnabled(isEnableSendMessage);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        String key = preference.getKey();

        if (mShakeCountPreference.getKey().equals(key)) {
            // 서비스가 실행 중인 경우, 변경 사항을 바로 적용
            boolean isRunning = ServiceManager.isRunning(getContext(), mainService);

            if (isRunning) {
                MainService mainService = MainService.getInstance();
                ShakeDetector shakeDetector = mainService.getShakeDetector();

                shakeDetector.setShakeDetectCount(Integer.parseInt((String) newValue));
            }
        } else if (mShakeThresholdPreference.getKey().equals(key)) {
            // 서비스가 실행 중인 경우, 변경 사항을 바로 적용
            boolean isRunning = ServiceManager.isRunning(getContext(), mainService);

            if (isRunning) {
                MainService mainService = MainService.getInstance();
                ShakeDetector shakeDetector = mainService.getShakeDetector();

                shakeDetector.setShakeDetectThreshold(Float.parseFloat((String) newValue));
            }
        } else if (mMessageEnablePreference.getKey().equals(key)) {
            boolean isSwitchOn = (boolean) newValue;

            mMessageTargetPreference.setEnabled(isSwitchOn);
        }

        return true;
    }
}
