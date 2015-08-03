package com.sam.smartplaceslib.datastore.object;

/**
 *
 */
public interface BeaconObject extends DataStoreObject {
    String getUUID();

    void setUUID(String uuid);

    int getMajor();

    void setMajor(int major);

    int getMinor();

    void setMinor(int minor);

    String getName();

    String getIcon();


}
