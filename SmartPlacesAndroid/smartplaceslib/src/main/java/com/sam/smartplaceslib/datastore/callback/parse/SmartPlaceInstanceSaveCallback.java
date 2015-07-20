package com.sam.smartplaceslib.datastore.callback.parse;

import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;

/**
 *
 */
public class SmartPlaceInstanceSaveCallback extends
        ParseDataStoreSaveCallback<SmartPlaceInstanceParseObject> {

    public SmartPlaceInstanceSaveCallback(DataStoreCallback<? super SmartPlaceInstanceParseObject> callback, SmartPlaceInstanceParseObject savedObject) {
        super(callback, savedObject);
    }
}
