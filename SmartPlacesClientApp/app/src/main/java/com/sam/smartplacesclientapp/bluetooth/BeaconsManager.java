package com.sam.smartplacesclientapp.bluetooth;

/**
 *
 */
public interface BeaconsManager {
    public void startScan(BeaconScanCallback callback);
    public void stopScan();
    public void unbind();
}
