package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.ListDataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.observer.DataStoreCallbackDoneObserver;
import com.sam.smartplaceslib.datastore.object.DataStoreObject;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;

import java.util.List;

/**
 *
 */
public abstract class ParseDataStoreFindObservableCallback<T extends AbstractParseObject,
        T1 extends DataStoreObject> extends ParseDataStoreFindCallback<T, T1> {

    private DataStoreCallbackDoneObserver observer;

    public ParseDataStoreFindObservableCallback(ListDataStoreCallback<T1> callback,
                                                DataStoreCallbackDoneObserver observer) {
        super(callback);
        this.observer = observer;
    }

    public ParseDataStoreFindObservableCallback(ListDataStoreCallback<T1> callback) {
        super(callback);
        this.observer = new NullDataStoreCallbackDoneObserver();
    }

    public void setObserver(DataStoreCallbackDoneObserver observer) {
        this.observer = observer;
    }

    @Override
    public void done(List<T> list, ParseException e) {
        observer.done();
        super.done(list, e);
    }

    public class NullDataStoreCallbackDoneObserver implements DataStoreCallbackDoneObserver {

        @Override
        public void done() {
            // Nothing to do...
        }
    }
}
