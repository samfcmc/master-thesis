package com.sam.smartplacesownerapp.datastore;

import android.content.Intent;

import com.sam.smartplacesownerapp.datastore.login.LoginCallback;
import com.sam.smartplacesownerapp.datastore.login.LoginStrategy;
import com.sam.smartplacesownerapp.datastore.login.LogoutCallback;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore<UserType, ExceptionType> {

    public void login(LoginStrategy<UserType, ExceptionType> loginStrategy,
                      LoginCallback<UserType, ExceptionType> callback);
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);
    public UserType getCurrentUser();
    public boolean isUserLoggedIn();
    public void logout(LogoutCallback<ExceptionType> callback);

}
