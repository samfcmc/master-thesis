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

import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplaceslib.datastore.ClientDataStore;
import com.sam.smartplaceslib.datastore.ClientParseDataStore;
import com.sam.smartplaceslib.exception.CannotFindParseJsonFile;
import com.sam.smartplaceslib.statistics.LogDReporter;
import com.sam.smartplaceslib.statistics.Statistics;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.IOException;

/**
 *
 */
public class SmartPlacesClientApplication extends Application implements BootstrapNotifier {

    private Region region;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private ClientDataStore dataStore;
    private int notificationId = 0;
    private Statistics statistics;

    private BeaconsManager beaconsManager;

    public static final int REQUEST_LOGIN = 2;

    public void onCreate() {
        super.onCreate();
        this.beaconsManager = new IBeaconsManager(this);
        LogDReporter reporter = new LogDReporter();
        this.statistics = new Statistics(reporter);
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
            this.dataStore = ClientParseDataStore.fromRawJsonResource(this, R.raw.parse, this.statistics);
        } catch (IOException e) {
            logToDisplay("Error creating data store object");
            throw new CannotFindParseJsonFile();
        }
    }

    public ClientDataStore getDataStore() {
        return this.dataStore;
    }

    @Override
    public void didEnterRegion(Region region) {
        logToDisplay("Enter region");
    }

    @Override
    public void didExitRegion(Region region) {
        logToDisplay("Exit region");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        logToDisplay("State for region");
    }

    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public BeaconsManager getBeaconsManager() {
        return beaconsManager;
    }

    public void createNotification(Context context, String title, String text, Intent resultIntent, Class<? extends Activity> activityClass) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        int id = newNotificationId();
        notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_more);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(text);
        notificationBuilder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilder.build());
    }

    private int newNotificationId() {
        return notificationId++;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
