package com.sam.smartplaceslib.datastore;

import android.content.Intent;

import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.UserObject;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore {

    void login(LoginStrategy loginStrategy,
               LoginCallback callback);

    void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);

    UserObject getCurrentUser();

    boolean isUserLoggedIn();

    void logout(final LogoutCallback callback);

}
