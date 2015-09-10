package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class ReportThread extends Thread {

    private Statistics statistics;
    private boolean started;

    public ReportThread(Statistics statistics) {
        this.statistics = statistics;
        this.started = false;
    }

    public boolean hasStarted() {
        synchronized (this) {
            return started;
        }
    }

    private void started() {
        synchronized (this) {
            started = true;
        }
    }

    @Override
    public void run() {
        started();
        StatisticsReporter reporter = statistics.getReporter();

        reporter.reportValues(statistics.getValues());
        reporter.reportCounters(statistics.getCounters().values());
        reporter.reportEvents(statistics.getEvents());
    }
}
