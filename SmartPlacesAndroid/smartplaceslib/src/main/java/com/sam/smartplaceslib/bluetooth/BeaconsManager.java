package com.sam.smartplaceslib.bluetooth;

import android.app.Activity;
import android.content.Context;

import java.util.Collection;

/**
 *
 */
public interface BeaconsManager<BeaconType> {
    public void startScan(Activity activity, BeaconScanCallback<BeaconType> callback);
    public void stopScan();
    public void unbind();
    public BeaconType getNearestBeacon(Collection<BeaconType> beacons);
    public void startScan(BeaconScanCallback<BeaconType> callback);
    public boolean isBluetoothTurnedOn(Context context);
    public void askToTurnBluetoothOn(Activity activity, int requestCode);
}
