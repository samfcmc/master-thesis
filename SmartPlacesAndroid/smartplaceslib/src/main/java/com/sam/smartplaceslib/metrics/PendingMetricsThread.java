package com.sam.smartplaceslib.metrics;

import android.util.Log;

/**
 *
 */
public class PendingMetricsThread extends Thread {
    private Metrics metrics;

    private boolean run;

    public PendingMetricsThread(Metrics metrics) {
        this.metrics = metrics;
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
                PendingElement pendingElement = metrics.takePendingElement();
                pendingElement.process(metrics);
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
