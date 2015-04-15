package com.sam.smartplacesclientapp.bluetooth;

import android.app.Activity;

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
}
