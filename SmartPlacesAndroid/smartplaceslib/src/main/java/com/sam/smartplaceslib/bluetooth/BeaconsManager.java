package com.sam.smartplaceslib.bluetooth;

import android.app.Activity;
import android.content.Context;

import com.sam.smartplaceslib.datastore.BeaconInfo;

import java.util.Collection;

/**
 *
 */
public interface BeaconsManager {
    public void startScan(Activity activity, BeaconScanCallback callback);

    public void stopScan();

    public void unbind();

    public BeaconInfo getNearestBeacon(Collection<BeaconInfo> beacons);

    public void startScan(BeaconScanCallback callback);

    public boolean isBluetoothTurnedOn(Context context);

    public void askToTurnBluetoothOn(Activity activity, int requestCode);
}
