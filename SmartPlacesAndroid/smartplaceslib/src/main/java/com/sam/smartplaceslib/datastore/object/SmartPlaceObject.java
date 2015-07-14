package com.sam.smartplaceslib.datastore.object;

/**
 *
 */
public interface SmartPlaceObject extends DataStoreObject {
    public String getName();
    public void setName(String name);
    public String getUrl();
    public void setUrl(String url);
    public String getDescription();
    public void setDescription(String description);
}
