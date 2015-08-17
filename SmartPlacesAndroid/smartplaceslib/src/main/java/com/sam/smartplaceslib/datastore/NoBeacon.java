package com.sam.smartplaceslib.datastore;

/**
 * NoBeacon: Used to represent a null beacon info object
 */
public class NoBeacon extends BeaconInfo {
    private static NoBeacon instance;

    private NoBeacon() {
        super("INVALID UUID", 0, 0, Integer.MAX_VALUE);
    }

    public static NoBeacon getInstance() {
        if (instance == null) {
            instance = new NoBeacon();
        }
        return instance;
    }
}
