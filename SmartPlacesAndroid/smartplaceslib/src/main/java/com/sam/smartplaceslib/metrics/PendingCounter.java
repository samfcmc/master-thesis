package com.sam.smartplaceslib.metrics;

/**
 *
 */
public abstract class PendingCounter implements PendingMetric {

    private String name;

    public PendingCounter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void process(Metrics metrics) {
        Counter counter = metrics.getOrCreateCounter(name);
        applyOperaration(counter);
    }

    protected abstract void applyOperaration(Counter counter);
}
