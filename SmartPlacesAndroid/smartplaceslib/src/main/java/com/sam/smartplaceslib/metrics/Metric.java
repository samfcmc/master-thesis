package com.sam.smartplaceslib.metrics;

/**
 *
 */
public class Metric<T> {
    private String name;
    private T value;
    private String unit;

    public Metric(String name, T value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public Metric(String name, T value) {
        this(name, value, "");
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }
}
