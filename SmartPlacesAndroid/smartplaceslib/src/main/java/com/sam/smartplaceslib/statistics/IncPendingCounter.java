package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class IncPendingCounter extends PendingCounter {

    public IncPendingCounter(String name) {
        super(name);
    }

    @Override
    protected void applyOperaration(Counter counter) {
        counter.inc();
    }
}
