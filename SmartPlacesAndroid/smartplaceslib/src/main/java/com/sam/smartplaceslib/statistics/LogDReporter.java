package com.sam.smartplaceslib.statistics;

import android.util.Log;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class LogDReporter implements StatisticsReporter {

    private static final String TAG = "Statistics Report";

    @Override
    public void reportValues(Map<String, Value> values) {
        header();
        for (Map.Entry<String, Value> entry : values.entrySet()) {
            Value value = entry.getValue();
            String message = String.format("\t\t Values for: %s", value.getName());
            log(message);
            for (Double v : value.getValues()) {
                message = String.format("\t\t\t Value: %.2f %s", v.doubleValue(), value.getUnit());
                log(message);
            }
            message = String.format("\t\t Average for %s: %.2f %s", value.getName(), value.avg(), value.getUnit());
            log(message);
        }

    }

    private void header() {
        log("------------------------------------");
        log("Statistics startSession: " + new Date());
        log("------------------------------------");
    }

    private void log(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void reportCounters(Collection<Counter> counters) {
        for (Counter counter : counters) {
            String message = String.format("Counter: %s, Value: %d", counter.getName(),
                    counter.getValue());
            log(message);
        }
    }

    @Override
    public void reportEvents(Map<String, Collection<Event>> events) {
        for (Map.Entry<String, Collection<Event>> entry : events.entrySet()) {
            String category = entry.getKey();
            String message = String.format("==> Event Category: %s", category);
            log(message);
            for (Event event : entry.getValue()) {
                message = String.format("\t\t Event: %s", event.getName());
                log(message);
                for (Date occurrence : event.getOccurences()) {
                    message = String.format("\t\t\t Occurrence: %s", occurrence.toString());
                    log(message);
                }
                message = String.format("\t\t\t Total occurrences: %d", event.getOccurences().size());
                log(message);
            }
        }
    }
}
