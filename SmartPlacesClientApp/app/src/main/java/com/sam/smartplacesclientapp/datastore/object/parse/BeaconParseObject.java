package com.sam.smartplacesclientapp.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sam.smartplacesclientapp.datastore.object.BeaconObject;

import org.altbeacon.beacon.Beacon;
import org.json.JSONObject;

/**
 *
 */
@ParseClassName("Beacon")
public class BeaconParseObject extends AbstractParseObject implements BeaconObject {

    private static final String UUID = "uuid";
    private static final String MAJOR = "major";
    private static final String MINOR = "minor";
    private static final String OBJECT = "object";

    static {
        ParseObject.registerSubclass(BeaconParseObject.class);
    }

    public BeaconParseObject(String uuid, int major, int minor) {
        this();
        setUUID(uuid);
        setMajor(major);
        setMinor(minor);
    }

    public BeaconParseObject() {
        super();
    }

    @Override
    public String getUUID() {
        return getString(UUID);
    }

    @Override
    public void setUUID(String uuid) {
        put(UUID, uuid);
    }

    @Override
    public int getMajor() {
        return getInt(MAJOR);
    }

    @Override
    public void setMajor(int major) {
        put(MAJOR, major);
    }

    @Override
    public int getMinor() {
        return getInt(MINOR);
    }

    @Override
    public void setMinor(int minor) {
        put(MINOR, minor);
    }

    @Override
    public JSONObject getObject() {
        return getJSONObject(OBJECT);
    }

    @Override
    public void setObject(JSONObject object) {
        put(OBJECT, object);
    }

    public static ParseQuery<BeaconParseObject> getQuery() {
        return ParseQuery.getQuery(BeaconParseObject.class);
    }
}
