package com.sam.smartplaceslib.datastore.object;

import org.json.JSONObject;

/**
 * Tag
 */
public interface TagObject extends DataStoreObject {

    JSONObject getData();

    void setData(JSONObject data);

    BeaconObject getBeacon();
}
