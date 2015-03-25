package com.sam.smartplacesclientapp;

import android.app.Application;
import android.widget.Toast;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

/**
 *
 */
public class SmartPlacesApplication extends Application implements BootstrapNotifier {

    private Region region;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;

    public void onCreate() {
        super.onCreate();
        this.region = new Region("backgroundRegion", null, null, null);
        this.regionBootstrap = new RegionBootstrap(this, region);
        this.backgroundPowerSaver = new BackgroundPowerSaver(this);

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
