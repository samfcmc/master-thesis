package com.sam.ownersapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.ownersapp.R;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

public class UserActivity extends AppCompatActivity implements SmartPlaceListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        replaceFragment(new UserActivityFragment());
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
        } else if (id == R.id.action_smart_places) {
            goToSmartPlaceList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(final Fragment fragment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.user_fragment_container, fragment);
                transaction.addToBackStack(fragment.getClass().getSimpleName());
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
        //TODO: Go to a screen to show info about this smart place
    }
}
