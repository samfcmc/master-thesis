package com.sam.smartplaceslib.bluetooth.ibeacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class IBeaconsManager implements BeaconsManager<Beacon>, BeaconConsumer {

    private BeaconManager beaconManager;
    private Region region;
    private Context context;
    private Activity activity;
    private BeaconScanCallback scanCallback;

    public IBeaconsManager(Context context) {
        this.beaconManager = BeaconManager.getInstanceForApplication(context);
        BeaconParser parser = new IBeaconParser();
        this.region = new Region("myRegion", null, null, null);
        this.context = context;
        beaconManager.getBeaconParsers().add(parser);
    }

    @Override
    public void onBeaconServiceConnect() {
        this.beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                List<Beacon> list = new ArrayList<Beacon>(beacons);
                Collections.sort(list, new BeaconComparator());
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
        return activity.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        activity.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return activity.bindService(intent, serviceConnection, i);
    }

    @Override
    public void startScan(Activity activity, BeaconScanCallback<Beacon> callback) {
        this.scanCallback = callback;
        this.activity = activity;
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
        if (this.beaconManager.isBound(this)) {
            this.beaconManager.unbind(this);
        }
    }

    @Override
    public Beacon getNearestBeacon(Collection<Beacon> beacons) {
        Beacon nearestBeacon = null;

        for (Beacon beacon : beacons) {
            if (nearestBeacon == null || (beacon.getDistance() < nearestBeacon.getDistance())) {
                nearestBeacon = beacon;
            }
        }

        return nearestBeacon;
    }

    @Override
    public void startScan(BeaconScanCallback<Beacon> callback) {
        this.scanCallback = callback;
        this.beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                IBeaconsManager.this.scanCallback.beaconsFound(beacons);
            }
        });
        try {
            this.beaconManager.startRangingBeaconsInRegion(this.region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBluetoothTurnedOn(Context context) {
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
    }

    @Override
    public void askToTurnBluetoothOn(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

}
