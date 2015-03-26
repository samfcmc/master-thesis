package com.sam.smartplacesclientapp.datastore.callback;

import com.sam.smartplacesclientapp.datastore.object.DataStoreObject;

/**
 *
 */
public interface DataStoreCallback<T extends DataStoreObject> {

    public abstract void done(T object);
}
