package com.sam.smartplaceslib.metrics;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public interface MetricsReporter {

    void reportValues(Map<String, Value> values);

    void reportCounters(Collection<Counter> counters);

    void reportEvents(Map<String, Collection<Event>> events);
}
