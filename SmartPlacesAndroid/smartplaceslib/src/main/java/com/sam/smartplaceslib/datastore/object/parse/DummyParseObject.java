package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sam.smartplaceslib.datastore.object.DummyObject;

/**
 * Dummy object just to test DataStore
 * Other DataStore objects should follow this model
 */
@ParseClassName("Dummies")
public class DummyParseObject extends AbstractParseObject implements DummyObject {

    static {
        ParseObject.registerSubclass(DummyParseObject.class);
    }

    private static final String NAME = "name";

    public DummyParseObject() {
        super();
    }

    public DummyParseObject(String name) {
        this();
        setName(name);
    }

    @Override
    public String getName() {
        return getString(NAME);
    }

    @Override
    public void setName(String name) {
        put(NAME, name);
    }

}
