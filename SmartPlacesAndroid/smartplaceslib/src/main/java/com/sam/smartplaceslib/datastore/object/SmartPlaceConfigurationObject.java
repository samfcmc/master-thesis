package com.sam.smartplaceslib.datastore.object;

import org.json.JSONObject;

/**
 *
 */
public interface SmartPlaceConfigurationObject extends DataStoreObject {
    UserObject getOwner();
    void setOwner(UserObject owner);
    void setOwner(String ownerId);
    SmartPlaceObject getSmartPlace();
    void setSmartPlace(SmartPlaceObject smartPlace);
    void setSmartPlace(String smartPlaceId);
    JSONObject getObject();
    void setObject(JSONObject object);
}
