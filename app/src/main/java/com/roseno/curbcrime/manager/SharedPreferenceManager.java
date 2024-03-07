package com.roseno.curbcrime.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceManager {
    private final String TAG = "SharedPreferenceManager";

    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;
    private static final long DEFAULT_VALUE_LONG = -1L;
    private static final float DEFAULT_VALUE_FLOAT = -1F;
    private static final String DEFAULT_VALUE_STRING = "";
    private static final Set<String> DEFAULT_VALUE_STRING_SET = new HashSet<>();

    private static SharedPreferenceManager instance;
    private final SharedPreferences preferences;

    private SharedPreferenceManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized SharedPreferenceManager getInstance(final Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }

        return instance;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_VALUE_BOOLEAN);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key) {
        return getInt(key, DEFAULT_VALUE_INT);
    }

    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return getLong(key, DEFAULT_VALUE_LONG);
    }

    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return getFloat(key, DEFAULT_VALUE_FLOAT);
    }

    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    public String getString(String key) {
        return getString(key, DEFAULT_VALUE_STRING);
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, DEFAULT_VALUE_STRING_SET);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        return preferences.getStringSet(key, defaultValues);
    }

    public void setBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void setInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void setLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void setFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void setStringSet(String key, Set<String> value) {
        preferences.edit().putStringSet(key, value).apply();
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
