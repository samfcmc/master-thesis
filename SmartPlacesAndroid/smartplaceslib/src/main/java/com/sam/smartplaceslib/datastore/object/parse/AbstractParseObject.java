package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseObject;
import com.sam.smartplaceslib.datastore.object.DataStoreObject;

/**
 *
 */
public abstract class AbstractParseObject extends ParseObject implements DataStoreObject {

    public AbstractParseObject() {
        super();
    }

    public AbstractParseObject(ParseObject object) {
        this();
        setObjectId(object.getObjectId());
    }

    public AbstractParseObject(DataStoreObject object) {
        this();
        setObjectId(object.getId());
    }

    @Override
    public String getId() {
        return getObjectId();
    }

}
