package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
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

    @Override
    public BeaconObject getBeacon() {
        ParseObject parseObject = getParseObject(BEACON);
        BeaconParseObject beaconParseObject =
                ParseObject.createWithoutData(BeaconParseObject.class, parseObject.getObjectId());
        beaconParseObject.fromParseObject(parseObject);
        return beaconParseObject;
    }

    public void setBeacon(BeaconParseObject beacon) {
        put(BEACON, beacon);
    }

    public void setSmartPlaceInstance(SmartPlaceInstanceParseObject smartPlaceInstance) {
        put(SMARTPLACE_INSTANCE, smartPlaceInstance);
    }

}
