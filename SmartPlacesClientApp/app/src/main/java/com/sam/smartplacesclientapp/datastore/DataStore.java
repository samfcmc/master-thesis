package com.sam.smartplacesclientapp.datastore;

import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore {

    void createDummy(String name, DummyCallback callback);

}
