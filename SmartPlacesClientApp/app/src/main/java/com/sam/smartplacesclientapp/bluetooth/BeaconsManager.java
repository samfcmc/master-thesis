package com.sam.smartplacesclientapp.bluetooth;

import java.util.Collection;

/**
 *
 */
public interface BeaconsManager<BeaconType> {
    public void startScan(BeaconScanCallback<BeaconType> callback);
    public void stopScan();
    public void unbind();
    public BeaconType getNearestBeacon(Collection<BeaconType> beacons);
}
