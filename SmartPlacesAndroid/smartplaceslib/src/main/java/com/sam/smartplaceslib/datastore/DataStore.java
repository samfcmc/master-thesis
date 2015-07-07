package com.sam.smartplaceslib.datastore;

import android.content.Intent;

import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.DummyCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesConfigurationsCallback;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceConfigurationObject;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.datastore.object.UserObject;

import org.json.JSONObject;

/**
 * Interface to fetch and send data to the backend
 */
public interface DataStore {

    void createDummy(String name, final DummyCallback callback);

    void login(LoginStrategy loginStrategy,
               LoginCallback callback);

    void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data);

    UserObject getCurrentUser();

    boolean isUserLoggedIn();

    void logout(final LogoutCallback callback);

    void getBeacon(String uuid, int major, int minor, final BeaconCallback callback);

    void getBeacon(BeaconInfo beaconInfo, final BeaconCallback callback);

    void getSmartPlaceConfigurations(BeaconObject beaconObject, final SmartPlacesConfigurationsCallback callback);

    void getTag(BeaconInfo beaconInfo, final TagCallback callback);

    void createTag(BeaconInfo beaconInfo, String smartPlaceConfigurationId, final TagCallback callback);

    void getSmartPlaces(BeaconObject beaconObject, final SmartPlacesCallback callback);

    void saveBeacon(BeaconObject beaconObject, String smartPlaceConfigurationId,
                    final BeaconCallback callback);

    void getSmartPlaceConfiguration(String smartPlaceId, final SmartPlaceConfigurationCallback callback);

    void getSmartPlaceConfiguration(String smartPlaceId, String beaconId,
                                    final SmartPlaceConfigurationCallback callback);

    void createSmartPlaceConfiguration(String smartPlaceId, String name, String message,
                                       SmartPlaceConfigurationCallback callback);

    void saveSmartPlaceConfiguration(SmartPlaceConfigurationObject object,
                                     SmartPlaceConfigurationCallback callback);


    void saveTag(TagObject object, JSONObject jsonObject, TagCallback beaconCallback);
}
