package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.observer.DataStoreCallbackDoneObserver;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;

/**
 *
 */
public class ParseDataStoreSaveObservableCallback<T extends AbstractParseObject>
        extends ParseDataStoreSaveCallback<T> {

    private DataStoreCallbackDoneObserver observer;

    public ParseDataStoreSaveObservableCallback(DataStoreCallback<? super T> callback,
                                                T savedObject,
                                                DataStoreCallbackDoneObserver observer) {
        super(callback, savedObject);
        this.observer = observer;
    }

    @Override
    public void done(ParseException e) {
        observer.done();
        super.done(e);
    }
}
