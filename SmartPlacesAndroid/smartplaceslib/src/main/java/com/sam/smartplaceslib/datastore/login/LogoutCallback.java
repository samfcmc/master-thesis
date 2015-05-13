package com.sam.smartplaceslib.datastore.login;

import com.sam.smartplaceslib.datastore.DataStoreException;

/**
 *
 */
public interface LogoutCallback {
    public void done(DataStoreException exception);
}
