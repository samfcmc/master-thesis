package com.sam.smartplacesclientapp.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesApplication;
import com.sam.smartplacesclientapp.bluetooth.BeaconsManager;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconScanCallback;
import com.sam.smartplacesclientapp.datastore.callback.BeaconCallback;
import com.sam.smartplacesclientapp.datastore.object.BeaconObject;

import org.altbeacon.beacon.Beacon;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;


public class BeaconScanActivity extends ActionBarActivity implements IBeaconScanCallback {

    private SmartPlacesApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);
        this.application = (SmartPlacesApplication) getApplication();
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beaconsFound(Collection<Beacon> beacons) {
        BeaconsManager<Beacon> beaconsManager = this.application.getBeaconsManager();
        if(!beacons.isEmpty()) {
            this.application.getBeaconsManager().stopScan();
            Beacon beacon = beaconsManager.getNearestBeacon(beacons);
            String uuid = beacon.getId1().toHexString();
            int major = beacon.getId2().toInt();
            int minor = beacon.getId3().toInt();
            this.application.getDataStore().getBeaon(uuid, major, minor, new BeaconCallback() {
                @Override
                public void done(BeaconObject object) {
                    if(object == null) {
                        logToDisplay("Beacon not found");
                    }
                    else {
                        createNotification(object);
                    }

                }
            });
            logToDisplay("Detected beacon " + beacon.getId1().toHexString());
        }
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
        super.onDestroy();
        this.application.getBeaconsManager().unbind();
    }

    private void createNotification(BeaconObject beaconObject) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_more);
        notificationBuilder.setContentTitle("Something for you");
        notificationBuilder.setContentText(beaconObject.getObject().toString());
        notificationBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(this, BeaconContentActivity.class);
        String url = buildUrl("http://beacons-demo.meteor.com/", beaconObject.getObject());
        resultIntent.putExtra("url", url);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(BeaconContentActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(50, notificationBuilder.build());
    }

    private String buildUrl(String baseUrl, JSONObject jsonObject) {
        String finalUrl = baseUrl + "?";
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            try {
                finalUrl += key + "=" + jsonObject.get(key).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return finalUrl;
    }
}
