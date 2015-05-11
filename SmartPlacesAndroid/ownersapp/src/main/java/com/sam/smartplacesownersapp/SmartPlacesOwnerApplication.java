package com.sam.smartplacesownersapp;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplaceslib.datastore.DataStore;
import com.sam.smartplaceslib.datastore.ParseDataStore;
import com.sam.smartplaceslib.exception.CannotFindParseJsonFile;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.IOException;

/**
 * Application class
 */
public class SmartPlacesOwnerApplication extends Application implements BootstrapNotifier {

    private Region region;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private DataStore<ParseUser, ParseException> dataStore;

    private BeaconsManager<Beacon> beaconsManager;

    private ProgressDialog progressDialog;

    public static final int REQUEST_LOGIN = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        this.beaconsManager = new IBeaconsManager(this);
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
    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public DataStore<ParseUser, ParseException> getDataStore() {
        return dataStore;
    }

    public BeaconsManager<Beacon> getBeaconsManager() {
        return beaconsManager;
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

    public void showProgressDialog(final Activity activity, final String title, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = ProgressDialog.show(activity, title, message);
            }
        });
    }

    public void showProgressDialog(final Activity activity, final String title) {
        showProgressDialog(activity, title, title);
    }

    public void showProgressDialog(final Activity activity) {
        String loadingStr = getString(R.string.loading);
        showProgressDialog(activity, loadingStr, loadingStr);
    }

    public void dismissProgressDialog(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }
}
