package com.sam.smartplaceslib.datastore.callback.parse;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.sam.smartplaceslib.datastore.callback.ListDataStoreCallback;
import com.sam.smartplaceslib.datastore.object.DataStoreObject;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;

import java.util.List;

/**
 *
 */
public abstract class ParseDataStoreFindCallback<T extends AbstractParseObject, T1 extends DataStoreObject> implements FindCallback<T> {
    ListDataStoreCallback<T1> callback;

    public ParseDataStoreFindCallback(ListDataStoreCallback<T1> callback) {
        this.callback = callback;
    }

    @Override
    public void done(List<T> list, ParseException e) {
        List<T1> resultList = convertListFromResponse(list);
        this.callback.done(resultList);
    }

    protected abstract List<T1> convertListFromResponse(List<T> list);

}
