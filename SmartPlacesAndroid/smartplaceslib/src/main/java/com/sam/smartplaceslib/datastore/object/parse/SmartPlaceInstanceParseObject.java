package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.datastore.object.UserObject;

import org.json.JSONObject;

/**
 *
 */
@ParseClassName("SmartPlaceInstance")
public class SmartPlaceInstanceParseObject extends AbstractParseObject implements SmartPlaceInstanceObject {

    public static final String OWNER = "owner";
    public static final String SMART_PLACE = "smartPlace";
    public static final String DATA = "data";

    public SmartPlaceInstanceParseObject() {
        super();
    }

    public SmartPlaceInstanceParseObject(UserObject owner, SmartPlaceObject smartPlace, JSONObject object) {
        this(owner.getId(), smartPlace.getId(), object);
    }

    public SmartPlaceInstanceParseObject(String ownerId, String smartPlaceId, JSONObject object) {
        super();
        setOwner(ownerId);
        setSmartPlace(smartPlaceId);
        setData(object);
    }

    public SmartPlaceInstanceParseObject(SmartPlaceInstanceObject object) {
        super(object);
        setOwner(object.getOwner().getId());
        setSmartPlace(object.getSmartPlace().getId());
        setData(object.getData());
    }

    public void updateFromParseObject(ParseObject parseObject) {
        setOwner(parseObject.getParseUser(OWNER).getObjectId());
        setSmartPlace(parseObject.getParseObject(SMART_PLACE).getObjectId());
        setData(parseObject.getJSONObject(DATA));
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
    public JSONObject getData() {
        return getJSONObject(DATA);
    }

    @Override
    public void setData(JSONObject object) {
        put(DATA, object);
    }

    public void update(SmartPlaceInstanceObject object) {
        setData(object.getData());
    }

}
