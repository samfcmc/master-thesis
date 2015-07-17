package com.sam.ownersapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

import java.util.List;

public class UserActivity extends AppCompatActivity
        implements SmartPlaceListFragment.OnFragmentInteractionListener,
        SmartPlaceInstanceListFragment.OnFragmentInteractionListener,
        UpdateSmartPlaceInstanceFragment.OnFragmentInteractionListener,
        LogoutCallback {

    private SmartPlacesOwnersApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setUpToolbar();
        setUpTabs();

        this.application = (SmartPlacesOwnersApplication) getApplication();
    }

    private void setUpTabs() {
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.user_tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.user_tabcontent);
        String tabTitle = getString(R.string.action_smart_places);
        tabHost.addTab(tabHost.newTabSpec(tabTitle).setIndicator(tabTitle),
                SmartPlaceListFragment.class, new Bundle());
        tabTitle = getString(R.string.action_instances);
        tabHost.addTab(tabHost.newTabSpec(tabTitle).setIndicator(tabTitle),
                SmartPlaceInstanceListFragment.class, new Bundle());
    }

    private void detachFragments(FragmentTransaction transaction) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                transaction.detach(fragment);
            }
        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        toolbar.inflateMenu(R.menu.menu_user);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                    //TODO:
                    return true;
                } else if (id == R.id.action_logout) {
                    logout();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        this.application.getDataStore().logout(this);
    }

    private void goToSmartPlaceInstancesList() {
        replaceFragment(SmartPlaceInstanceListFragment.newInstance());
    }

    private void replaceFragment(final Fragment fragment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                detachFragments(transaction);
                transaction.replace(R.id.user_tabcontent, fragment);
                transaction.commit();
            }
        });
    }

    private void logToDisplay(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToSmartPlaceList() {
        replaceFragment(SmartPlaceListFragment.newInstance());
    }

    @Override
    public void onSmartPlaceSelected(SmartPlaceObject smartPlaceObject) {
        Intent intent = ShowSmartPlaceActivity.getIntent(smartPlaceObject, this);
        startActivity(intent);
    }

    @Override
    public void onSmartPlaceInstanceSelected(SmartPlaceInstanceObject smartPlaceInstanceObject) {
        replaceFragment(UpdateSmartPlaceInstanceFragment.newInstance(smartPlaceInstanceObject));
    }

    @Override
    public void smartPlaceInstanceSaved(SmartPlaceInstanceObject object) {
        replaceFragment(SmartPlaceInstanceListFragment.newInstance());
    }

    // Logout
    @Override
    public void done(DataStoreException exception) {
        // Back to the main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
