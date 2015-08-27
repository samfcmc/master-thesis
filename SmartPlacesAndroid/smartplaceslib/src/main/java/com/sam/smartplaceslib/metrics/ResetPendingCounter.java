package com.sam.smartplaceslib.metrics;

/**
 *
 */
public class ResetPendingCounter extends PendingCounter {

    public ResetPendingCounter(String name) {
        super(name);
    }

    @Override
    protected void applyOperaration(Counter counter) {
        counter.reset();
    }
}
