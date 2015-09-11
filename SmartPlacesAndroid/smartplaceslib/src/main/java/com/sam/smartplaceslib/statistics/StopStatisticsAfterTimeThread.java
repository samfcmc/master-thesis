package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class StopStatisticsAfterTimeThread extends TimerThread {
    private Statistics statistics;
    private boolean started;

    public StopStatisticsAfterTimeThread(Statistics statistics, long duration) {
        super(duration);
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
    public void onPeriodReached() {
        started();
        this.statistics.stop();
        this.statistics.report();
    }
}

