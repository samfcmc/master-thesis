package com.sam.smartplacesownerapp.bluetooth.ibeacon;

import org.altbeacon.beacon.BeaconParser;

/**
 *
 */
public class IBeaconParser extends BeaconParser {

    private static final String BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    public IBeaconParser() {
        super();
        setBeaconLayout(BEACON_LAYOUT);
    }
}
