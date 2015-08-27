package com.sam.smartplaceslib.metrics;

import java.util.List;

/**
 *
 */
public class PendingValue<T> implements PendingMetric {
    private String category;
    private String name;
    private T value;
    private String unit;

    public PendingValue(String category, String name, T value, String unit) {
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

    public T getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public void process(Metrics metrics) {
        List<Metric<?>> found = metrics.getOrCreateCategory(category);
        found.add(new Metric<T>(name, value, unit));
    }
}
