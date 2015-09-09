package com.sam.smartplaceslib.statistics;

import android.util.Log;

/**
 *
 */
public class PendingStatisticsThread extends Thread {
    private Statistics statistics;

    private boolean run;

    public PendingStatisticsThread(Statistics statistics) {
        this.statistics = statistics;
        this.run = true;
    }

    public boolean isRun() {
        synchronized (this) {
            return run;
        }
    }

    @Override
    public void run() {
        try {
            while (isRun()) {
                PendingElement pendingElement = statistics.takePendingElement();
                pendingElement.process(statistics);
            }
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void stopRunning() {
        synchronized (this) {
            run = false;
        }
    }
}
