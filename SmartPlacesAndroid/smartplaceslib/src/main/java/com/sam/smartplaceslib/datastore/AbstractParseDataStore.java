package com.sam.smartplaceslib.datastore;

import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.DeleteDataStoreCallback;
import com.sam.smartplaceslib.datastore.callback.parse.ParseDataStoreDeleteCallback;
import com.sam.smartplaceslib.datastore.callback.parse.ParseDataStoreFindCallback;
import com.sam.smartplaceslib.datastore.callback.parse.ParseDataStoreGetCallback;
import com.sam.smartplaceslib.datastore.callback.parse.ParseDataStoreSaveCallback;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.UserObject;
import com.sam.smartplaceslib.datastore.object.parse.AbstractParseObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.TagParseObject;
import com.sam.smartplaceslib.datastore.object.parse.UserParseObject;

/**
 *
 */
public abstract class AbstractParseDataStore implements DataStore {

    private LoginStrategy loginStrategy;

    public static int REQUEST_LOGIN = 2;

    public AbstractParseDataStore(Application application, DataStoreCredentials dataStoreCredentials) {
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

    protected ParseQuery<BeaconParseObject> getBeaconQuery(BeaconInfo beaconInfo) {
        ParseQuery<BeaconParseObject> beaconQuery = getQuery(BeaconParseObject.class);
        beaconQuery = beaconQuery.whereEqualTo(BeaconParseObject.UUID, beaconInfo.getUuid())
                .whereEqualTo(BeaconParseObject.MAJOR, beaconInfo.getMajor())
                .whereEqualTo(BeaconParseObject.MINOR, beaconInfo.getMinor());
        return beaconQuery;
    }

    protected <T extends AbstractParseObject> void save(T object, DataStoreCallback<? super T> callback) {
        object.saveInBackground(new ParseDataStoreSaveCallback<T>(callback, object));
    }

    protected <T extends AbstractParseObject> void delete(T object, DeleteDataStoreCallback callback) {
        object.deleteInBackground(new ParseDataStoreDeleteCallback(callback));
    }

    protected <T extends AbstractParseObject> void find(ParseQuery<T> query,
                                                        ParseDataStoreFindCallback<T, ? super T> findCallback) {
        query.findInBackground(findCallback);
    }

    protected <T extends AbstractParseObject> void getFirst(ParseQuery<T> query,
                                                            DataStoreCallback<? super T> callback) {
        query.getFirstInBackground(new ParseDataStoreGetCallback<T>(callback));
    }

    @Override
    public void login(LoginStrategy loginStrategy, LoginCallback callback) {
        this.loginStrategy = loginStrategy;
        this.loginStrategy.login(callback);
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

    @Override
    public void afterLoginOnActivityResult(int requestCode, int resultCode, Intent data) {
        this.loginStrategy.onActivityResult(requestCode, resultCode, data);
    }



}
