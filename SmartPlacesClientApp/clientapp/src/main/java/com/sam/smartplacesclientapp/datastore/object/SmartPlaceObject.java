package com.sam.smartplacesclientapp.datastore.object;

/**
 *
 */
public interface SmartPlaceObject extends DataStoreObject {
    public String getName();
    public void setName(String name);
    public String getUrl();
    public void setUrl(String url);
    public String getMessage();
    public void setMessage(String message);
}
