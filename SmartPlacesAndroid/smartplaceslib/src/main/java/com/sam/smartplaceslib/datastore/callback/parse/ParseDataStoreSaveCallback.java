package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;

/**
 *
 */
public class ParseDataStoreSaveCallback<T extends AbstractParseObject> implements SaveCallback {

    DataStoreCallback<? super T> callback;
    T savedObject;

    public ParseDataStoreSaveCallback(DataStoreCallback<? super T> callback, T savedObject) {
        this.callback = callback;
        this.savedObject = savedObject;
    }

    @Override
    public void done(ParseException e) {
        this.callback.done(this.savedObject);
    }
}
