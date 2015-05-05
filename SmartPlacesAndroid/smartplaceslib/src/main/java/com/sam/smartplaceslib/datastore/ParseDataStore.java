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
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.DummyParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStore implementation to work with Parse.com BaaS
 */
public class ParseDataStore extends AbstractDataStore<ParseUser, ParseException> {

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
        Parse.initialize(application, applicationId, clientKey);
        FacebookSdk.setApplicationId(facebookAppId);
        ParseFacebookUtils.initialize(application, REQUEST_LOGIN);
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
        ParseQuery<BeaconParseObject> query = BeaconParseObject.getQuery().whereEqualTo("uuid", uuid).whereEqualTo("major", major).whereEqualTo("minor", minor);
        query.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                callback.done(beaconParseObject);
            }
        });
    }

    @Override
    public void getSmartPlaces(String uuid, int major, int minor, final SmartPlacesCallback callback) {
        ParseQuery<BeaconParseObject> query = BeaconParseObject.getQuery().whereEqualTo("uuid", uuid).whereEqualTo("major", major).whereEqualTo("minor", minor);
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
    }

    @Override
    public void saveBeacon(final BeaconObject beaconObject, final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = BeaconParseObject.getQuery();

        query.getInBackground(beaconObject.getId(), new GetCallback<BeaconParseObject>() {
            @Override
            public void done(final BeaconParseObject beaconParseObject, ParseException e) {
                // Update object
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
    public ParseUser getCurrentUser() {
        return ParseUser.getCurrentUser();
    }

    @Override
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    @Override
    public void logout(final LogoutCallback<ParseException> callback) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(e);
            }
        });
    }


}
