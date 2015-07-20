package com.sam.smartplaceslib.datastore.callback.parse;

import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;

/**
 *
 */
public class BeaconGetCallback extends ParseDataStoreGetCallback<BeaconParseObject> {
    public BeaconGetCallback(DataStoreCallback<BeaconObject> callback) {
        super(callback);
    }
}
