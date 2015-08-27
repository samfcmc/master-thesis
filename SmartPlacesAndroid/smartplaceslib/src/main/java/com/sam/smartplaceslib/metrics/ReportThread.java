package com.sam.smartplaceslib.metrics;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReportThread extends Thread {

    private Metrics metrics;

    public ReportThread(Metrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void run() {
        MetricsReporter reporter = metrics.getReporter();

        for (Map.Entry<String, List<Metric<?>>> entry : metrics.getMetrics().entrySet()) {
            reporter.report(entry.getKey(), entry.getValue());
        }
        for (Counter counter : metrics.getCounters().values()) {
            reporter.report(counter);
        }
    }
}
