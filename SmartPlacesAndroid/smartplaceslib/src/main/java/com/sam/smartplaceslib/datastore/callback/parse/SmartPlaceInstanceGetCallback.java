package com.sam.smartplaceslib.datastore.callback.parse;

import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;

/**
 *
 */
public class SmartPlaceInstanceGetCallback extends ParseDataStoreGetCallback<SmartPlaceInstanceParseObject> {

    public SmartPlaceInstanceGetCallback(DataStoreCallback<? super SmartPlaceInstanceParseObject> callback) {
        super(callback);
    }
}
