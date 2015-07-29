package com.sam.smartplacesclientapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.smartplacesclientapp.Keys;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.ui.SmartPlacesWebView;
import com.sam.smartplaceslib.web.OnPageLoadedCallback;

import java.util.Collection;

public class SmartPlaceActivity extends ActionBarActivity implements BeaconScanCallback, OnPageLoadedCallback {

    private SmartPlacesWebView webView;

    private SmartPlacesClientApplication application;

    private String name;
    private String url;
    private String beaconId;
    private String smartPlaceId;
    private String smartPlaceInstanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_place);
        this.application = (SmartPlacesClientApplication) getApplication();
        initUI();
    }

    private void initUI() {
        this.webView = (SmartPlacesWebView) findViewById(R.id.smartplace_webView);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Keys.MESSAGE);
        this.name = intent.getStringExtra(Keys.NAME);
        this.url = intent.getStringExtra(Keys.URL);
        this.beaconId = intent.getStringExtra(Keys.BEACON);
        this.smartPlaceId = intent.getStringExtra(Keys.SMART_PLACE);
        this.smartPlaceInstanceId = intent.getStringExtra(Keys.SMART_PLACE_CONFIGURATION);
        setTitle(this.name);

        this.webView.setOnPageLoadedCallback(this);
        this.webView.loadUrl(this.url);
    }

    private void scanForNearbyObjects() {
        this.application.getBeaconsManager().startScan(this);
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
            this.application.getBeaconsManager().stopScan();
            BeaconInfo nearestBeacon = this.application.getBeaconsManager().getNearestBeacon(beacons);
            this.application.getDataStore().getTag(this.smartPlaceInstanceId, nearestBeacon,
                    new TagCallback() {
                        @Override
                        public void done(TagObject object) {
                            if (object == null) {
                                logToDisplay("tag not found");
                            } else {
                                logToDisplay("tag found " + object);
                                tagFound(object);
                            }

                        }
                    });
        }
    }

    private void tagFound(TagObject tagObject) {
        this.webView.tagFound(tagObject);
    }


    private void logToDisplay(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SmartPlaceActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void done() {
        scanForNearbyObjects();
    }
}
