package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.observer.DataStoreCallbackDoneObserver;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;

/**
 *
 */
public class ParseDataStoreGetObservableCallback<T extends AbstractParseObject>
        extends ParseDataStoreGetCallback<T> {

    private DataStoreCallbackDoneObserver observer;

    public ParseDataStoreGetObservableCallback(DataStoreCallback<? super T> callback,
                                               DataStoreCallbackDoneObserver observer) {
        super(callback);
        this.observer = observer;
    }

    @Override
    public void done(T object, ParseException e) {
        observer.done();
        super.done(object, e);
    }
}
