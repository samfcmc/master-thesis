package com.sam.smartplaceslib.datastore.callback.observer.metrics;

import com.sam.smartplaceslib.datastore.callback.observer.DataStoreCallbackDoneObserver;
import com.sam.smartplaceslib.statistics.Statistics;

/**
 *
 */
public class DataStoreLatencyObserver implements DataStoreCallbackDoneObserver {

    private Statistics statistics;
    private String requestName;
    private long requestStart;
    private static final String unit = "ms";


    public DataStoreLatencyObserver(Statistics statistics, String requestName, long requestStart) {
        this.statistics = statistics;
        this.requestName = requestName;
        this.requestStart = requestStart;
    }

    @Override
    public void done() {
        long latency = System.currentTimeMillis() - requestStart;
        statistics.value("Requests." + requestName, latency, unit);
    }
}
