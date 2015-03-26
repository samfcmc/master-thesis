package com.sam.smartplacesclientapp.bluetooth;

import java.util.Collection;

/**
 *
 */
public interface BeaconScanCallback<T> {
    public void beaconsFound(Collection<T> beacons);
}
