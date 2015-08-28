package com.sam.smartplaceslib.metrics;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 */
public class Event {

    private String name;
    private Collection<Date> occurences;

    public Event(String name) {
        this.name = name;
        this.occurences = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public Collection<Date> getOccurences() {
        return occurences;
    }

    public void addOccurrence() {
        occurences.add(new Date());
    }
}
