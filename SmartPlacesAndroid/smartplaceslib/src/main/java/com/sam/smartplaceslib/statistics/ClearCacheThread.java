package com.sam.smartplaceslib.statistics;


import com.sam.smartplaceslib.bluetooth.BeaconsCache;

/**
 *
 */
public class ClearCacheThread extends TimerThread {
    private BeaconsCache cache;

    public ClearCacheThread(long period, BeaconsCache cache) {
        super(period, true);
        this.cache = cache;
    }

    @Override
    protected void onPeriodReached() {
        cache.clear();
    }
}
