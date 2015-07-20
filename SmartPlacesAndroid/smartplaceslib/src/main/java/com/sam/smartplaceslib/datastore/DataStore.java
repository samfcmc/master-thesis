package com.sam.smartplaceslib.datastore;

import android.content.Intent;

import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.DeleteCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.datastore.object.UserObject;

import org.json.JSONObject;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore {

    void login(LoginStrategy loginStrategy,
               LoginCallback callback);

    void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);

    UserObject getCurrentUser();

    boolean isUserLoggedIn();

    void logout(final LogoutCallback callback);

    void getBeacon(BeaconInfo beaconInfo, final BeaconCallback callback);

    void getSmartPlaces(final SmartPlacesCallback callback);

    void getSmartPlaceInstances(BeaconObject beaconObject, final SmartPlaceInstancesCallback callback);

    void getUserSmartPlaceInstances(final SmartPlaceInstancesCallback callback);

    void getTag(String smartPlaceId, BeaconInfo beaconInfo, final TagCallback callback);

    void createTag(BeaconInfo beaconInfo, String smartPlaceConfigurationId, final TagCallback callback);

    void getSmartPlace(SmartPlaceInstanceObject smartPlaceInstanceObject,
                       final SmartPlaceCallback callback);

    void getSmartPlaceConfiguration(String smartPlaceId, final SmartPlaceInstanceCallback callback);

    void createSmartPlaceInstance(String smartPlaceId, String title, String message,
                                  final SmartPlaceInstanceCallback callback);

    void saveSmartPlaceInstance(SmartPlaceInstanceObject object,
                                SmartPlaceInstanceCallback callback);


    void saveTag(TagObject object, JSONObject jsonObject, TagCallback beaconCallback);

    void deleteSmartPlaceInstance(String id, final DeleteCallback callback);

    void updateSmartPlaceInstance(String id, String title, String message,
                                  SmartPlaceInstanceCallback callback);
}
