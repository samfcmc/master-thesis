package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.DeleteDataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.observer.DataStoreCallbackDoneObserver;

/**
 * ParseDataStoreDeleteObservableCallback: Callback used when we want to use delete callback
 * and measure the latency
 */
public class ParseDataStoreDeleteObservableCallback extends ParseDataStoreDeleteCallback {

    private DataStoreCallbackDoneObserver observer;

    public ParseDataStoreDeleteObservableCallback(DeleteDataStoreCallback callback,
                                                  DataStoreCallbackDoneObserver observer) {
        super(callback);
        this.observer = observer;
    }

    @Override
    public void done(ParseException e) {
        observer.done();
        super.done(e);
    }
}
