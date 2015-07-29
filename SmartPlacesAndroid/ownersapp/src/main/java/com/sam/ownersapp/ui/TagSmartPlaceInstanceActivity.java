package com.sam.ownersapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.ui.SmartPlacesWebView;
import com.sam.smartplaceslib.web.OnPageLoadedCallback;

import org.json.JSONException;

import java.util.Collection;

public class TagSmartPlaceInstanceActivity extends AppCompatActivity implements BeaconScanCallback {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String URL = "url";

    private static final int REQUEST_TURN_ON_BT = 4;

    private SmartPlacesOwnersApplication application;

    private SmartPlacesWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_smart_place_instance);
        this.application = (SmartPlacesOwnersApplication) getApplication();
        Intent intent = getIntent();

        TextView titleTextView = (TextView) findViewById(R.id.tag_smart_place_instance_title_textview);
        this.webview = (SmartPlacesWebView) findViewById(R.id.tag_smart_place_instance_webview);

        ImageView tagImageView = (ImageView) findViewById(R.id.tag_smart_place_instance_tag_imageview);

        titleTextView.setText(intent.getStringExtra(TITLE));

        this.webview.setOnPageLoadedCallback(new OnPageLoadedCallback() {
            @Override
            public void done() {
                initWebView();
            }
        });
        this.webview.loadUrl(intent.getStringExtra(URL));

        tagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBeacons();
            }
        });

        if (!this.application.getBeaconsManager().isBluetoothTurnedOn(this)) {
            this.application.getBeaconsManager().askToTurnBluetoothOn(this, REQUEST_TURN_ON_BT);
        }
    }

    private void initWebView() {
        String id = getIntent().getStringExtra(ID);
        this.webview.init(id);
    }

    private void scanBeacons() {
        if (this.application.getBeaconsManager().isBluetoothTurnedOn(this)) {
            this.application.getBeaconsManager().startScan(this, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_smart_place_instance, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    public static Intent getIntent(Context context, String id, String title, String url) {
        Intent intent = new Intent(context, TagSmartPlaceInstanceActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    public void beaconsFound(final Collection<BeaconInfo> beacons) {
        if (!beacons.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    beaconsScanned(beacons);
                }
            });
        }
    }

    private void beaconsScanned(Collection<BeaconInfo> beaconInfoList) {
        this.application.logToDisplay(this, "Beacons detected");
        try {
            this.webview.beaconsScanned(beaconInfoList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.application.getBeaconsManager().stopScan();
    }
}
