package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.DeleteDataStoreCallback;

/**
 *
 */
public class ParseDataStoreDeleteCallback implements DeleteCallback {

    private DeleteDataStoreCallback callback;

    public ParseDataStoreDeleteCallback(DeleteDataStoreCallback callback) {
        this.callback = callback;
    }

    @Override
    public void done(ParseException e) {
        this.callback.deleted();
    }
}
