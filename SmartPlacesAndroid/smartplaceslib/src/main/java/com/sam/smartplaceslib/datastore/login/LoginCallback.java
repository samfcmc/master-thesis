package com.sam.smartplaceslib.datastore.login;

import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.object.UserObject;

/**
 *
 */
public interface LoginCallback {
    public void done(UserObject user, DataStoreException exception);
}
