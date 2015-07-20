package com.sam.ownersapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.ClientParseDataStore;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnFragmentInteractionListener {
    SmartPlacesOwnersApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        this.application = (SmartPlacesOwnersApplication) getApplication();
        if (this.application.getDataStore().isUserLoggedIn()) {
            goToUserActivity();
        } else {
            replaceFragment(MainActivityFragment.newInstance());
        }
    }

    private void goToUserActivity() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        finish();
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

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(final Fragment fragment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, fragment);
                transaction.addToBackStack(fragment.getClass().getSimpleName());
                transaction.commit();
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
        if (resultCode == RESULT_OK && requestCode == ClientParseDataStore.REQUEST_LOGIN) {
            this.application.getDataStore().afterLoginOnActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void afterLogin() {
        goToUserActivity();
    }
}
