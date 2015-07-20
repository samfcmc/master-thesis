package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.callback.parse.BeaconGetCallback;
import com.sam.smartplaceslib.datastore.callback.parse.SmartPlaceInstanceGetCallback;
import com.sam.smartplaceslib.datastore.callback.parse.TagGetCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.TagParseObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStore implementation to work with Parse.com BaaS
 */
public class ClientParseDataStore extends AbstractDataStore implements ClientDataStore {

    public static ClientDataStore fromRawJsonResource(Application application, int jsonRawResourceId) throws IOException {
        DataStoreCredentials dataStoreCredentials = DataStoreCredentials
                .fromJsonRawResource(application, jsonRawResourceId);
        return new ClientParseDataStore(application, dataStoreCredentials);
    }

    public ClientParseDataStore(Application application, DataStoreCredentials dataStoreCredentials) {
        super(application, dataStoreCredentials);
    }

    @Override
    public void getBeacon(BeaconInfo beaconInfo, final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = getBeaconQuery(beaconInfo);

        query.getFirstInBackground(new BeaconGetCallback(callback));
    }

    @Override
    public void getSmartPlaceInstances(BeaconObject beaconObject,
                                       final SmartPlaceInstancesCallback callback) {
        ParseQuery<TagParseObject> tagQuery = getQuery(TagParseObject.class);
        BeaconParseObject beaconParseObject = ParseObject.createWithoutData(BeaconParseObject.class, beaconObject.getId());
        tagQuery = tagQuery.whereEqualTo(TagParseObject.BEACON, beaconParseObject);
        tagQuery = tagQuery.include(TagParseObject.SMARTPLACE_INSTANCE);
        tagQuery.findInBackground(new FindCallback<TagParseObject>() {
            @Override
            public void done(List<TagParseObject> list, ParseException e) {
                List<SmartPlaceInstanceObject> result = new ArrayList<SmartPlaceInstanceObject>(list.size());
                for (TagParseObject tag : list) {
                    ParseObject parseObject = tag.getParseObject(TagParseObject.SMARTPLACE_INSTANCE);
                    SmartPlaceInstanceParseObject configuration = ParseObject.createWithoutData(SmartPlaceInstanceParseObject.class, parseObject.getObjectId());
                    configuration.updateFromParseObject(parseObject);
                    result.add(configuration);
                }
                callback.done(result);
            }
        });

    }

    private ParseQuery<BeaconParseObject> getBeaconQuery(BeaconInfo beaconInfo) {
        ParseQuery<BeaconParseObject> beaconQuery = getQuery(BeaconParseObject.class);
        beaconQuery = beaconQuery.whereEqualTo(BeaconParseObject.UUID, beaconInfo.getUuid())
                .whereEqualTo(BeaconParseObject.MAJOR, beaconInfo.getMajor())
                .whereEqualTo(BeaconParseObject.MINOR, beaconInfo.getMinor());
        return beaconQuery;
    }

    @Override
    public void getTag(String smartPlaceInstanceId, BeaconInfo beaconInfo, final TagCallback callback) {
        SmartPlaceInstanceParseObject instanceParseObject =
                ParseObject.createWithoutData(SmartPlaceInstanceParseObject.class,
                        smartPlaceInstanceId);
        ParseQuery<BeaconParseObject> beaconQuery = getBeaconQuery(beaconInfo);
        ParseQuery<TagParseObject> tagQuery = getQuery(TagParseObject.class);
        tagQuery = tagQuery.whereMatchesQuery(TagParseObject.BEACON, beaconQuery);
        tagQuery = tagQuery.whereEqualTo(TagParseObject.SMARTPLACE_INSTANCE, instanceParseObject);
        tagQuery.getFirstInBackground(new TagGetCallback(callback));
    }

    @Override
    public void createTag(BeaconInfo beaconInfo, final String smartPlaceConfigurationId, final TagCallback callback) {
        ParseQuery<BeaconParseObject> beaconQuery = getBeaconQuery(beaconInfo);
        beaconQuery.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                SmartPlaceInstanceParseObject configurationParseObject = ParseObject.createWithoutData(
                        SmartPlaceInstanceParseObject.class, smartPlaceConfigurationId);
                final TagParseObject tag = ParseObject.create(TagParseObject.class);
                tag.setData(new JSONObject());
                tag.setBeacon(beaconParseObject);
                tag.setSmartPlaceInstance(configurationParseObject);
                tag.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callback.done(tag);
                    }
                });
            }
        });
    }

    @Override
    public void getSmartPlace(SmartPlaceInstanceObject smartPlaceInstanceObject, final SmartPlaceCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query =
                getQuery(SmartPlaceInstanceParseObject.class);
        query.include(SmartPlaceInstanceParseObject.SMART_PLACE);
        query.getInBackground(smartPlaceInstanceObject.getId(), new GetCallback<SmartPlaceInstanceParseObject>() {
            @Override
            public void done(SmartPlaceInstanceParseObject smartPlaceConfigurationParseObject, ParseException e) {
                ParseObject parseObject = smartPlaceConfigurationParseObject
                        .getParseObject(SmartPlaceInstanceParseObject.SMART_PLACE);
                SmartPlaceParseObject smartPlaceParseObject = ParseObject.createWithoutData(
                        SmartPlaceParseObject.class, parseObject.getObjectId());
                smartPlaceParseObject.updateFromParseObject(parseObject);
                callback.done(smartPlaceParseObject);
            }
        });
    }

    @Override
    public void getSmartPlaceConfiguration(String smartPlaceId, final SmartPlaceInstanceCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query = getQuery(SmartPlaceInstanceParseObject.class);
        ParseUser user = ParseUser.getCurrentUser();
        SmartPlaceParseObject smartPlaceParseObject = ParseObject.createWithoutData(
                SmartPlaceParseObject.class, smartPlaceId);
        query = query.whereEqualTo(SmartPlaceInstanceParseObject.OWNER, user);
        query = query.whereEqualTo(SmartPlaceInstanceParseObject.SMART_PLACE, smartPlaceParseObject);
        query.getFirstInBackground(new SmartPlaceInstanceGetCallback(callback));
    }


    @Override
    public void saveSmartPlaceInstance(final SmartPlaceInstanceObject object,
                                       final SmartPlaceInstanceCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query = getQuery(SmartPlaceInstanceParseObject.class);
        query.getInBackground(object.getId(), new GetCallback<SmartPlaceInstanceParseObject>() {
            @Override
            public void done(final SmartPlaceInstanceParseObject
                                     smartPlaceConfigurationParseObject, ParseException e) {
                smartPlaceConfigurationParseObject.update(object);
                smartPlaceConfigurationParseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callback.done(smartPlaceConfigurationParseObject);
                    }
                });
            }
        });
    }

    @Override
    public void saveTag(TagObject object, final JSONObject jsonObject, final TagCallback callback) {
        ParseQuery<TagParseObject> query = getQuery(TagParseObject.class);
        query.getInBackground(object.getId(), new GetCallback<TagParseObject>() {
            @Override
            public void done(final TagParseObject tagParseObject, ParseException e) {
                tagParseObject.setData(jsonObject);
                tagParseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callback.done(tagParseObject);
                    }
                });
            }
        });
    }

}
