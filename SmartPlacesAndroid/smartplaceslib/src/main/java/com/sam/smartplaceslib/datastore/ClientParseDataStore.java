package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.TagParseObject;
import com.sam.smartplaceslib.statistics.Statistics;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataStore implementation to work with Parse.com BaaS
 */
public class ClientParseDataStore extends AbstractParseDataStore implements ClientDataStore {


    public static ClientDataStore fromRawJsonResource(Application application,
                                                      int jsonRawResourceId,
                                                      Statistics statistics) throws IOException {
        DataStoreCredentials dataStoreCredentials = DataStoreCredentials
                .fromJsonRawResource(application, jsonRawResourceId);
        return new ClientParseDataStore(application, dataStoreCredentials, statistics);
    }

    public ClientParseDataStore(Application application, DataStoreCredentials dataStoreCredentials,
                                Statistics statistics) {
        super(application, dataStoreCredentials, statistics);
    }

    @Override
    public void getBeacon(BeaconInfo beaconInfo, final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = getBeaconQuery(beaconInfo);
        getFirst(query, callback, getStatistics(), "Get beacon");
    }

    @Override
    public void getSmartPlaceInstances(BeaconObject beaconObject,
                                       final SmartPlaceInstancesCallback callback) {
        ParseQuery<TagParseObject> tagQuery = getQuery(TagParseObject.class);
        BeaconParseObject beaconParseObject = ParseObject.createWithoutData(BeaconParseObject.class, beaconObject.getId());
        tagQuery = tagQuery.whereEqualTo(TagParseObject.BEACON, beaconParseObject);
        tagQuery = include(tagQuery, TagParseObject.SMARTPLACE_INSTANCE);
        tagQuery = include(tagQuery, TagParseObject.SMARTPLACE_INSTANCE, SmartPlaceInstanceParseObject.SMART_PLACE);
        final long start = System.currentTimeMillis();
        tagQuery.findInBackground(new FindCallback<TagParseObject>() {
            @Override
            public void done(List<TagParseObject> list, ParseException e) {
                measureLatency("Get Smart Place Instances", start);
                Map<String, SmartPlaceInstanceObject> result = new HashMap<String, SmartPlaceInstanceObject>();
                for (TagParseObject tag : list) {
                    ParseObject parseObject = tag.getParseObject(TagParseObject.SMARTPLACE_INSTANCE);
                    SmartPlaceInstanceParseObject instance = ParseObject.createWithoutData(SmartPlaceInstanceParseObject.class, parseObject.getObjectId());
                    SmartPlaceInstanceObject found = result.get(instance.getId());
                    if (found == null) {
                        instance.updateFromParseObject(parseObject);
                        result.put(instance.getId(), instance);
                    }

                }
                callback.done(new ArrayList<SmartPlaceInstanceObject>(result.values()));
            }
        });
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
        tagQuery = tagQuery.include(TagParseObject.BEACON);
        getFirst(tagQuery, callback, getStatistics(), "Get tag");
    }

    @Override
    public void createTag(BeaconInfo beaconInfo, final String smartPlaceConfigurationId, final TagCallback callback) {
        ParseQuery<BeaconParseObject> beaconQuery = getBeaconQuery(beaconInfo);
        beaconQuery.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                SmartPlaceInstanceParseObject instanceParseObject = ParseObject.createWithoutData(
                        SmartPlaceInstanceParseObject.class, smartPlaceConfigurationId);
                final TagParseObject tag = ParseObject.create(TagParseObject.class);
                tag.setData(new JSONObject());
                tag.setBeacon(beaconParseObject);
                tag.setSmartPlaceInstance(instanceParseObject);
                save(tag, callback, getStatistics(), "Create tag");
            }
        });
    }

    @Override
    public void getSmartPlace(SmartPlaceInstanceObject smartPlaceInstanceObject, final SmartPlaceCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query =
                getQuery(SmartPlaceInstanceParseObject.class);
        query.include(SmartPlaceInstanceParseObject.SMART_PLACE);
        final long start = System.currentTimeMillis();
        query.getInBackground(smartPlaceInstanceObject.getId(), new GetCallback<SmartPlaceInstanceParseObject>() {
            @Override
            public void done(SmartPlaceInstanceParseObject smartPlaceConfigurationParseObject, ParseException e) {
                measureLatency("Get smart place", start);
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
    public void saveSmartPlaceInstance(final SmartPlaceInstanceObject object,
                                       final SmartPlaceInstanceCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query = getQuery(SmartPlaceInstanceParseObject.class);
        query.getInBackground(object.getId(), new GetCallback<SmartPlaceInstanceParseObject>() {
            @Override
            public void done(final SmartPlaceInstanceParseObject
                                     smartPlaceConfigurationParseObject, ParseException e) {
                smartPlaceConfigurationParseObject.update(object);
                save(smartPlaceConfigurationParseObject, callback, getStatistics(),
                        "Save smart place instance");
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
                save(tagParseObject, callback, getStatistics(), "Save tag");
            }
        });
    }

}
