package com.sam.smartplaceslib.bluetooth;

import android.app.Activity;
import android.content.Context;

import com.sam.smartplaceslib.datastore.BeaconInfo;

import java.util.Collection;

/**
 *
 */
public interface BeaconsManager {
    void startScan(Activity activity, BeaconScanCallback callback);

    void stopScan();

    void unbind();

    BeaconInfo getNearestBeacon(Collection<BeaconInfo> beacons);

    void startScan(BeaconScanCallback callback);

    boolean isBluetoothTurnedOn(Context context);

    void askToTurnBluetoothOn(Activity activity, int requestCode);

    void setBackgroundMode(boolean backgroundMode);
}
