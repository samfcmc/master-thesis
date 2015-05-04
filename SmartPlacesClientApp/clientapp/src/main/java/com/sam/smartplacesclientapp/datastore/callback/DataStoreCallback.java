package com.sam.smartplacesclientapp.datastore.callback;

import com.sam.smartplacesclientapp.datastore.object.DataStoreObject;

/**
 *
 */
public interface DataStoreCallback<T> {

    public abstract void done(T object);
}
