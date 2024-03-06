package com.roseno.curbcrime.manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.roseno.curbcrime.R;

public class AlarmManager {
    private final String TAG = "AlarmManager";

    private final Context context;
    private final MediaPlayer mediaPlayer;

    public AlarmManager(final Context context) {
        this.context = context;

        // TODO: 알람을 선택할 수 있어야 한다.
        this.mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        this.mediaPlayer.setLooping(true);
    }

    public void startAlarm() {
        mediaPlayer.start();
    }

    public void stopAlarm() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
