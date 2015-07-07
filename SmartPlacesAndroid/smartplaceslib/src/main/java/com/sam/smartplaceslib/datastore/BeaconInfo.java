package com.sam.smartplaceslib.datastore;

/**
 *
 */
public class BeaconInfo {
    private String uuid;
    private int major;
    private int minor;

    public BeaconInfo(String uuid, int major, int minor) {
        this.uuid = uuid.replace("0x", "");
        this.major = major;
        this.minor = minor;
    }

    public String getUuid() {
        return uuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }
}
