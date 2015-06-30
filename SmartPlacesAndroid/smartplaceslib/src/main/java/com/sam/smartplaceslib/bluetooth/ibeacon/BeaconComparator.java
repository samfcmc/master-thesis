package com.sam.smartplaceslib.bluetooth.ibeacon;

import org.altbeacon.beacon.Beacon;

import java.util.Comparator;

/**
 * Allow beacons to be sorted by distance
 */
public class BeaconComparator implements Comparator<Beacon> {
    @Override
    public int compare(Beacon beacon1, Beacon beacon2) {
        double distance1 = beacon1.getDistance();
        double distance2 = beacon2.getDistance();

        if (distance1 < distance2) {
            return -1;
        } else if (distance1 > distance2) {
            return 1;
        } else {
            return 0;
        }
    }
}
