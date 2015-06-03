package com.sam.smartplacesclientapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.smartplacesclientapp.Keys;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.bluetooth.ibeacon.IBeaconScanCallback;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.List;


public class BeaconScanActivity extends ActionBarActivity implements IBeaconScanCallback {

    private SmartPlacesClientApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);
        this.application = (SmartPlacesClientApplication) getApplication();
        this.application.getBeaconsManager().startScan(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beacon_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_turn_of_bt) {
            this.application.getBeaconsManager().stopScan();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        this.application.getDataStore().logout(new LogoutCallback() {
            @Override
            public void done(DataStoreException exception) {
                if (exception == null) {
                    finish();
                }
            }
        });
    }

    @Override
    public void beaconsFound(Collection<Beacon> beacons) {
        BeaconsManager<Beacon> beaconsManager = this.application.getBeaconsManager();
        if (!beacons.isEmpty()) {
            this.application.getBeaconsManager().stopScan();
            Beacon beacon = beaconsManager.getNearestBeacon(beacons);
            String uuid = beacon.getId1().toHexString();
            int major = beacon.getId2().toInt();
            int minor = beacon.getId3().toInt();
            this.application.getDataStore().getBeacon(uuid, major, minor, new BeaconCallback() {
                @Override
                public void done(BeaconObject object) {
                    beaconFetched(object);
                }
            });
            logToDisplay("Detected beacon " + beacon.getId1().toHexString());
        }
    }

    private void beaconFetched(final BeaconObject object) {
        this.application.getDataStore().getSmartPlaces(object, new SmartPlacesCallback() {
            @Override
            public void done(List<SmartPlaceObject> list) {
                logToDisplay("Found smart places");
                notifyAboutSmartPlaces(list, object);
            }
        });
    }

    private void logToDisplay(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BeaconScanActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        //this.application.getBeaconsManager().unbind();
        super.onDestroy();
    }

    private void notifyAboutSmartPlaces(List<SmartPlaceObject> smartPlaces, BeaconObject beacon) {
        for (SmartPlaceObject smartPlace : smartPlaces) {
            Intent resultIntent = new Intent(this, SmartPlaceActivity.class);
            String url = smartPlace.getUrl();
            String name = smartPlace.getName();
            String message = smartPlace.getMessage();
            resultIntent.putExtra(Keys.URL, url);
            resultIntent.putExtra(Keys.NAME, name);
            resultIntent.putExtra(Keys.MESSAGE, message);
            resultIntent.putExtra(Keys.SMART_PLACE, smartPlace.getId());
            resultIntent.putExtra(Keys.BEACON, beacon.getId());
            // TODO: Go to an activity that will scan for beacons but this time to map them to objects
            // TODO: Requires some changes in the backend
            this.application.createNotification(this, smartPlace.getName(), smartPlace.getMessage(), resultIntent, SmartPlaceActivity.class);
        }
    }
}
