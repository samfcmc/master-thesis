package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class PendingStatisticsThread extends StatisticsThread {
    private Statistics statistics;

    private boolean run;

    public PendingStatisticsThread(Statistics statistics) {
        super();
        this.statistics = statistics;
        this.run = true;
    }

    public boolean isRun() {
        synchronized (this) {
            return run;
        }
    }

    @Override
    protected void afterStarted() {
        try {
            while (isRun()) {
                PendingElement pendingElement = statistics.takePendingElement();
                pendingElement.process(statistics);
            }
        } catch (InterruptedException e) {
            // Can be interrupted... No problem :)
        }
    }

    public void stopRunning() {
        synchronized (this) {
            run = false;
        }
    }
}
