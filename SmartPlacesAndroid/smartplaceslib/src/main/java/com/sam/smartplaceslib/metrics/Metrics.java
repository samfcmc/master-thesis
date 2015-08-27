package com.sam.smartplaceslib.metrics;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class Metrics {

    private MetricsReporter reporter;
    private Map<String, List<Metric<?>>> metrics;
    private Map<String, Counter> counters;
    private BlockingQueue<PendingMetric> queue;
    private PendingMetricsThread pendingMetricsThread;
    private ReportThread reportThread;

    public Metrics(MetricsReporter reporter) {
        this.reporter = reporter;
        this.metrics = new HashMap<>();
        this.counters = new HashMap<>();
        this.queue = new LinkedBlockingQueue<>();
        this.pendingMetricsThread = new PendingMetricsThread(this);
        this.reportThread = new ReportThread(this);
    }

    public Map<String, List<Metric<?>>> getMetrics() {
        return metrics;
    }

    public Map<String, Counter> getCounters() {
        return counters;
    }

    public MetricsReporter getReporter() {
        return reporter;
    }

    public PendingMetric takePendingMetric() throws InterruptedException {
        return queue.take();
    }

    public void start() {
        if (!pendingMetricsThread.isAlive()) {
            pendingMetricsThread.start();
        }
    }

    private void putInQueue(PendingMetric pendingMetric) {
        try {
            queue.put(pendingMetric);
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void stop() {
        if (pendingMetricsThread.isAlive()) {
            pendingMetricsThread.stopRunning();
            try {
                pendingMetricsThread.join();
            } catch (InterruptedException e) {
                Log.e("Error", e.getMessage());
            }
        }
        if (reportThread.isAlive()) {
            try {
                reportThread.join();
            } catch (InterruptedException e) {
                Log.e("Error", e.getMessage());
            }
        }
    }

    public void report() {
        reportThread.start();
    }

    public <T> void value(String category, String name, T value, String unit) {
        PendingValue<T> pendingValue = new PendingValue<>(category, name, value, unit);
        putInQueue(pendingValue);
    }

    public <T> void value(String category, String name, T value) {
        value(category, name, value, "");
    }

    public Counter getOrCreateCounter(String name) {
        Counter counter = counters.get(name);
        if (counter == null) {
            counter = new Counter(name);
            counters.put(name, counter);
        }
        return counter;
    }

    public void counterInc(String name) {
        PendingCounter pendingCounter = new IncPendingCounter(name);
        putInQueue(pendingCounter);
    }

    public void counterDec(String name) {
        PendingCounter pendingCounter = new DecPendingCounter(name);
        putInQueue(pendingCounter);
    }

    public void counterReset(String name) {
        PendingCounter pendingCounter = new ResetPendingCounter(name);
        putInQueue(pendingCounter);
    }

    public List<Metric<?>> getOrCreateCategory(String category) {
        List<Metric<?>> found = metrics.get(category);
        if (found == null) {
            metrics.put(category, new LinkedList<Metric<?>>());
            found = metrics.get(category);
        }

        return found;
    }

}
