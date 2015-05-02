package com.sam.smartplacesownerapp;

import android.app.Application;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.sam.smartplacesownerapp.bluetooth.BeaconsManager;
import com.sam.smartplacesownerapp.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplacesownerapp.datastore.DataStore;
import com.sam.smartplacesownerapp.datastore.ParseDataStore;
import com.sam.smartplacesownerapp.exception.CannotFindParseJsonFile;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.IOException;

/**
 * Application class
 */
public class SmartPlacesOwnerApplication extends Application implements BootstrapNotifier{

    private Region region;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private DataStore<ParseUser, ParseException> dataStore;

    private BeaconsManager<Beacon> beaconsManager;

    public static final int REQUEST_LOGIN = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        this.beaconsManager = new IBeaconsManager(this);
        initBeaconLib();
        initDataStore();
    }

    private void initDataStore() {
        try {
            this.dataStore = ParseDataStore.fromRawJsonResource(this, R.raw.parse);
        } catch (IOException e) {
            logToDisplay("Error creating data store object");
            throw new CannotFindParseJsonFile();
        }
    }

    private void initBeaconLib() {
        this.region = new Region("backgroundRegion", null, null, null);
        this.regionBootstrap = new RegionBootstrap(this, region);
        this.backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public BeaconsManager<Beacon> getBeaconsManager() {
        return beaconsManager;
    }

    public DataStore<ParseUser, ParseException> getDataStore() {
        return dataStore;
    }
}
