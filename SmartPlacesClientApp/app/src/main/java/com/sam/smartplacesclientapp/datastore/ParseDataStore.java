package com.sam.smartplacesclientapp.datastore;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.SmartPlacesApplication;
import com.sam.smartplacesclientapp.datastore.callback.BeaconCallback;
import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;
import com.sam.smartplacesclientapp.datastore.login.LoginStrategy;
import com.sam.smartplacesclientapp.datastore.object.parse.BeaconParseObject;
import com.sam.smartplacesclientapp.datastore.object.parse.DummyParseObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * DataStore implementation to work Parse.com BaaS
 */
public class ParseDataStore extends AbstractDataStore {

    public static ParseDataStore fromRawJsonResource(Application application, int jsonRawResId) throws IOException {
        InputStream inputStream = application.getResources().openRawResource(R.raw.parse);
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
        ParseFacebookUtils.initialize(application, SmartPlacesApplication.REQUEST_LOGIN);
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
    public void getBeaon(final String uuid, final int major, final int minor, final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = BeaconParseObject.getQuery().whereEqualTo("uuid", uuid).whereEqualTo("major", major).whereEqualTo("minor", minor);
        query.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                callback.done(beaconParseObject);
            }
        });
    }


}
