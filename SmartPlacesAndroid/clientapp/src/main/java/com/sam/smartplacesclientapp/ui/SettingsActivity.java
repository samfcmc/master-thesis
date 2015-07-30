package com.sam.smartplacesclientapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesClientApplication;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.utils.Settings;

public class SettingsActivity extends AppCompatActivity {

    private EditText scanPeriodBackgroundEditText;
    private EditText scanPeriodForegroundEditText;

    private SmartPlacesClientApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.scanPeriodBackgroundEditText = (EditText)
                findViewById(R.id.settings_scan_period_background_editText);
        this.scanPeriodForegroundEditText = (EditText)
                findViewById(R.id.settings_scan_period_foreground_editText);

        this.application = (SmartPlacesClientApplication) getApplication();
        initUI();
    }

    private void initUI() {
        Settings settings = this.application.getBeaconsManager().getSettings();
        this.scanPeriodBackgroundEditText.setText(settings.getScanPeriodInBackgroundMode() + "");
        this.scanPeriodForegroundEditText.setText(settings.getScanPeriodInForegroundMode() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_restore_default) {
            restoreDefaultValues();
            return true;
        } else if (id == R.id.action_save) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void restoreDefaultValues() {
        BeaconsManager beaconsManager = this.application.getBeaconsManager();
        beaconsManager.updateScanPeriodInBackgroundMode(beaconsManager.getSettings().DEFAULT_SCAN_PERIOD_BACKGROUND);
        beaconsManager.updateScanPeriodInForegroundMode(beaconsManager.getSettings().DEFAULT_SCAN_PERIOD_FOREGROUND);
        initUI();
    }

    private void save() {
        String scanPeriodBackgroundStr = this.scanPeriodBackgroundEditText.getText().toString();
        String scanPeriodForegroundStr = this.scanPeriodForegroundEditText.getText().toString();
        long scanPeriodBackground = Long.parseLong(scanPeriodBackgroundStr);
        long scanPeriodForeground = Long.parseLong(scanPeriodForegroundStr);
        BeaconsManager beaconsManager = this.application.getBeaconsManager();
        beaconsManager.updateScanPeriodInBackgroundMode(scanPeriodBackground);
        beaconsManager.updateScanPeriodInForegroundMode(scanPeriodForeground);
        String savedMessage = getString(R.string.settings_saved);
        logToDisplay(savedMessage);
    }

    private void logToDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
