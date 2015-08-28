package com.sam.smartplaceslib.metrics;

/**
 *
 */
public class Counter {

    private String name;
    private int value;

    public Counter(String name) {
        this.name = name;
        this.value = 0;
    }

    public void inc() {
        value++;
    }

    public void dec() {
        value--;
    }

    public void reset() {
        value = 0;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
