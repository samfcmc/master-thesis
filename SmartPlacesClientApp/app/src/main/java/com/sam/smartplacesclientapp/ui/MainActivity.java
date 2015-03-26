package com.sam.smartplacesclientapp.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesApplication;
import com.sam.smartplacesclientapp.bluetooth.BeaconsManager;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconScanCallback;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplacesclientapp.datastore.callback.BeaconCallback;
import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;
import com.sam.smartplacesclientapp.datastore.object.BeaconObject;
import com.sam.smartplacesclientapp.datastore.object.DummyObject;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class MainActivity extends ActionBarActivity implements IBeaconScanCallback {

    private BeaconsManager beaconsManager;
    private BluetoothAdapter bluetoothAdapter;
    private Region region;
    private static final int REQUEST_ENABLE_BT = 0xFF;
    private SmartPlacesApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.beaconsManager = new IBeaconsManager(this);

        this.application = (SmartPlacesApplication) getApplication();
        initBluetooth();
        this.application.getDataStore().createDummy("test", new DummyCallback() {
            @Override
            public void done(DummyObject object) {
                logToDisplay("Dummy created " + object.getName());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.beaconsManager.unbind();
    }

    private void initBluetooth() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        if(this.bluetoothAdapter == null || !this.bluetoothAdapter.isEnabled()) {
            askToTurnOnBluetooth();
        }
        startBeaconScan();
    }

    private void startBeaconScan() {
        this.beaconsManager.startScan(this);
    }

    private void askToTurnOnBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ENABLE_BT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logToDisplay(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Beacon getNearestBeacon(Collection<Beacon> beacons) {
        //TODO: Get the real nearest beacon
        return beacons.iterator().next();
    }

    @Override
    public void beaconsFound(Collection<Beacon> beacons) {
        if(beacons.size() > 0) {
            onBeaconsFound(beacons);
        }
    }

    private void onBeaconsFound(Collection<Beacon> beacons) {
        this.beaconsManager.stopScan();
        logToDisplay("Error stoping ranging beacons");
        Beacon beacon = getNearestBeacon(beacons);
        String uuid = beacon.getId1().toHexString();
        int major = beacon.getId2().toInt();
        int minor = beacon.getId3().toInt();
        this.application.getDataStore().getBeaon(uuid, major, minor, new BeaconCallback() {
            @Override
            public void done(BeaconObject object) {
                if(object == null) {
                    logToDisplay("Beacon not found");
                }
                else {
                    updateText(object);
                }

            }
        });
        logToDisplay("Detected beacon " + beacon.getId1().toHexString());
    }

    private void updateText(BeaconObject beaconObject) {
        TextView textView = (TextView) findViewById(R.id.main_hello_textview);
        textView.setText(beaconObject.getObject().toString());
    }
}
