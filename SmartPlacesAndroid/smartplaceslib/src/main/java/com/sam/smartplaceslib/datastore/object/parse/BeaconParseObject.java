package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.sam.smartplaceslib.datastore.object.BeaconObject;

/**
 *
 */
@ParseClassName("Beacon")
public class BeaconParseObject extends AbstractParseObject implements BeaconObject {

    private static final String UUID = "uuid";
    private static final String MAJOR = "major";
    private static final String MINOR = "minor";
    private static final String OBJECT = "object";
    private static final String SMART_PLACES = "smartPlaces";
    private static final String SMART_PLACES_CONFIGURATION = "smartPlacesConfiguration";
    private static final String MESSAGE = "message";

    public BeaconParseObject(String uuid, int major, int minor, String message) {
        this();
        setUUID(uuid);
        setMajor(major);
        setMinor(minor);
        setMessage(message);
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
    public String getMessage() {
        return getString(MESSAGE);
    }

    @Override
    public void setMessage(String message) {
        put(MESSAGE, message);
    }

    public ParseRelation<SmartPlaceParseObject> getSmartPlacesRelation() {
        return getRelation(SMART_PLACES);
    }

    public ParseRelation<SmartPlaceConfigurationParseObject> getSmartPlacesConfigurationRelation() {
        return getRelation(SMART_PLACES_CONFIGURATION);
    }
}
