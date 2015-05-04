package com.sam.smartplacesclientapp.datastore.login;

import android.content.Intent;

import com.sam.smartplacesclientapp.datastore.callback.DataStoreCallback;

/**
 *
 */
public interface LoginStrategy<UserType, ExceptionType> {
    public void login(LoginCallback<UserType, ExceptionType> callback);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
