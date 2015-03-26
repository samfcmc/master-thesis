package com.sam.smartplacesclientapp.datastore.object;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 *
 */
public interface BeaconObject extends DataStoreObject {
    public String getUUID();
    public void setUUID(String uuid);
    public int getMajor();
    public void setMajor(int major);
    public int getMinor();
    public void setMinor(int minor);
    public JSONObject getObject();
    public void setObject(JSONObject object);
}
