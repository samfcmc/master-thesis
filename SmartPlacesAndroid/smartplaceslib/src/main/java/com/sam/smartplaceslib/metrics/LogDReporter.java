package com.sam.smartplaceslib.metrics;

import android.util.Log;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class LogDReporter implements MetricsReporter {

    private static final String TAG = "Metrics Report";

    @Override
    public void report(Map<String, Collection<Metric<?>>> metrics) {
        header();
        for (Map.Entry<String, Collection<Metric<?>>> entry : metrics.entrySet()) {
            String category = entry.getKey();
            String message = String.format("==> Category: %s", category);
            log(message);
            for (Metric metric : entry.getValue()) {
                message = String.format("Category: %s, Metric %s, value %s", category,
                        metric.getName(), metric.getValue().toString() + " " + metric.getUnit());
                log(message);
            }
        }

    }

    private void header() {
        log("------------------------------------");
        log("Metrics start: " + new Date());
        log("------------------------------------");
    }

    private void log(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void report(Collection<Counter> counters) {
        for (Counter counter : counters) {
            String message = String.format("Counter: %s, Value: %d", counter.getName(),
                    counter.getValue());
            log(message);
        }
    }
}
