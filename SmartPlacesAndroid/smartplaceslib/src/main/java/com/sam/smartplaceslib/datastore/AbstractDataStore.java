package com.sam.smartplaceslib.datastore;

import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.parse.ParseDataStoreSaveCallback;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.TagParseObject;

/**
 *
 */
public abstract class AbstractDataStore implements DataStore {

    private LoginStrategy loginStrategy;

    public static int REQUEST_LOGIN = 2;

    public AbstractDataStore(Application application, DataStoreCredentials dataStoreCredentials) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(application);
        registerSubclasses();
        Parse.initialize(application, dataStoreCredentials.getClientId(), dataStoreCredentials.getClientKey());
        FacebookSdk.setApplicationId(dataStoreCredentials.getFacebookAppId());
        ParseFacebookUtils.initialize(application, REQUEST_LOGIN);
    }

    private void registerSubclasses() {
        ParseObject.registerSubclass(BeaconParseObject.class);
        ParseObject.registerSubclass(SmartPlaceInstanceParseObject.class);
        ParseObject.registerSubclass(SmartPlaceParseObject.class);
        ParseObject.registerSubclass(TagParseObject.class);
    }

    protected <T extends ParseObject> ParseQuery<T> getQuery(Class<T> clazz) {
        return ParseQuery.getQuery(clazz);
    }

    protected <T extends AbstractParseObject> void save(T object, DataStoreCallback<? super T> callback) {
        object.saveInBackground(new ParseDataStoreSaveCallback<T>(callback, object));
    }

    @Override
    public void login(LoginStrategy loginStrategy, LoginCallback callback) {
        this.loginStrategy = loginStrategy;
        this.loginStrategy.login(callback);
    }

    @Override
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data) {
        this.loginStrategy.onActivityResult(requestCode, resultCode, data);
    }

}
