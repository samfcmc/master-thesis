package com.sam.smartplaceslib.datastore.object;

import org.json.JSONObject;

/**
 *
 */
public interface SmartPlaceInstanceObject extends DataStoreObject {
    UserObject getOwner();

    void setOwner(UserObject owner);

    void setOwner(String ownerId);

    SmartPlaceObject getSmartPlace();

    void setSmartPlace(SmartPlaceObject smartPlace);

    void setSmartPlace(String smartPlaceId);

    JSONObject getData();

    void setData(JSONObject object);

    String getTitle();

    void setTitle(String title);

    String getMessage();

    void setMessage(String message);
}
