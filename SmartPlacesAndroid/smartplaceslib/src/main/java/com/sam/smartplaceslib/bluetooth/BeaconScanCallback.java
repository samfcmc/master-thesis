package com.sam.smartplaceslib.bluetooth;

import com.sam.smartplaceslib.datastore.BeaconInfo;

import java.util.Collection;

/**
 *
 */
public interface BeaconScanCallback {
    public void beaconsFound(Collection<BeaconInfo> beacons);
}
