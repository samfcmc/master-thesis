package com.sam.smartplacesclientapp.datastore.callback;

/**
 *
 */
public interface DataStoreCallback<T> {

    public abstract void done(T object);
}
