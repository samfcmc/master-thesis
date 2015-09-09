package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class ReportThread extends Thread {

    private Statistics statistics;

    public ReportThread(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void run() {
        StatisticsReporter reporter = statistics.getReporter();

        reporter.reportValues(statistics.getValues());
        reporter.reportCounters(statistics.getCounters().values());
    }
}
