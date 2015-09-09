package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class StopStatisticsAfterTimeThread extends Thread {
    private Statistics statistics;
    private long duration;

    public StopStatisticsAfterTimeThread(Statistics statistics, long duration) {
        this.statistics = statistics;
        this.duration = duration;
    }

    @Override
    public void run() {
        try {
            sleep(this.duration);
            this.statistics.stop();
            this.statistics.report();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

