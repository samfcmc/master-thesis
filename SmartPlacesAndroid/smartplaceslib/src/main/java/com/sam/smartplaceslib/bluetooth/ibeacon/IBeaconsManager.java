package com.sam.smartplaceslib.bluetooth.ibeacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.sam.smartplaceslib.R;
import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.utils.Settings;

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
public class IBeaconsManager implements BeaconsManager, BeaconConsumer {

    private BeaconManager beaconManager;
    private Region region;
    private Context context;
    private Activity activity;
    private BeaconScanCallback scanCallback;
    private static final long FOREGROUND_SCAN_PERIOD = 10000;
    private Settings settings;

    public IBeaconsManager(Context context) {
        this.beaconManager = BeaconManager.getInstanceForApplication(context);
        BeaconParser parser = new IBeaconParser();
        EstimoeBeaconParser estimoeBeaconParser = new EstimoeBeaconParser();
        this.region = new Region("myRegion", null, null, null);
        this.context = context;
        beaconManager.getBeaconParsers().add(parser);
        beaconManager.getBeaconParsers().add(estimoeBeaconParser);
        this.settings = new Settings(context);
    }

    @Override
    public void onBeaconServiceConnect() {
        long scanPeriodBackground = settings.getScanPeriodInBackgroundMode();
        long scanPeriodForeground = settings.getScanPeriodInForegroundMode();
        beaconManager.setForegroundBetweenScanPeriod(scanPeriodForeground);
        beaconManager.setBackgroundBetweenScanPeriod(scanPeriodBackground);
        this.beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                didRange(beacons);
            }
        });

        try {
            this.beaconManager.startRangingBeaconsInRegion(this.region);
        } catch (RemoteException e) {
            throw new RuntimeException("Remote exception in startSession beacons ranging");
        }
    }

    private void didRange(Collection<Beacon> beacons) {
        List<Beacon> list = new ArrayList<Beacon>(beacons);
        Collections.sort(list, new BeaconComparator());
        this.scanCallback.beaconsFound(getBeaconInfoCollection(beacons));
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
    public void startScan(Activity activity, BeaconScanCallback callback) {
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
    public BeaconInfo getNearestBeacon(Collection<BeaconInfo> beacons) {
        BeaconInfo nearestBeacon = null;

        for (BeaconInfo beacon : beacons) {
            if (nearestBeacon == null || (beacon.getDistance() < nearestBeacon.getDistance())) {
                nearestBeacon = beacon;
            }
        }

        return nearestBeacon;
    }

    private Collection<BeaconInfo> getBeaconInfoCollection(Collection<Beacon> beacons) {
        List<BeaconInfo> list = new ArrayList<>(beacons.size());
        for (Beacon beacon : beacons) {
            list.add(getBeaconInfo(beacon));
        }
        return list;
    }

    private BeaconInfo getBeaconInfo(Beacon beacon) {
        String uuid = beacon.getId1().toUuid().toString().toUpperCase();
        int major = beacon.getId2().toInt();
        int minor = beacon.getId3().toInt();
        double distance = beacon.getDistance();
        BeaconInfo beaconInfo = new BeaconInfo(uuid, major, minor, distance);
        return beaconInfo;
    }

    @Override
    public void startScan(BeaconScanCallback callback) {
        this.scanCallback = callback;
        this.beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                IBeaconsManager.this.scanCallback.beaconsFound(getBeaconInfoCollection(beacons));

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

    @Override
    public void setBackgroundMode(boolean backgroundMode) {
        if (beaconManager.isBound(this)) {
            beaconManager.setBackgroundMode(backgroundMode);
        }
    }

    @Override
    public void updateScanPeriodInBackgroundMode(long period) {
        beaconManager.setBackgroundBetweenScanPeriod(period);
        settings.setScanPeriodInBackgroundMode(period);
        try {
            beaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            throw new RuntimeException("Error updating background scan period");
        }
    }

    @Override
    public void updateScanPeriodInForegroundMode(long period) {
        beaconManager.setForegroundBetweenScanPeriod(period);
        settings.setScanPeriodInForegroundMode(period);
        try {
            beaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            throw new RuntimeException("Error updating foreground scan period");
        }
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

}
