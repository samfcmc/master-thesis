package com.sam.smartplaceslib.metrics;

/**
 *
 */
public class PendingValue implements PendingElement {
    private String category;
    private String name;
    private double value;
    private String unit;

    public PendingValue(String category, String name, double value, String unit) {
        this.category = category;
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getCategory() {
        return category;
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
    public void process(Metrics metrics) {
        metrics.processPendingValue(this);
    }

}
