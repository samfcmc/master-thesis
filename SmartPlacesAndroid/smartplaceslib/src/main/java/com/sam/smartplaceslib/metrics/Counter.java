package com.sam.smartplaceslib.metrics;

/**
 *
 */
public class Counter extends Metric<Integer> {
    public Counter(String name) {
        super(name, 0);
    }

    public void inc() {
        setValue(getValue() + 1);
    }

    public void dec() {
        setValue(getValue() - 1);
    }

    public void reset() {
        setValue(0);
    }

}
