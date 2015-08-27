package com.sam.smartplaceslib.datastore.callback.observer.metrics;

import com.sam.smartplaceslib.datastore.callback.observer.DataStoreCallbackDoneObserver;
import com.sam.smartplaceslib.metrics.Metrics;

/**
 *
 */
public class DataStoreLatencyObserver implements DataStoreCallbackDoneObserver {

    private Metrics metrics;
    private String requestName;
    private long requestStart;
    private static final String unit = "ms";


    public DataStoreLatencyObserver(Metrics metrics, String requestName, long requestStart) {
        this.metrics = metrics;
        this.requestName = requestName;
        this.requestStart = requestStart;
    }

    @Override
    public void done() {
        long latency = System.currentTimeMillis() - requestStart;
        metrics.value("Requests", requestName, latency, unit);
    }
}
