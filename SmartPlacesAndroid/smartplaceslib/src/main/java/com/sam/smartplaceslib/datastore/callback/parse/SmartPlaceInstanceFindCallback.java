package com.sam.smartplaceslib.datastore.callback.parse;

import com.sam.smartplaceslib.datastore.callback.ListDataStoreCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SmartPlaceInstanceFindCallback extends
        ParseDataStoreFindCallback<SmartPlaceInstanceParseObject, SmartPlaceInstanceObject> {

    public SmartPlaceInstanceFindCallback(ListDataStoreCallback<SmartPlaceInstanceObject> callback) {
        super(callback);
    }

    @Override
    protected List<SmartPlaceInstanceObject> convertListFromResponse(List<SmartPlaceInstanceParseObject> list) {
        return new ArrayList<SmartPlaceInstanceObject>(list);
    }
}
