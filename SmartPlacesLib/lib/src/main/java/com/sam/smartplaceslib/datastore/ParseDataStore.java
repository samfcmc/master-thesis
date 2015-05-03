package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;

import java.io.IOException;
import java.io.InputStream;

/**
 * DataStore implementation to work with Parse.com BaaS
 */
public class ParseDataStore extends AbstractDataStore<ParseUser, ParseException> {

    public static final int REQUEST_LOGIN = 2;

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
