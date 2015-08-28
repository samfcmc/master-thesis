package com.sam.smartplaceslib.metrics;

/**
 *
 */
public abstract class PendingCounter implements PendingElement {

    private String name;

    public PendingCounter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void process(Metrics metrics) {
        metrics.processPendingCounter(this);
    }

    protected abstract void applyOperaration(Counter counter);
}
