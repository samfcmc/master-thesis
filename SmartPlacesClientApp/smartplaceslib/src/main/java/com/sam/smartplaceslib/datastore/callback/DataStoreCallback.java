package com.sam.smartplaceslib.datastore.callback;

/**
 *
 */
public interface DataStoreCallback<T> {

    public abstract void done(T object);
}
