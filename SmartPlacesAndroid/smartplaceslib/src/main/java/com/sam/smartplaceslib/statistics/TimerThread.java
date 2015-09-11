package com.sam.smartplaceslib.statistics;

/**
 *
 */
public abstract class TimerThread extends StatisticsThread {
    private long period;
    private boolean repeat;

    public TimerThread(long period, boolean repeat) {
        this.period = period;
        this.repeat = repeat;
    }

    public TimerThread(long period) {
        this(period, false);
    }

    @Override
    public void afterStarted() {
        try {
            do {
                sleep(period);
                onPeriodReached();
            } while (repeat);

        } catch (InterruptedException e) {
            // It is ok to be interrupted :)
        }
    }

    protected abstract void onPeriodReached();
}
