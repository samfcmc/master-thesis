package com.sam.smartplacesclientapp.datastore;

import android.content.Intent;

import com.sam.smartplacesclientapp.datastore.callback.BeaconCallback;
import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginStrategy;
import com.sam.smartplacesclientapp.datastore.login.LogoutCallback;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore<UserType, ExceptionType> {

    public void createDummy(String name, DummyCallback callback);
    public void getBeaon(String uuid, int major, int minor, BeaconCallback callback);
    public void login(LoginStrategy<UserType, ExceptionType> loginStrategy,
                                                LoginCallback<UserType, ExceptionType> callback);
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);
    public UserType getCurrentUser();
    public boolean isUserLoggedIn();
    public void logout(LogoutCallback<ExceptionType> callback);

}
