package com.sam.smartplaceslib.statistics;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public interface StatisticsReporter {

    void reportValues(Map<String, Value> values);

    void reportCounters(Collection<Counter> counters);

    void reportEvents(Map<String, Event> events);
}
