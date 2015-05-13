package com.sam.smartplaceslib.datastore.login;

import android.content.Intent;

/**
 *
 */
public interface LoginStrategy {
    public void login(LoginCallback callback);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
