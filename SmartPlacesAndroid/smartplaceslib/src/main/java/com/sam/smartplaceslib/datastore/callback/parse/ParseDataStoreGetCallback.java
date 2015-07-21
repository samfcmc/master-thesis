package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;

/**
 *
 */
public class ParseDataStoreGetCallback<T extends AbstractParseObject> implements GetCallback<T> {

    private DataStoreCallback<? super T> callback;

    public ParseDataStoreGetCallback(DataStoreCallback<? super T> callback) {
        super();
        this.callback = callback;
    }

    @Override
    public void done(T object, ParseException e) {
        this.callback.done(object);
    }

}
