package com.sam.smartplaceslib.metrics;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public interface MetricsReporter {

    void report(Map<String, Collection<Metric<?>>> metrics);

    void report(Collection<Counter> counters);
}
