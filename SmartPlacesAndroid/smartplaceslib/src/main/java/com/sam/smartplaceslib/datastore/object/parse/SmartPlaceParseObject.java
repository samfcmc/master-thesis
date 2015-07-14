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
    private static final String DESCRIPTION = "description";

    public SmartPlaceParseObject() {
    }

    public SmartPlaceParseObject(String name, String url, String message) {
        setName(name);
        setUrl(url);
        setDescription(message);
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
    public String getDescription() {
        return getString(DESCRIPTION);
    }

    @Override
    public void setDescription(String description) {
        put(DESCRIPTION, description);
    }

    public void updateFromParseObject(ParseObject parseObject) {
        setName(parseObject.getString(NAME));
        setUrl(parseObject.getString(URL));
        setDescription(parseObject.getString(DESCRIPTION));
    }
}
