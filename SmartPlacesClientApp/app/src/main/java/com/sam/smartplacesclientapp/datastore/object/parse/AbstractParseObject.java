package com.sam.smartplacesclientapp.datastore.object.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sam.smartplacesclientapp.datastore.callback.DataStoreCallback;
import com.sam.smartplacesclientapp.datastore.object.DataStoreObject;

/**
 *
 */
public abstract class AbstractParseObject extends ParseObject implements DataStoreObject{

    @Override
    public String getId() {
        return getObjectId();
    }
}
