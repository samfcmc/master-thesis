package com.sam.smartplaceslib.metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Metrics {

    private MetricsReporter reporter;
    private Map<String, List<Metric<?>>> metrics;
    private Map<String, Counter> counters;

    public Metrics(MetricsReporter reporter) {
        this.reporter = reporter;
        this.metrics = new HashMap<>();
        this.counters = new HashMap<>();
    }

    public void report() {
        for (Map.Entry<String, List<Metric<?>>> entry : metrics.entrySet()) {
            reporter.report(entry.getKey(), entry.getValue());
        }
        for (Counter counter : counters.values()) {
            reporter.report(counter);
        }
    }

    public <T> void value(String category, String name, T value, String unit) {
        List<Metric<?>> found = metrics.get(category);
        if (found == null) {
            metrics.put(category, new LinkedList<Metric<?>>());
            found = metrics.get(category);
        }
        found.add(new Metric<T>(name, value, unit));
    }

    public <T> void value(String category, String name, T value) {
        value(category, name, value, "");
    }

    private Counter getOrCreateCounter(String name) {
        Counter counter = counters.get(name);
        if (counter == null) {
            counter = new Counter(name);
            counters.put(name, counter);
        }
        return counter;
    }

    public void counterInc(String name) {
        Counter counter = getOrCreateCounter(name);
        counter.inc();
    }

    public void counterDec(String name) {
        Counter counter = getOrCreateCounter(name);
        counter.dec();
    }

    public void counterReset(String name) {
        Counter counter = getOrCreateCounter(name);
        counter.reset();
    }

}
