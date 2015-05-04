package com.sam.smartplacesclientapp.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sam.smartplacesclientapp.datastore.callback.DataStoreCallback;
import com.sam.smartplacesclientapp.datastore.object.DummyObject;

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

    public static ParseQuery<DummyParseObject> getQuery() {
        return ParseQuery.getQuery(DummyParseObject.class);
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
