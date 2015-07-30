package com.sam.smartplaceslib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.altbeacon.beacon.BeaconManager;

/**
 *
 */
public class Settings {
    private SharedPreferences preferences;
    public static final long DEFAULT_SCAN_PERIOD_BACKGROUND = BeaconManager.DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD;
    public static final long DEFAULT_SCAN_PERIOD_FOREGROUND = BeaconManager.DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD;
    private static final String PREFERENCES_NAME = Settings.class.getName();

    private static final String SCAN_PERIOD_BACKGROUND = "scanPeriodBackground";
    private static final String SCAN_PERIOD_FOREGROUND = "scanPeriodForeground";

    public Settings(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public long getScanPeriodInBackgroundMode() {
        return this.preferences.getLong(SCAN_PERIOD_BACKGROUND, DEFAULT_SCAN_PERIOD_BACKGROUND);
    }

    public long getScanPeriodInForegroundMode() {
        return this.preferences.getLong(SCAN_PERIOD_FOREGROUND, DEFAULT_SCAN_PERIOD_FOREGROUND);
    }

    public void setScanPeriodInBackgroundMode(long period) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putLong(SCAN_PERIOD_BACKGROUND, period);
        editor.commit();
    }

    public void setScanPeriodInForegroundMode(long period) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putLong(SCAN_PERIOD_FOREGROUND, period);
        editor.commit();
    }

    public void restoreDefaultValues() {
        setScanPeriodInBackgroundMode(DEFAULT_SCAN_PERIOD_BACKGROUND);
        setScanPeriodInForegroundMode(DEFAULT_SCAN_PERIOD_FOREGROUND);
    }

}
