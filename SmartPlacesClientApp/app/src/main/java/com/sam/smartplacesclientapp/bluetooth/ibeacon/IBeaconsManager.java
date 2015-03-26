package com.sam.smartplacesclientapp.bluetooth.ibeacon;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.sam.smartplacesclientapp.bluetooth.BeaconScanCallback;
import com.sam.smartplacesclientapp.bluetooth.BeaconsManager;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconParser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 *
 */
public class IBeaconsManager implements BeaconsManager, BeaconConsumer {

    private BeaconManager beaconManager;
    private Region region;
    private Context context;
    private BeaconScanCallback scanCallback;

    public IBeaconsManager(Context context) {
        this.beaconManager = BeaconManager.getInstanceForApplication(context);
        BeaconParser parser = new IBeaconParser();
        beaconManager.getBeaconParsers().add(parser);
        this.region = new Region("myRegion", null, null, null);
        this.context = context;
    }

    @Override
    public void onBeaconServiceConnect() {
        this.beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                IBeaconsManager.this.scanCallback.beaconsFound(beacons);
            }
        });

        try {
            this.beaconManager.startRangingBeaconsInRegion(this.region);
        } catch (RemoteException e) {
            throw new RuntimeException("Remote exception in start beacons ranging");
        }
    }

    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent, serviceConnection, i);
    }

    @Override
    public void startScan(BeaconScanCallback callback) {
        this.scanCallback = callback;
        this.beaconManager.bind(this);
    }

    @Override
    public void stopScan() {
        try {
            this.beaconManager.stopRangingBeaconsInRegion(this.region);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unbind() {
        this.beaconManager.unbind(this);
    }

}
