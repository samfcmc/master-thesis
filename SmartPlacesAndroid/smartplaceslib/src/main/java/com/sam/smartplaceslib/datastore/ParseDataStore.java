package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.DummyCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceConfigurationObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.datastore.object.UserObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.DummyParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceConfigurationParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.UserParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStore implementation to work with Parse.com BaaS
 */
public class ParseDataStore extends AbstractDataStore {

    public static int REQUEST_LOGIN = 2;

    public static ParseDataStore fromRawJsonResource(Application application, int jsonRawResId) throws IOException {
        InputStream inputStream = application.getResources().openRawResource(jsonRawResId);
        int size = inputStream.available();

        byte[] buffer = new byte[size];

        inputStream.read(buffer);

        inputStream.close();

        String json = new String(buffer, "UTF-8");
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String appId = jsonObject.get("appId").getAsString();
        String clientKey = jsonObject.get("clientKey").getAsString();
        String facebookAppId = jsonObject.get("facebookAppId").getAsString();
        return new ParseDataStore(application, appId, clientKey, facebookAppId);
    }

    public ParseDataStore(Application application, String applicationId, String clientKey, String facebookAppId) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(application);
        registerSubclasses();
        Parse.initialize(application, applicationId, clientKey);
        FacebookSdk.setApplicationId(facebookAppId);
        ParseFacebookUtils.initialize(application, REQUEST_LOGIN);
    }

    private void registerSubclasses() {
        ParseObject.registerSubclass(BeaconParseObject.class);
        ParseObject.registerSubclass(SmartPlaceConfigurationParseObject.class);
        ParseObject.registerSubclass(SmartPlaceParseObject.class);
    }

    private <T extends ParseObject> ParseQuery<T> getQuery(Class<T> clazz) {
        return ParseQuery.getQuery(clazz);
    }

    @Override
    public void createDummy(final String name, final DummyCallback callback) {
        final DummyParseObject object = new DummyParseObject(name);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(object);
            }
        });
    }

    @Override
    public void getBeacon(final String uuid, final int major, final int minor, final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = getQuery(BeaconParseObject.class).whereEqualTo("uuid", uuid).whereEqualTo("major", major).whereEqualTo("minor", minor);
        query.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                callback.done(beaconParseObject);
            }
        });
    }

    /*@Override
    public void getSmartPlaces(String uuid, int major, int minor, final SmartPlacesCallback callback) {
        ParseQuery<BeaconParseObject> query = getQuery(BeaconParseObject.class).whereEqualTo("uuid", uuid).whereEqualTo("major", major).whereEqualTo("minor", minor);
        query.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                ParseRelation<ParseObject> relation = beaconParseObject.getSmartPlacesRelation();
                ParseQuery<ParseObject> query = relation.getQuery();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        List<SmartPlaceObject> smartPlaces = new ArrayList<SmartPlaceObject>(objects.size());
                        for(ParseObject parseObject: objects) {
                            SmartPlaceObject newObject = new SmartPlaceParseObject(parseObject);
                            smartPlaces.add(newObject);
                        }
                        callback.done(smartPlaces);
                    }
                });
            }
        });
    }*/

    @Override
    public void getSmartPlaces(BeaconObject beaconObject, final SmartPlacesCallback callback) {
        BeaconParseObject beaconParseObject = ParseObject.createWithoutData(BeaconParseObject.class,
                beaconObject.getId());
        ParseRelation<SmartPlaceParseObject> relation = beaconParseObject.getSmartPlacesRelation();
        relation.getQuery().findInBackground(new FindCallback<SmartPlaceParseObject>() {
            @Override
            public void done(List<SmartPlaceParseObject> list, ParseException e) {
                List<SmartPlaceObject> smartPlaces = new ArrayList<SmartPlaceObject>(list);
                callback.done(smartPlaces);
            }
        });
    }

    @Override
    public void saveBeacon(final BeaconObject beaconObject, final String smartPlaceConfigurationId,
                           final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = getQuery(BeaconParseObject.class);

        query.getInBackground(beaconObject.getId(), new GetCallback<BeaconParseObject>() {
            @Override
            public void done(final BeaconParseObject beaconParseObject, ParseException e) {
                // Update object
                SmartPlaceConfigurationParseObject smartPlaceConfigurationParseObject =
                        ParseObject.createWithoutData(SmartPlaceConfigurationParseObject.class,
                                smartPlaceConfigurationId);
                beaconParseObject.getSmartPlacesConfigurationRelation().
                        add(smartPlaceConfigurationParseObject);
                beaconParseObject.setObject(beaconObject.getObject());
                beaconParseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callback.done(beaconParseObject);
                    }
                });
            }
        });
    }

    @Override
    public void getSmartPlaceConfiguration(String smartPlaceId, final SmartPlaceConfigurationCallback callback) {
        ParseQuery<SmartPlaceConfigurationParseObject> query = getQuery(SmartPlaceConfigurationParseObject.class);
        ParseUser user = ParseUser.getCurrentUser();
        SmartPlaceParseObject smartPlaceParseObject = ParseObject.createWithoutData(
                SmartPlaceParseObject.class, smartPlaceId);
        query = query.whereEqualTo("owner", user);
        query = query.whereEqualTo("smartPlace", smartPlaceParseObject);
        query.getFirstInBackground(new GetCallback<SmartPlaceConfigurationParseObject>() {
            @Override
            public void done(SmartPlaceConfigurationParseObject smartPlaceConfigurationParseObject, ParseException e) {
                callback.done(smartPlaceConfigurationParseObject);
            }
        });
    }

    @Override
    public void createSmartPlaceConfiguration(String smartPlaceId, String name, String message,
                                              final SmartPlaceConfigurationCallback callback) {
        ParseUser user = ParseUser.getCurrentUser();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("message", message);
        } catch (JSONException e) {
            // Now we just do not care
        }

        final SmartPlaceConfigurationParseObject smartPlaceConfiguration =
                new SmartPlaceConfigurationParseObject(user.getObjectId(), smartPlaceId, jsonObject);
        smartPlaceConfiguration.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(smartPlaceConfiguration);
            }
        });
    }

    @Override
    public void saveSmartPlaceConfiguration(final SmartPlaceConfigurationObject object,
                                            final SmartPlaceConfigurationCallback callback) {
        ParseQuery<SmartPlaceConfigurationParseObject> query = getQuery(SmartPlaceConfigurationParseObject.class);
        query.getInBackground(object.getId(), new GetCallback<SmartPlaceConfigurationParseObject>() {
            @Override
            public void done(final SmartPlaceConfigurationParseObject
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
    public UserObject getCurrentUser() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            return null;
        } else {
            return new UserParseObject(user);
        }

    }

    @Override
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    @Override
    public void logout(final LogoutCallback callback) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                DataStoreException exception = new DataStoreException(e);
                callback.done(exception);
            }
        });
    }


}
