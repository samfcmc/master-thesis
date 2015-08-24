package com.sam.smartplaceslib.metrics;

import java.util.Collection;

/**
 *
 */
public interface MetricsReporter {

    void report(String category, Collection<Metric<?>> metrics);

    void report(Counter counter);
}
