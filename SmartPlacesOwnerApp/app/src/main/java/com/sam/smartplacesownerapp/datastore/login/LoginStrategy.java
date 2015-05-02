package com.sam.smartplacesownerapp.datastore.login;

import android.content.Intent;

/**
 *
 */
public interface LoginStrategy<UserType, ExceptionType> {
    public void login(LoginCallback<UserType, ExceptionType> callback);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
