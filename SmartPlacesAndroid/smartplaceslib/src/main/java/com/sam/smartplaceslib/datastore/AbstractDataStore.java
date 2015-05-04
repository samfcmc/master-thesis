package com.sam.smartplaceslib.datastore;

import android.content.Intent;

import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;

/**
 *
 */
public abstract class AbstractDataStore<UserType, ExceptionType> implements DataStore<UserType, ExceptionType> {

    private LoginStrategy loginStrategy;

    @Override
    public void login(LoginStrategy<UserType, ExceptionType> loginStrategy,
                                                LoginCallback<UserType, ExceptionType> callback) {
        this.loginStrategy = loginStrategy;
        this.loginStrategy.login(callback);
    }

    @Override
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data) {
        this.loginStrategy.onActivityResult(requestCode, resultCode, data);
    }
}