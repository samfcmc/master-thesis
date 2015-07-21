package com.sam.ownersapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sam.ownersapp.R;
import com.sam.smartplaceslib.ui.SmartPlacesWebView;

public class TagSmartPlaceInstanceActivity extends AppCompatActivity {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_smart_place_instance);
        Intent intent = getIntent();

        TextView titleTextView = (TextView) findViewById(R.id.tag_smart_place_instance_title_textview);
        SmartPlacesWebView webView = (SmartPlacesWebView) findViewById(R.id.tag_smart_place_instance_webview);

        titleTextView.setText(intent.getStringExtra(TITLE));
        webView.loadUrl(intent.getStringExtra(URL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_smart_place_instance, menu);
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

    public static Intent getIntent(Context context, String id, String title, String url) {
        Intent intent = new Intent(context, TagSmartPlaceInstanceActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        return intent;
    }
}
