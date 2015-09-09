package com.sam.smartplaceslib.statistics;

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
    public void process(Statistics statistics) {
        statistics.processPendingCounter(this);
    }

    protected abstract void applyOperaration(Counter counter);
}
