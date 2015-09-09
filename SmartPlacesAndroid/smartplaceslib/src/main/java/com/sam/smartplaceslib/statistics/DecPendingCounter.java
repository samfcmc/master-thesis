package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class DecPendingCounter extends PendingCounter {

    public DecPendingCounter(String name) {
        super(name);
    }

    @Override
    protected void applyOperaration(Counter counter) {
        counter.dec();
    }
}
