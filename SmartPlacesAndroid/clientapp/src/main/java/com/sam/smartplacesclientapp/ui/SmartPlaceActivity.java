package com.sam.smartplacesclientapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sam.smartplacesclientapp.Keys;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.ibeacon.IBeaconScanCallback;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceConfigurationObject;

import org.altbeacon.beacon.Beacon;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;

public class SmartPlaceActivity extends ActionBarActivity implements IBeaconScanCallback {


    private TextView messageTextView;

    private SmartPlacesClientApplication application;

    private String name;
    private String url;
    private String beaconId;
    private String smartPlaceId;
    private String smartPlaceConfigurationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_place);
        this.application = (SmartPlacesClientApplication) getApplication();
        initUI();
    }

    private void initUI() {
        this.messageTextView = (TextView) findViewById(R.id.smart_place_message_textview);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Keys.MESSAGE);
        this.name = intent.getStringExtra(Keys.NAME);
        this.url = intent.getStringExtra(Keys.URL);
        this.beaconId = intent.getStringExtra(Keys.BEACON);
        this.smartPlaceId = intent.getStringExtra(Keys.SMART_PLACE);
        this.messageTextView.setText(message);
        setTitle(this.name);
        this.application.getDataStore().getSmartPlaceConfiguration(this.smartPlaceId, this.beaconId,
                new SmartPlaceConfigurationCallback() {
                    @Override
                    public void done(SmartPlaceConfigurationObject object) {
                        scanForNearbyObjects(object.getId());
                    }
                });
    }

    private void scanForNearbyObjects(String smartPlaceConfigurationId) {
        this.smartPlaceConfigurationId = smartPlaceConfigurationId;
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
    public void beaconsFound(Collection<Beacon> beacons) {
        if (!beacons.isEmpty()) {
            this.application.getBeaconsManager().stopScan();
            Beacon nearestBeacon = this.application.getBeaconsManager().getNearestBeacon(beacons);
            String uuid = nearestBeacon.getId1().toHexString();
            int major = nearestBeacon.getId2().toInt();
            int minor = nearestBeacon.getId3().toInt();
            final String smartPlaceConfiguration = this.smartPlaceConfigurationId;
            this.application.getDataStore().getBeacon(uuid, major, minor, new BeaconCallback() {
                @Override
                public void done(BeaconObject object) {
                    notifyAboutObject(object, smartPlaceConfiguration);
                }
            });
        }
    }

    private void notifyAboutObject(BeaconObject beaconObject, String configurationId) {
        String url = buildUrl(this.url, configurationId, beaconObject.getObject());
        Intent intent = new Intent(this, BeaconContentActivity.class);
        intent.putExtra(BeaconContentActivity.URL_KEY, url);
        this.application.createNotification(this, this.name, beaconObject.getMessage(), intent, BeaconContentActivity.class);
    }

    private String buildUrl(String baseUrl, String configurationId, JSONObject jsonObject) {
        String finalUrl = baseUrl + "?smartPlaceConfiguration=" + configurationId + "&";
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                finalUrl += key + "=" + jsonObject.get(key).toString() + "&";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return finalUrl;
    }
}
