package com.sam.smartplaceslib.datastore;

import android.content.Intent;

import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;

/**
 *
 */
public abstract class AbstractDataStore implements DataStore {

    private LoginStrategy loginStrategy;

    @Override
    public void login(LoginStrategy loginStrategy, LoginCallback callback) {
        this.loginStrategy = loginStrategy;
        this.loginStrategy.login(callback);
    }

    @Override
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data) {
        this.loginStrategy.onActivityResult(requestCode, resultCode, data);
    }

}
