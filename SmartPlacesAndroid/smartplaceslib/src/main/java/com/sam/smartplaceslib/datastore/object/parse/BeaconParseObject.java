package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.sam.smartplaceslib.datastore.object.BeaconObject;

/**
 *
 */
@ParseClassName("Beacon")
public class BeaconParseObject extends AbstractParseObject implements BeaconObject {

    public static final String UUID = "uuid";
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String NAME = "name";
    public static final String ICON = "icon";

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
    public String getName() {
        return getString(NAME);
    }

    @Override
    public String getIcon() {
        return getString(ICON);
    }


    public void fromParseObject(ParseObject parseObject) {
        String uuid = parseObject.getString(UUID);
        int major = parseObject.getInt(MAJOR);
        int minor = parseObject.getInt(MINOR);
        String name = parseObject.getString(NAME);
        String icon = parseObject.getString(ICON);
    }
}
