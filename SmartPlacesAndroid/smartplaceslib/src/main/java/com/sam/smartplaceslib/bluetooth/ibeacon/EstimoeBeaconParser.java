package com.sam.smartplaceslib.bluetooth.ibeacon;

import org.altbeacon.beacon.BeaconParser;

/**
 *
 */
public class EstimoeBeaconParser extends BeaconParser {
    private static final String LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    public EstimoeBeaconParser() {
        super();
        setBeaconLayout(LAYOUT);
    }

}
