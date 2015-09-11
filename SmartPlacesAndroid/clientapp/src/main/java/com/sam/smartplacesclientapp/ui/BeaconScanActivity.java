package com.sam.smartplacesclientapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.smartplacesclientapp.Keys;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BeaconScanActivity extends AppCompatActivity implements BeaconScanCallback {

    private SmartPlacesClientApplication application;
    private List<BeaconInfo> detectedBeacons;
    private Map<String, SmartPlaceInstanceObject> instancesFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);
        this.application = (SmartPlacesClientApplication) getApplication();
        this.detectedBeacons = new LinkedList<>();
        this.instancesFound = new HashMap<>();
        this.application.getBeaconsManager().startScan(this, this);
        this.application.getStatistics().startSession(this.application, this.application.getBeaconsManager());
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
            turnScanOff();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_settings) {
            settings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void turnScanOff() {
        BeaconsManager beaconsManager = this.application.getBeaconsManager();
        beaconsManager.stopScan();
        finish();
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
    public void beaconsFound(Collection<BeaconInfo> beacons) {
        BeaconsManager beaconsManager = this.application.getBeaconsManager();
        if (!beacons.isEmpty()) {
            final BeaconInfo beacon = beaconsManager.getNearestBeacon(beacons);
            if (!detectedBeacons.contains(beacon)) {
                this.application.getDataStore().getBeacon(beacon, new BeaconCallback() {
                    @Override
                    public void done(BeaconObject object) {
                        if (object != null) {
                            detectedBeacons.add(beacon);
                            beaconFetched(object);
                        }
                    }
                });
            }
            logToDisplay("Detected beacons");
        }
    }

    private void beaconFetched(final BeaconObject object) {
        this.application.getDataStore().getSmartPlaceInstances(object, new SmartPlaceInstancesCallback() {
            @Override
            public void done(List<SmartPlaceInstanceObject> list) {
                logToDisplay("Found smart places " + list.size());
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
        logToDisplay("On destroy");
        if (!this.application.getStatistics().isBackgroundMode()) {
            this.application.getBeaconsManager().stopScan();
            this.application.getBeaconsManager().unbind();
            logToDisplay("Stop scanning");
        } else {
            logToDisplay("Scanning in background");
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logToDisplay("Pause");
        this.application.getBeaconsManager().setBackgroundMode(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        logToDisplay("Resume");
        this.application.getBeaconsManager().setBackgroundMode(false);
    }

    private void notifyAboutSmartPlaces(List<SmartPlaceInstanceObject> list, final BeaconObject beacon) {
        for (final SmartPlaceInstanceObject instance : list) {
            SmartPlaceInstanceObject found = instancesFound.get(instance.getId());
            if (found == null) {
                createNotification(beacon, instance);
                instancesFound.put(instance.getId(), instance);
            }

        }
    }

    private void createNotification(BeaconObject beacon,
                                    SmartPlaceInstanceObject instance) {
        Intent resultIntent = new Intent(this, SmartPlaceActivity.class);
        SmartPlaceObject smartPlace = instance.getSmartPlace();
        String url = smartPlace.getUrl();
        String name = instance.getTitle();
        String message = instance.getMessage();
        resultIntent.putExtra(Keys.URL, url);
        resultIntent.putExtra(Keys.NAME, name);
        resultIntent.putExtra(Keys.MESSAGE, message);
        resultIntent.putExtra(Keys.SMART_PLACE, smartPlace.getId());
        resultIntent.putExtra(Keys.BEACON, beacon.getId());
        resultIntent.putExtra(Keys.SMART_PLACE_CONFIGURATION, instance.getId());
        this.application.createNotification(this, name, message, resultIntent, SmartPlaceActivity.class);
    }
}
