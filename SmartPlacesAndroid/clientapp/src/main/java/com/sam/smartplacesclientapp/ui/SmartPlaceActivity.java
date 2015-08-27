package com.sam.smartplacesclientapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sam.smartplacesclientapp.Keys;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.datastore.NoBeacon;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.metrics.Metrics;
import com.sam.smartplaceslib.ui.SmartPlacesWebView;
import com.sam.smartplaceslib.web.OnPageLoadedCallback;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SmartPlaceActivity extends ActionBarActivity implements BeaconScanCallback, OnPageLoadedCallback {

    private SmartPlacesWebView webView;

    private SmartPlacesClientApplication application;

    private String name;
    private String url;
    private String smartPlaceInstanceId;
    private BeaconInfo previousBeacon;

    Map<BeaconInfo, TagObject> detectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_place);
        this.detectedTags = new HashMap<>();
        this.application = (SmartPlacesClientApplication) getApplication();
        this.previousBeacon = NoBeacon.getInstance();
        this.application.getMetrics().start();
        initUI();
    }

    private void initUI() {
        this.webView = (SmartPlacesWebView) findViewById(R.id.smartplace_webView);
        Intent intent = getIntent();
        this.name = intent.getStringExtra(Keys.NAME);
        this.url = intent.getStringExtra(Keys.URL);
        this.smartPlaceInstanceId = intent.getStringExtra(Keys.SMART_PLACE_CONFIGURATION);
        setTitle(this.name);

        this.webView.setOnPageLoadedCallback(this);
        this.webView.clearCache(true);
        this.webView.loadUrl(this.url);
    }

    private void scanForNearbyObjects() {
        this.application.getBeaconsManager().setBackgroundMode(false);
        this.application.getBeaconsManager().startScan(this, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beaconsFound(Collection<BeaconInfo> beacons) {
        if (!beacons.isEmpty()) {
            final BeaconInfo nearestBeacon = this.application.getBeaconsManager().getNearestBeacon(beacons);
            if (!nearestBeacon.equals(previousBeacon)) {
                newNearestBeaconCount();
                previousBeacon = nearestBeacon;
            }
            TagObject foundTag = this.detectedTags.get(nearestBeacon);
            if (foundTag == null) {
                this.application.getDataStore().getTag(this.smartPlaceInstanceId, nearestBeacon,
                        new TagCallback() {
                            @Override
                            public void done(TagObject object) {
                                detectedTags.put(nearestBeacon, object);
                                if (object == null) {
                                } else {
                                    beaconScannedMetric(object.getBeacon());
                                    tagFound(object);
                                }

                            }
                        });
            } else {
                beaconScannedMetric(foundTag.getBeacon());
                tagFound(foundTag);
            }
        }
    }

    private void newNearestBeaconCount() {
        Metrics metrics = application.getMetrics();
        metrics.counterInc("New nearest beacon");
    }

    private void beaconScannedMetric(BeaconObject beaconObject) {
        Metrics metrics = application.getMetrics();
        metrics.value("Beacon scan", beaconObject.getName(), new Date());
    }

    private void tagFound(final TagObject tagObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.tagFound(tagObject);
            }
        });

    }

    @Override
    public void done() {
        application.getBeaconsManager().stopScan();
        scanForNearbyObjects();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BeaconsManager beaconsManager = this.application.getBeaconsManager();
        beaconsManager.stopScan();
        Metrics metrics = application.getMetrics();
        metrics.report();
        metrics.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
