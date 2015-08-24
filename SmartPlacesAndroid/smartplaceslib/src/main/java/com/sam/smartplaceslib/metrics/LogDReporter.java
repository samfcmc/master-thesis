package com.sam.smartplaceslib.metrics;

import android.util.Log;

import java.util.Collection;
import java.util.Date;

/**
 *
 */
public class LogDReporter implements MetricsReporter {

    private static final String TAG = "Metrics Report";

    @Override
    public void report(String category, Collection<Metric<?>> metrics) {
        header();
        String message = String.format("Category: %s", category);
        log(message);
        for (Metric metric : metrics) {
            message = String.format("Category: %s, Metric %s, value %s", category,
                    metric.getName(), metric.getValue().toString() + " " + metric.getUnit());
            log(message);
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
    public void report(Counter counter) {
        String message = String.format("Counter: %s, Value: %d", counter.getName(),
                counter.getValue());
        log(message);
    }
}
