package com.sam.smartplacesclientapp.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.bluetooth.IBeaconManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class MainActivity extends ActionBarActivity implements BeaconConsumer {

    private BeaconManager iBeaconManager;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.iBeaconManager = IBeaconManager.getInstance(this);
        this.region = new Region("myRegion", null, null, null);
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

    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBeaconServiceConnect() {
        this.iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size() > 0) {
                    onBeaconsDetected(beacons);
                }

            }
        });
    }

    private void onBeaconsDetected(Collection<Beacon> beacons) {
        Beacon beacon = getNearestBeacon(beacons);
        logToDisplay("Detected beacon " + beacon.getId1());

    }

    private Beacon getNearestBeacon(Collection<Beacon> beacons) {
        //TODO: Get the real nearest beacon
        return beacons.iterator().next();
    }
}
