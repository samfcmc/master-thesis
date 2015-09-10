package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class StopStatisticsAfterTimeThread extends Thread {
    private Statistics statistics;
    private long duration;
    private boolean started;

    public StopStatisticsAfterTimeThread(Statistics statistics, long duration) {
        this.statistics = statistics;
        this.duration = duration;
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
        try {
            sleep(this.duration);
            this.statistics.stop();
            this.statistics.report();
        } catch (InterruptedException e) {
            // Can be interrupted... No problem
        }
    }
}

