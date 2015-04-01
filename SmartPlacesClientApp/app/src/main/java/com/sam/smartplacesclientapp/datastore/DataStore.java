package com.sam.smartplacesclientapp.datastore;

import android.content.Intent;

import com.sam.smartplacesclientapp.datastore.callback.BeaconCallback;
import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginStrategy;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore {

    public void createDummy(String name, DummyCallback callback);
    public void getBeaon(String uuid, int major, int minor, BeaconCallback callback);
    public <UserType, ExceptionType> void login(LoginStrategy<UserType, ExceptionType> loginStrategy,
                                                LoginCallback<UserType, ExceptionType> callback);
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);

}
