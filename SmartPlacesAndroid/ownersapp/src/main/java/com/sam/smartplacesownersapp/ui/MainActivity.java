package com.sam.smartplacesownersapp.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.ParseDataStore;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceConfigurationObject;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.SmartPlacesOwnerApplication;


public class MainActivity extends ActionBarActivity implements
        BeaconListFragment.OnBeaconListFragmentInteractionListener,
        BeaconConfigFragment.OnBeaconConfigFragmentInteractionListener,
        SmartPlaceConfigurationFragment.OnFragmentInteractionListener{

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
        final String smartPlaceId = "uWE3R6EDOv";
        if(this.application.getDataStore().isUserLoggedIn()) {
            if(this.application.getBeaconsManager().isBluetoothTurnedOn(this)) {
                showLoading();
                // Defined for the restaurant...
                this.application.getDataStore().getSmartPlaceConfiguration(smartPlaceId, new SmartPlaceConfigurationCallback() {
                    @Override
                    public void done(SmartPlaceConfigurationObject object) {
                        stopLoading();
                        if(object == null) {
                            replaceFragment(SmartPlaceConfigurationFragment.newInstance(smartPlaceId));
                        }
                        else {
                            replaceFragment(BeaconListFragment.newInstance());
                        }
                    }
                });
                //replaceFragment(new BeaconListFragment());
            }
            else {
                this.application.getBeaconsManager().askToTurnBluetoothOn(this, TURN_BT_ON_REQUEST);
            }
        }
        else {
            replaceFragment(new MainFragment());
        }
    }

    private void showLoading() {
        this.application.showProgressDialog(this);
    }

    private void stopLoading() {
        this.application.dismissProgressDialog(this);
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
            this.application.getDataStore().logout(new LogoutCallback() {
                @Override
                public void done(DataStoreException exception) {
                    logToDisplay("User logged out");
                    selectFragment();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(final Fragment fragment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
            }
        });
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
    public void onBeaconSelected(String uuid, int major, int minor) {
        logToDisplay("Selected beacon " + uuid);
        replaceFragment(BeaconConfigFragment.newInstance(uuid, major, minor));
    }

    @Override
    public void onSaveBeacon(BeaconObject object) {
        logToDisplay("Saved changes");
        selectFragment();
    }

    @Override
    public void onSmartPlaceConfigurationSaved() {
        replaceFragment(BeaconListFragment.newInstance());
    }
}
