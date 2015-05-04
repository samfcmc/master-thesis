package com.sam.smartplacesclientapp.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesApplication;
import com.sam.smartplacesclientapp.bluetooth.BeaconsManager;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconScanCallback;
import com.sam.smartplacesclientapp.bluetooth.ibeacon.IBeaconsManager;
import com.sam.smartplacesclientapp.datastore.callback.BeaconCallback;
import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginStrategy;
import com.sam.smartplacesclientapp.datastore.login.LogoutCallback;
import com.sam.smartplacesclientapp.datastore.login.parse.ParseFacebookLoginStrategy;
import com.sam.smartplacesclientapp.datastore.object.BeaconObject;
import com.sam.smartplacesclientapp.datastore.object.DummyObject;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_LOGIN = SmartPlacesApplication.REQUEST_LOGIN;
    private SmartPlacesApplication application;

    //UI
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.application = (SmartPlacesApplication) getApplication();
        if(this.application.getDataStore().isUserLoggedIn()) {
            goToAskToTurnOnBluetoothActivity();
        }
        initUI();
    }

    private void initUI() {
        this.loginButton = (LoginButton) findViewById(R.id.main_login_button);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });
    }

    private void login(LoginStrategy loginStrategy) {
        this.application.getDataStore().login(loginStrategy, new LoginCallback<ParseUser, ParseException>() {
            @Override
            public void done(ParseUser user, ParseException exception) {
                logToDisplay("User logged in " + user.getUsername());
                goToAskToTurnOnBluetoothActivity();
            }
        });
    }

    private void loginWithFacebook() {
        login(new ParseFacebookLoginStrategy(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        this.application.getDataStore().logout(new LogoutCallback<ParseException>() {
            @Override
            public void done(ParseException exception) {
                if(exception == null) {
                    logToDisplay("Log out");
                    finish();
                }
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
            if(requestCode == REQUEST_LOGIN) {
                this.application.getDataStore().afterLoginOnActivityResult(requestCode,
                        resultCode, data);
            }
        }
    }

    private void goToAskToTurnOnBluetoothActivity() {
        Intent intent = new Intent(this, TurnOnBluetoothActivity.class);
        startActivity(intent);
    }
}
