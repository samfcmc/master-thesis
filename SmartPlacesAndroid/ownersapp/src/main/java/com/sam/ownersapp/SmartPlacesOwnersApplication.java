package com.sam.ownersapp;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplaceslib.datastore.OwnerDataStore;
import com.sam.smartplaceslib.datastore.OwnerParseDataStore;
import com.sam.smartplaceslib.exception.CannotFindParseJsonFile;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.IOException;

/**
 *
 */
public class SmartPlacesOwnersApplication extends Application implements BootstrapNotifier {
    private Region region;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private OwnerDataStore dataStore;

    private BeaconsManager beaconsManager;

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
            this.dataStore = OwnerParseDataStore.fromRawJsonResource(this, R.raw.parse);
        } catch (IOException e) {
            logToDisplay("Error creating data store object");
            throw new CannotFindParseJsonFile();
        }
    }

    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public OwnerDataStore getDataStore() {
        return dataStore;
    }

    public BeaconsManager getBeaconsManager() {
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

    public void logToDisplay(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void logToDisplay(Context context, int stringId) {
        String s = getString(stringId);
        logToDisplay(context, s);
    }
}
