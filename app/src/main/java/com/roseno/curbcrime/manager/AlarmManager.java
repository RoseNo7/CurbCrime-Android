package com.roseno.curbcrime.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.roseno.curbcrime.R;

import java.io.IOException;

public class AlarmManager {
    private static final String TAG = "AlarmManager";

    public static final String PREFERENCE_KEY_SELECTED_SOUND = "ALARM_SOUND";

    public static final String ACTION_ALARM_STOP = "ACTION_ALARM_STOP";

    public static final int DEFAULT_SOUND = R.raw.alarm_basic;

    private static MediaPlayer mediaPlayer;

    public static void start(final Context context) {
        SharedPreferenceManager sharedPreferManager = SharedPreferenceManager.getInstance(context);
        int resourceId = sharedPreferManager.getInt(PREFERENCE_KEY_SELECTED_SOUND, DEFAULT_SOUND);

        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId);

        start(context, uri);
    }

    public static void start(final Context context, Uri uri) {
        stop();

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "start: MediaPlayer prepare failed");
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
