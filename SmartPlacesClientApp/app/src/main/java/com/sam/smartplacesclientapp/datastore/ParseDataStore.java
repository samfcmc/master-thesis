package com.sam.smartplacesclientapp.datastore;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sam.smartplacesclientapp.R;
import com.sam.smartplacesclientapp.datastore.callback.DataStoreCallback;
import com.sam.smartplacesclientapp.datastore.callback.DummyCallback;
import com.sam.smartplacesclientapp.datastore.object.DataStoreObject;
import com.sam.smartplacesclientapp.datastore.object.DummyObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * DataStore implementation to work Parse.com BaaS
 */
public class ParseDataStore implements DataStore {

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
        return new ParseDataStore(application, appId, clientKey);
    }

    public ParseDataStore(Application application, String applicationId, String clientKey) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(application);
        Parse.initialize(application, applicationId, clientKey);
    }

    private <T extends ParseObject> void save(final T object, final DataStoreCallback<T> callback) {
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(object);
            }
        });
    }

    @Override
    public void createDummy(String name, DummyCallback callback) {
        final DummyObject object = new DummyObject(name);
        save(object, callback);

    }
}
