package com.sam.smartplaceslib.statistics;

import android.app.Application;
import android.util.Log;

import com.google.gson.JsonObject;
import com.sam.smartplaceslib.bluetooth.BeaconsCache;
import com.sam.smartplaceslib.bluetooth.BeaconsManager;
import com.sam.smartplaceslib.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class Statistics {

    private StatisticsReporter reporter;
    private Map<String, Value> metrics;
    private Map<String, Counter> counters;
    private Map<String, Event> events;
    private BlockingQueue<PendingElement> queue;
    private PendingStatisticsThread pendingStatisticsThread;
    private ReportThread reportThread;
    private StopStatisticsAfterTimeThread stopThread;
    private boolean backgroundMode;
    private ClearCacheThread clearCacheThread;
    private boolean clearCache;

    public Statistics(StatisticsReporter reporter) {
        this.reporter = reporter;
        this.metrics = new HashMap<>();
        this.counters = new HashMap<>();
        this.queue = new LinkedBlockingQueue<>();
        this.pendingStatisticsThread = new PendingStatisticsThread(this);
        this.reportThread = new ReportThread(this);
        this.events = new HashMap<>();
        this.stopThread = null;
        this.backgroundMode = false;
        this.clearCache = false;
    }

    public Map<String, Value> getValues() {
        return metrics;
    }

    public Map<String, Counter> getCounters() {
        return counters;
    }

    public StatisticsReporter getReporter() {
        return reporter;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public PendingElement takePendingElement() throws InterruptedException {
        return queue.take();
    }

    public void startSession(Application application, BeaconsManager beaconsManager, BeaconsCache cache) {
        if (!pendingStatisticsThread.isAlive()) {
            if (readFromJsonFile(application, beaconsManager, cache)) {
                if (pendingStatisticsThread.hasStarted()) {
                    pendingStatisticsThread.interrupt();
                }
                pendingStatisticsThread = new PendingStatisticsThread(this);
                pendingStatisticsThread.start();
            }
        }
    }

    public void startSession(Application application, BeaconsManager beaconsManager) {
        startSession(application, beaconsManager, null);
    }

    private boolean readFromJsonFile(Application application, BeaconsManager beaconsManager, BeaconsCache cache) {
        int rawId = application.getResources().getIdentifier("statistics", "raw", application.getPackageName());
        boolean statistics = false;
        if (rawId != 0) {
            JsonObject jsonObject = JsonUtils.readJsonFromRawResource(application, rawId);
            statistics = jsonObject.get("statistics").getAsBoolean();
            if (statistics) {
                int seconds = jsonObject.get("time").getAsInt();
                long millis = seconds * 1000;
                int scanIntervalSeconds = jsonObject.get("scanInterval").getAsInt();
                long scanInterval = scanIntervalSeconds * 1000;
                beaconsManager.updateScanPeriodInBackgroundMode(scanInterval);
                beaconsManager.updateScanPeriodInForegroundMode(scanInterval);
                this.backgroundMode = jsonObject.get("backgroundMode").getAsBoolean();
                if (this.backgroundMode && cache != null) {
                    this.clearCache = jsonObject.get("clearCache").getAsBoolean();
                }
                this.stopThread = new StopStatisticsAfterTimeThread(this, millis);
                this.stopThread.start();
            }
        }
        return statistics;
    }

    public boolean clearCache() {
        return clearCache;
    }

    public boolean isBackgroundMode() {
        return backgroundMode;
    }

    private void putInQueue(PendingElement pendingElement) {
        try {
            queue.put(pendingElement);
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void stop() {
        if (pendingStatisticsThread.isAlive()) {
            StopPendingMetricsThread stop = new StopPendingMetricsThread();
            putInQueue(stop);
            try {
                pendingStatisticsThread.join();
            } catch (InterruptedException e) {
                Log.e("Error", e.getMessage());
            }
        }
        if (reportThread.isAlive()) {
            try {
                reportThread.join();
            } catch (InterruptedException e) {
                Log.e("Error", e.getMessage());
            }
        }
        if (stopThread != null) {
            stopThread.interrupt();
        }
    }

    public void report() {
        if (this.stopThread != null && !reportThread.isAlive()) {
            if (!reportThread.hasStarted()) {
                reportThread.start();
            } else {
                try {
                    reportThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void value(String name, double value, String unit) {
        PendingValue pendingValue = new PendingValue(name, value, unit);
        putInQueue(pendingValue);
    }

    public <T> void value(String name, double value) {
        value(name, value, "");
    }

    public void event(String category, String name) {
        PendingEvent pendingEvent = new PendingEvent(category, name);
        putInQueue(pendingEvent);
    }

    private Counter getOrCreateCounter(String name) {
        Counter counter = counters.get(name);
        if (counter == null) {
            counter = new Counter(name);
            counters.put(name, counter);
        }
        return counter;
    }

    public void counterInc(String name) {
        PendingCounter pendingCounter = new IncPendingCounter(name);
        putInQueue(pendingCounter);
    }

    public void counterDec(String name) {
        PendingCounter pendingCounter = new DecPendingCounter(name);
        putInQueue(pendingCounter);
    }

    public void counterReset(String name) {
        PendingCounter pendingCounter = new ResetPendingCounter(name);
        putInQueue(pendingCounter);
    }

    public void processPendingValue(PendingValue pendingValue) {
        Value found = metrics.get(pendingValue.getName());
        if (found == null) {
            found = new Value(pendingValue.getName(), pendingValue.getUnit());
            metrics.put(pendingValue.getName(), found);
        }
        found.addValue(pendingValue.getValue());
    }

    public void processPendingCounter(PendingCounter pendingCounter) {
        Counter counter = getOrCreateCounter(pendingCounter.getName());
        pendingCounter.applyOperaration(counter);
    }

    public void processPendingEvent(PendingEvent pendingEvent) {
        Event found = events.get(pendingEvent.getCategory());
        if (found == null) {
            found = new Event(pendingEvent.getName());
            events.put(pendingEvent.getCategory(), found);
        }

        found.addOccurrence();
    }

    public void processStopPendingMetricsThread(StopPendingMetricsThread stopPendingMetricsThread) {
        pendingStatisticsThread.stopRunning();
    }
}
