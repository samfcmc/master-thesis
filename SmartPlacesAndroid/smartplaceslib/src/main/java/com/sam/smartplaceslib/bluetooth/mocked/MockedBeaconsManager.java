package com.sam.smartplaceslib.bluetooth.mocked;

import android.app.Activity;
import android.content.Context;

import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

/**
 * Mock of beacons manager that does not need real beacons
 */
public class MockedBeaconsManager implements BeaconsManager<Beacon> {

    @Override
    public void startScan(Activity activity, BeaconScanCallback<Beacon> callback) {

    }

    @Override
    public void stopScan() {

    }

    @Override
    public void unbind() {

    }

    @Override
    public Beacon getNearestBeacon(Collection<Beacon> beacons) {
        if(!beacons.isEmpty()) {
            Beacon nearestBeacon = beacons.iterator().next();
            for(Beacon beacon : beacons) {
                if(beacon.getDistance() < nearestBeacon.getDistance()) {
                    nearestBeacon = beacon;
                }
            }
            return nearestBeacon;
        }
        else {
            return null;
        }
    }

    @Override
    public void startScan(BeaconScanCallback<Beacon> callback) {

    }

    @Override
    public boolean isBluetoothTurnedOn(Context context) {
        // TODO
        return false;
    }

    @Override
    public void askToTurnBluetoothOn(Activity activity, int requestCode) {
        // TODO
    }
}
