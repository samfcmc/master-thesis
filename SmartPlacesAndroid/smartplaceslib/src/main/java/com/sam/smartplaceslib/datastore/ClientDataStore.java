package com.sam.smartplaceslib.datastore;

import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.TagObject;

import org.json.JSONObject;

/**
 *
 */
public interface ClientDataStore extends DataStore {
    void getBeacon(BeaconInfo beaconInfo, final BeaconCallback callback);

    void getSmartPlaceInstances(BeaconObject beaconObject, final SmartPlaceInstancesCallback callback);

    void getTag(String smartPlaceId, BeaconInfo beaconInfo, final TagCallback callback);

    void createTag(BeaconInfo beaconInfo, String smartPlaceConfigurationId, final TagCallback callback);

    void getSmartPlace(SmartPlaceInstanceObject smartPlaceInstanceObject,
                       final SmartPlaceCallback callback);

    void saveSmartPlaceInstance(SmartPlaceInstanceObject object,
                                SmartPlaceInstanceCallback callback);

    void saveTag(TagObject object, JSONObject jsonObject, TagCallback beaconCallback);


}
