package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.object.SmartPlaceConfigurationObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.datastore.object.UserObject;

import org.json.JSONObject;

/**
 *
 */
@ParseClassName("SmartPlacesConfiguration")
public class SmartPlaceConfigurationParseObject extends AbstractParseObject implements SmartPlaceConfigurationObject {

    private static final String OWNER = "owner";
    private static final String SMART_PLACE = "smartPlace";
    private static final String OBJECT = "object";

    public SmartPlaceConfigurationParseObject() {
        super();
    }

    public SmartPlaceConfigurationParseObject(UserObject owner, SmartPlaceObject smartPlace, JSONObject object) {
        this(owner.getId(), smartPlace.getId(), object);
    }

    public SmartPlaceConfigurationParseObject(String ownerId, String smartPlaceId, JSONObject object) {
        super();
        setOwner(ownerId);
        setSmartPlace(smartPlaceId);
        setObject(object);
    }

    public SmartPlaceConfigurationParseObject(SmartPlaceConfigurationObject object) {
        super(object);
        setOwner(object.getOwner().getId());
        setSmartPlace(object.getSmartPlace().getId());
        setObject(object.getObject());
    }

    public void updateFromParseObject(ParseObject parseObject) {
        setOwner(parseObject.getParseUser(OWNER).getObjectId());
        setSmartPlace(parseObject.getParseObject(SMART_PLACE).getObjectId());
        setObject(parseObject.getJSONObject(OBJECT));
    }

    @Override
    public UserObject getOwner() {
        return new UserParseObject(getParseUser(OWNER));
    }

    @Override
    public void setOwner(UserObject user) {
        setOwner(user.getId());
    }

    @Override
    public void setOwner(String ownerId) {
        ParseUser owner = ParseUser.createWithoutData(ParseUser.class, ownerId);
        put(OWNER, owner);
    }

    @Override
    public SmartPlaceObject getSmartPlace() {
        return new SmartPlaceParseObject(getParseObject(SMART_PLACE));
    }

    @Override
    public void setSmartPlace(SmartPlaceObject smartPlace) {
        setSmartPlace(smartPlace.getId());
    }

    @Override
    public void setSmartPlace(String smartPlaceId) {
        SmartPlaceParseObject object = ParseObject.createWithoutData(SmartPlaceParseObject.class, smartPlaceId);
        put(SMART_PLACE, object);
    }

    @Override
    public JSONObject getObject() {
        return getJSONObject(OBJECT);
    }

    @Override
    public void setObject(JSONObject object) {
        put(OBJECT, object);
    }

    public void update(SmartPlaceConfigurationObject object) {
        setObject(object.getObject());
    }

}

