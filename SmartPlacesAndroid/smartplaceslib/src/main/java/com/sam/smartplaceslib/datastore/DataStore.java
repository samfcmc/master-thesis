package com.sam.smartplaceslib.datastore;

import android.content.Intent;

import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.DummyCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore<UserType, ExceptionType> {

    public void createDummy(String name, DummyCallback callback);
    public void login(LoginStrategy<UserType, ExceptionType> loginStrategy,
                      LoginCallback<UserType, ExceptionType> callback);
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);
    public UserType getCurrentUser();
    public boolean isUserLoggedIn();
    public void logout(LogoutCallback<ExceptionType> callback);
    public void getBeacon(String uuid, int major, int minor, BeaconCallback callback);
    public void getSmartPlaces(String uuid, int major, int minor, SmartPlacesCallback callback);

}
