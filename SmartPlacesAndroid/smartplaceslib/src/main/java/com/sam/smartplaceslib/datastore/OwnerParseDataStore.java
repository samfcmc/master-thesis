package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.callback.DeleteDataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.callback.parse.SmartPlaceFindCallback;
import com.sam.smartplaceslib.datastore.callback.parse.SmartPlaceInstanceFindCallback;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;

import org.json.JSONObject;

import java.io.IOException;

/**
 *
 */
public class OwnerParseDataStore extends AbstractParseDataStore implements OwnerDataStore {

    public static OwnerDataStore fromRawJsonResource(Application application, int rawJsonResourceId) throws IOException {
        DataStoreCredentials dataStoreCredentials = DataStoreCredentials
                .fromJsonRawResource(application, rawJsonResourceId);
        return new OwnerParseDataStore(application, dataStoreCredentials);
    }

    public OwnerParseDataStore(Application application, DataStoreCredentials dataStoreCredentials) {
        super(application, dataStoreCredentials);
    }

    @Override
    public void getSmartPlaces(final SmartPlacesCallback callback) {
        ParseQuery<SmartPlaceParseObject> query = getQuery(SmartPlaceParseObject.class);
        find(query, new SmartPlaceFindCallback(callback));
    }

    @Override
    public void getUserSmartPlaceInstances(final SmartPlaceInstancesCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query = getQuery(SmartPlaceInstanceParseObject.class);
        ParseUser user = ParseUser.getCurrentUser();
        query.include(SmartPlaceInstanceParseObject.SMART_PLACE);
        query.whereEqualTo(SmartPlaceInstanceParseObject.OWNER, user);
        find(query, new SmartPlaceInstanceFindCallback(callback));
    }

    @Override
    public void deleteSmartPlaceInstance(String id, final DeleteDataStoreCallback callback) {
        SmartPlaceInstanceParseObject parseObject = ParseObject
                .createWithoutData(SmartPlaceInstanceParseObject.class, id);
        delete(parseObject, callback);
    }

    @Override
    public void updateSmartPlaceInstance(String id, String title, String message,
                                         final SmartPlaceInstanceCallback callback) {
        final SmartPlaceInstanceParseObject object = ParseObject.createWithoutData(
                SmartPlaceInstanceParseObject.class, id);
        object.setTitle(title);
        object.setMessage(message);
        save(object, callback);
    }

    @Override
    public void createSmartPlaceInstance(String smartPlaceId, String title, String message,
                                         final SmartPlaceInstanceCallback callback) {
        ParseUser user = ParseUser.getCurrentUser();
        final SmartPlaceInstanceParseObject smartPlaceInstance =
                new SmartPlaceInstanceParseObject(user.getObjectId(), smartPlaceId, new JSONObject(),
                        title, message);
        save(smartPlaceInstance, callback);
    }
}
