package com.sam.smartplacesownersapp.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.ParseDataStore;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.SmartPlacesOwnerApplication;


public class MainActivity extends ActionBarActivity implements BeaconObjectFragment.OnBeaconObjectFragmentInteractionListener{

    SmartPlacesOwnerApplication application;

    private static final int LOGIN_REQUEST = ParseDataStore.REQUEST_LOGIN;
    private static final int TURN_BT_ON_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.application = (SmartPlacesOwnerApplication) getApplication();
        selectFragment();
    }

    private void selectFragment() {
        if(this.application.getDataStore().isUserLoggedIn()) {
            //TODO: Check if user has bluetooth turned on
            logToDisplay("User already logged in");
            if(this.application.getBeaconsManager().isBluetoothTurnedOn(this)) {
                replaceFragment(new BeaconObjectFragment());
            }
            else {
                this.application.getBeaconsManager().askToTurnBluetoothOn(this, TURN_BT_ON_REQUEST);
            }
        }
        else {
            replaceFragment(new MainFragment());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if(id == R.id.action_logout) {
            this.application.getDataStore().logout(new LogoutCallback<ParseException>() {
                @Override
                public void done(ParseException exception) {
                    logToDisplay("User logged out");
                    selectFragment();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }

    private void logToDisplay(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == LOGIN_REQUEST) {
                this.application.getDataStore().afterLoginOnActivityResult(requestCode, resultCode, data);
            }
            else if(requestCode == TURN_BT_ON_REQUEST) {
                logToDisplay("Bluetooth turned on");
                selectFragment();
            }
        }
    }

    public void afterLogin() {
        selectFragment();
    }

    @Override
    public void onBeaconSelected(String id) {
        logToDisplay("Selected beacon " + id);
    }
}
