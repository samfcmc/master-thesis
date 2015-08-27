package com.sam.smartplaceslib.metrics;

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

        reporter.report(metrics.getMetrics());
        reporter.report(metrics.getCounters().values());
    }
}
