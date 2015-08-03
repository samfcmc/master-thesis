package com.sam.smartplaceslib.datastore;

/**
 *
 */
public class BeaconInfo {
    private String uuid;
    private int major;
    private int minor;
    private double distance;

    public BeaconInfo(String uuid, int major, int minor, double distance) {
        this.uuid = uuid.replace("0x", "");
        this.major = major;
        this.minor = minor;
        this.distance = distance;
    }

    public BeaconInfo(String uuid, int major, int minor) {
        this(uuid, major, minor, 0);
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

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BeaconInfo) {
            BeaconInfo beaconInfo = (BeaconInfo) o;
            return beaconInfo.getUuid().equals(getUuid()) && (beaconInfo.getMajor() == getMajor())
                    && beaconInfo.getMinor() == getMinor();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
