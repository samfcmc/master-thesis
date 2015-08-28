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

        reporter.reportValues(metrics.getValues());
        reporter.reportCounters(metrics.getCounters().values());
    }
}
