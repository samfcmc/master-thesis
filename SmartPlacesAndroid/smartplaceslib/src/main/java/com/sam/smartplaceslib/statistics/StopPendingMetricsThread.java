package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class StopPendingMetricsThread implements PendingElement {
    @Override
    public void process(Statistics statistics) {
        statistics.processStopPendingMetricsThread(this);
    }
}
