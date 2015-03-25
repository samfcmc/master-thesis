package com.sam.smartplacesclientapp.bluetooth;

import android.content.Context;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

/**
 *
 */
public class IBeaconManager {

    public static BeaconManager getInstance(Context context) {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(context);
        BeaconParser parser = new IBeaconParser();
        beaconManager.getBeaconParsers().add(parser);
        return beaconManager;
    }
}
