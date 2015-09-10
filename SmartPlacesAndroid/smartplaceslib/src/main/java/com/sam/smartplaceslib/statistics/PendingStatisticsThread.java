package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class PendingStatisticsThread extends Thread {
    private Statistics statistics;

    private boolean run;
    private boolean started;

    public PendingStatisticsThread(Statistics statistics) {
        this.statistics = statistics;
        this.run = true;
        this.started = false;
    }

    public boolean isRun() {
        synchronized (this) {
            return run;
        }
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
