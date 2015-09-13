package com.sam.smartplaceslib.bluetooth;

import com.sam.smartplaceslib.datastore.BeaconInfo;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class BeaconsCache {
    private List<BeaconInfo> beacons;
    private ClearCacheCallback callback;

    public BeaconsCache() {
        this.beacons = new LinkedList<>();
    }

    public BeaconsCache(ClearCacheCallback callback) {
        this();
        this.callback = callback;
    }

    public void addBeacon(BeaconInfo beaconInfo) {
        synchronized (this) {
            beacons.add(beaconInfo);
        }
    }

    public boolean contains(BeaconInfo beaconInfo) {
        synchronized (this) {
            return beacons.contains(beaconInfo);
        }
    }

    public void clear() {
        synchronized (this) {
            beacons.clear();
        }
        if (callback != null) {
            callback.onCacheClear();
        }
    }
}
