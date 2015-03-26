package com.sam.smartplacesclientapp.bluetooth.ibeacon;

import android.content.Context;

import com.sam.smartplacesclientapp.bluetooth.BeaconsManager;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconParser;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

/**
 *
 */
public class IBeaconsManager implements BeaconsManager {

    public static BeaconManager getInstance(Context context) {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(context);
        BeaconParser parser = new IBeaconParser();
        beaconManager.getBeaconParsers().add(parser);
        return beaconManager;
    }
}
