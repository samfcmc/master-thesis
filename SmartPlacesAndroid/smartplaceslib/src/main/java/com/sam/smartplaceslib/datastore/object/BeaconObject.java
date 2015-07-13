package com.sam.smartplaceslib.datastore.object;

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

}
