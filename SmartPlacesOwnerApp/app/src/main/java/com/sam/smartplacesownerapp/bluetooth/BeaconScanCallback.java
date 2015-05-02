package com.sam.smartplacesownerapp.bluetooth;

import java.util.Collection;

/**
 *
 */
public interface BeaconScanCallback<T> {
    public void beaconsFound(Collection<T> beacons);
}
