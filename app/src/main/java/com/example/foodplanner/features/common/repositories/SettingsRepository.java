package com.example.foodplanner.features.common.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodplanner.features.common.services.SettingsManager;

public class SettingsRepository implements SettingsManager {
    private static final String SETTINGS_FILE = "app_settings";
    private static final String HAS_SHOWN_LANDING = "HAS_SHOWN_LANDING";

    private final SharedPreferences preferences;

    public SettingsRepository(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static SettingsRepository create(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        return new SettingsRepository(sharedPreferences);
    }

    @Override
    public Boolean hasShownLanding() {
        String value = preferences.getString(HAS_SHOWN_LANDING, null);
        if (value == null) return null;
        return Boolean.valueOf(value);
    }

    @Override
    public void setHasShownLanding(boolean value) {
        preferences.edit().putString(HAS_SHOWN_LANDING, String.valueOf(value)).apply();
    }
}
