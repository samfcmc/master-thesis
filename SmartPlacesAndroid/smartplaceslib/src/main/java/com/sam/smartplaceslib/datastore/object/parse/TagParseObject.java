package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.sam.smartplaceslib.datastore.object.TagObject;

import org.json.JSONObject;

/**
 *
 */
@ParseClassName("Tag")
public class TagParseObject extends AbstractParseObject implements TagObject {
    public static final String DATA = "data";
    public static final String BEACON = "beacon";
    public static final String SMARTPLACE_INSTANCE = "smartPlaceInstance";

    public TagParseObject() {
    }

    public TagParseObject(JSONObject data) {
        setData(data);
    }

    @Override
    public JSONObject getData() {
        return getJSONObject(DATA);
    }

    @Override
    public void setData(JSONObject data) {
        put(DATA, data);
    }

    public void setBeacon(BeaconParseObject beacon) {
        put(BEACON, beacon);
    }

    public void setSmartPlaceInstance(SmartPlaceInstanceParseObject smartPlaceInstance) {
        put(SMARTPLACE_INSTANCE, smartPlaceInstance);
    }

}
