package com.sam.smartplacesclientapp.datastore.object;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Dummy object just to test DataStore
 * Other DataStore objects should follow this model
 */
@ParseClassName("Dummies")
public class DummyObject extends ParseObject implements DataStoreObject {

    static {
        ParseObject.registerSubclass(DummyObject.class);
    }

    private static final String NAME = "name";

    public DummyObject() {
        super();
    }

    public DummyObject(String name) {
        this();
        setName(name);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public static ParseQuery<DummyObject> getQuery() {
        return ParseQuery.getQuery(DummyObject.class);
    }
}
