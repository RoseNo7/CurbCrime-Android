package com.roseno.curbcrime.fragment.settings;

import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.adapter.AlarmAdapter;
import com.roseno.curbcrime.manager.AlarmManager;
import com.roseno.curbcrime.manager.SharedPreferenceManager;
import com.roseno.curbcrime.model.AlarmSound;

import java.util.ArrayList;
import java.util.List;

public class AlarmPreferenceFragment extends Fragment {

    RecyclerView mAlarmRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_alarm, container, false);

        mAlarmRecyclerView = view.findViewById(R.id.recyclerView_fragment_settings_alarm);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int selectedSound = -1;

        // 설정 정보 조회
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(getContext());
        int resId = sharedPreferenceManager.getInt(AlarmManager.PREFERENCE_KEY_SELECTED_SOUND);

        String[] alarmNames = getResources().getStringArray(R.array.alarm_sound_list_entries);
        TypedArray alarmResources = getResources().obtainTypedArray(R.array.alarm_sound_list_values);

        List<AlarmSound> alarmSounds = new ArrayList<>();
        for (int i = 0; i < alarmNames.length; i++) {
            int currentResId = alarmResources.getResourceId(i, AlarmManager.DEFAULT_SOUND);

            if (resId == currentResId) {
                selectedSound = i;
            }

            AlarmSound alarmSound = new AlarmSound();
            alarmSound.setName(alarmNames[i]);
            alarmSound.setUri(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + currentResId));

            alarmSounds.add(alarmSound);
        }

        alarmResources.recycle();

        AlarmAdapter alarmAdapter = new AlarmAdapter(alarmSounds, selectedSound);
        mAlarmRecyclerView.setAdapter(alarmAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        AlarmManager.stop();
    }
}

