package com.sam.smartplacesclientapp.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sam.smartplacesclientapp.R;

public class BeaconContentActivity extends ActionBarActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_content);
        initUI();
        loadURL();
    }

    private void initUI() {
        this.webView = (WebView) findViewById(R.id.beacon_content_webView);
        this.webView.getSettings().setJavaScriptEnabled(true);
    }

    private void loadURL() {
        String url = getIntent().getStringExtra("url");
        String trimUrl = url.trim();
        String finalUrl = trimUrl;

        if(!trimUrl.startsWith("http")) {
            finalUrl = "http://" + trimUrl;
        }

        this.webView.setWebViewClient(new WebViewClient());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
