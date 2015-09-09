package com.sam.smartplaceslib.statistics;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 */
public class Value {
    private String name;
    private Collection<Double> values;
    private String unit;

    public Value(String name, String unit) {
        this.name = name;
        this.values = new LinkedList<>();
        this.unit = unit;
    }

    public Value(String name) {
        this(name, "");
    }

    public String getName() {
        return name;
    }

    public Collection<Double> getValues() {
        return values;
    }

    public void addValue(double value) {
        values.add(value);
    }

    public double avg() {
        double sum = 0;
        for (Double value : values) {
            sum += value.doubleValue();
        }
        return sum / values.size();
    }

    public String getUnit() {
        return unit;
    }
}
