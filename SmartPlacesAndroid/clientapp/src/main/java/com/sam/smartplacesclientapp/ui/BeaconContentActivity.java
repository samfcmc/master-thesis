package com.sam.smartplacesclientapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;

public class BeaconContentActivity extends ActionBarActivity {

    private WebView webView;
    private SmartPlacesClientApplication application;

    public static final String URL_KEY = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_content);
        this.application = (SmartPlacesClientApplication) getApplication();
        initUI();
        loadURL();
    }

    private void initUI() {
        this.webView = (WebView) findViewById(R.id.beacon_content_webView);
        this.webView.getSettings().setJavaScriptEnabled(true);
    }

    private void loadURL() {
        String url = getIntent().getStringExtra(URL_KEY);
        String trimUrl = url.trim();
        String finalUrl = trimUrl;

        if (!trimUrl.startsWith("http")) {
            finalUrl = "http://" + trimUrl;
        }

        this.webView.setWebChromeClient(new WebChromeClient());
        this.webView.loadUrl(finalUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beacon_content, menu);
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
            turnScanOff();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void turnScanOff() {
        BeaconsManager beaconsManager = this.application.getBeaconsManager();
        beaconsManager.stopScan();
        beaconsManager.unbind();
    }

    private void logout() {
        this.application.getDataStore().logout(new LogoutCallback() {
            @Override
            public void done(DataStoreException exception) {
                if (exception == null) {
                    finish();
                }
            }
        });
    }
}
