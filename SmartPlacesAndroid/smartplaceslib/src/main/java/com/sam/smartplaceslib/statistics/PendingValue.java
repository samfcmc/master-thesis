package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class PendingValue implements PendingElement {
    private String name;
    private double value;
    private String unit;

    public PendingValue(String name, double value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public void process(Statistics statistics) {
        statistics.processPendingValue(this);
    }

}
