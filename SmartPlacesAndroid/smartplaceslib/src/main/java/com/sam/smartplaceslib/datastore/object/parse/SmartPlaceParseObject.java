package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

/**
 *
 */
@ParseClassName("SmartPlace")
public class SmartPlaceParseObject extends AbstractParseObject implements SmartPlaceObject {

    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String MESSAGE = "message";

    public SmartPlaceParseObject() {
    }

    public SmartPlaceParseObject(String name, String url, String message) {
        setName(name);
        setUrl(url);
        setMessage(message);
    }

    public SmartPlaceParseObject(ParseObject parseObject) {
        super(parseObject);
        updateFromParseObject(parseObject);
    }

    @Override
    public String getName() {
        return getString(NAME);
    }

    @Override
    public void setName(String name) {
        put(NAME, name);
    }

    @Override
    public String getUrl() {
        return getString(URL);
    }

    @Override
    public void setUrl(String url) {
        put(URL, url);
    }

    @Override
    public String getMessage() {
        return getString(MESSAGE);
    }

    @Override
    public void setMessage(String message) {
        put(MESSAGE, message);
    }

    public void updateFromParseObject(ParseObject parseObject) {
        setName(parseObject.getString(NAME));
        setUrl(parseObject.getString(URL));
        setMessage(parseObject.getString(MESSAGE));
    }
}
