package com.sam.smartplaceslib.statistics;

/**
 *
 */
public abstract class StatisticsThread extends Thread {
    private boolean started;

    public StatisticsThread() {
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
        afterStarted();
    }

    protected abstract void afterStarted();
}
