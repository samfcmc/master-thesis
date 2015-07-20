package com.sam.smartplaceslib.datastore;

import com.sam.smartplaceslib.datastore.callback.DeleteCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;

/**
 *
 */
public interface OwnerDataStore extends DataStore {

    void getSmartPlaces(final SmartPlacesCallback callback);

    void getUserSmartPlaceInstances(final SmartPlaceInstancesCallback callback);

    void deleteSmartPlaceInstance(String id, final DeleteCallback callback);

    void updateSmartPlaceInstance(String id, String title, String message,
                                  SmartPlaceInstanceCallback callback);

    void createSmartPlaceInstance(String smartPlaceId, String title, String message,
                                  SmartPlaceInstanceCallback callback);
}
