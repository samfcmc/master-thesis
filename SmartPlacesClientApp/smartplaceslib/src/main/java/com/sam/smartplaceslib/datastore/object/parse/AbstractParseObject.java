package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseObject;
import com.sam.smartplaceslib.datastore.object.DataStoreObject;

/**
 *
 */
public abstract class AbstractParseObject extends ParseObject implements DataStoreObject {

    @Override
    public String getId() {
        return getObjectId();
    }
}
