package com.sam.smartplaceslib.statistics;

/**
 *
 */
public class PendingEvent implements PendingElement {

    private String category;
    private String name;

    public PendingEvent(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    @Override
    public void process(Statistics statistics) {
        statistics.processPendingEvent(this);
    }
}
