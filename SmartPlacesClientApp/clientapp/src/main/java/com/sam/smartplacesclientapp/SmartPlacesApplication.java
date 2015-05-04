package com.sam.smartplacesclientapp;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.sam.smartplacesclientapp.bluetooth.BeaconsManager;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplacesclientapp.datastore.DataStore;
import com.sam.smartplacesclientapp.datastore.ParseDataStore;
import com.sam.smartplacesclientapp.exception.CannotFindParseJsonFile;

import org.altbeacon.beacon.Beacon;
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
    private DataStore<ParseUser, ParseException> dataStore;

    private BeaconsManager<Beacon> beaconsManager;

    public static final int REQUEST_LOGIN = 2;

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

    public DataStore<ParseUser, ParseException> getDataStore() {
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

    public BeaconsManager<Beacon> getBeaconsManager() {
        return beaconsManager;
    }

    public void createNotification(Context context, String title, String text, Intent resultIntent, Class<? extends Activity> activityClass) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_more);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(text);
        notificationBuilder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(50, notificationBuilder.build());
    }
}
