package com.sam.smartplaceslib.datastore.object;

/**
 *
 */
public interface SmartPlaceObject extends DataStoreObject {
    String getName();

    void setName(String name);

    String getUrl();

    void setUrl(String url);

    String getUrlManager();

    void setUrlManager(String url);

    String getDescription();

    void setDescription(String description);
}
