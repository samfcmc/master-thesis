package com.sam.smartplacesclientapp;

import android.app.Application;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.sam.smartplacesclientapp.datastore.DataStore;
import com.sam.smartplacesclientapp.datastore.ParseDataStore;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.IOException;

/**
 *
 */
public class SmartPlacesApplication extends Application implements BootstrapNotifier {

    private Region region;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private DataStore dataStore;

    public static final int REQUEST_LOGIN = 2;

    public void onCreate() {
        super.onCreate();
        initBeaconLib();
        initDataStore();
    }

    private void initBeaconLib() {
        this.region = new Region("backgroundRegion", null, null, null);
        this.regionBootstrap = new RegionBootstrap(this, region);
        this.backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    private void initDataStore() {
        try {
            this.dataStore = ParseDataStore.fromRawJsonResource(this, R.raw.parse);
        } catch (IOException e) {
            logToDisplay("Error creating data store object");
            throw new CannotFindParseJsonFile();
        }
    }

    public DataStore getDataStore() {
        return this.dataStore;
    }

    @Override
    public void didEnterRegion(org.altbeacon.beacon.Region region) {
        logToDisplay("Enter region");
    }

    @Override
    public void didExitRegion(org.altbeacon.beacon.Region region) {
        logToDisplay("Exit region");
    }

    @Override
    public void didDetermineStateForRegion(int i, org.altbeacon.beacon.Region region) {
        logToDisplay("State for region");
    }

    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
