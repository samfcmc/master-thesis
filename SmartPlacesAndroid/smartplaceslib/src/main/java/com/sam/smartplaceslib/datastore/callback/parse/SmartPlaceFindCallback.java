package com.sam.smartplaceslib.datastore.callback.parse;

import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SmartPlaceFindCallback extends ParseDataStoreFindCallback<SmartPlaceParseObject, SmartPlaceObject> {
    public SmartPlaceFindCallback(SmartPlacesCallback callback) {
        super(callback);
    }

    @Override
    protected List<SmartPlaceObject> convertListFromResponse(List<SmartPlaceParseObject> list) {
        return new ArrayList<SmartPlaceObject>(list);
    }
}
