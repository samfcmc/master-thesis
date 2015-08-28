package com.sam.smartplaceslib.metrics;

/**
 *
 */
public class StopPendingMetricsThread implements PendingElement {
    @Override
    public void process(Metrics metrics) {
        metrics.processStopPendingMetricsThread(this);
    }
}
