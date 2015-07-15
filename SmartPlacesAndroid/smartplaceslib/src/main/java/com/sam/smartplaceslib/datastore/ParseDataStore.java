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
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.login.LogoutCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.datastore.object.UserObject;
import com.sam.smartplaceslib.datastore.object.parse.BeaconParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceInstanceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.SmartPlaceParseObject;
import com.sam.smartplaceslib.datastore.object.parse.TagParseObject;
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
        ParseObject.registerSubclass(SmartPlaceInstanceParseObject.class);
        ParseObject.registerSubclass(SmartPlaceParseObject.class);
        ParseObject.registerSubclass(TagParseObject.class);
    }

    private <T extends ParseObject> ParseQuery<T> getQuery(Class<T> clazz) {
        return ParseQuery.getQuery(clazz);
    }

    @Override
    public void getBeacon(BeaconInfo beaconInfo, final BeaconCallback callback) {
        ParseQuery<BeaconParseObject> query = getBeaconQuery(beaconInfo);

        query.getFirstInBackground(new GetCallback<BeaconParseObject>() {
            @Override
            public void done(BeaconParseObject beaconParseObject, ParseException e) {
                callback.done(beaconParseObject);
            }
        });
    }

    @Override
    public void getSmartPlaces(final SmartPlacesCallback callback) {
        ParseQuery<SmartPlaceParseObject> query = getQuery(SmartPlaceParseObject.class);
        query.findInBackground(new FindCallback<SmartPlaceParseObject>() {
            @Override
            public void done(List<SmartPlaceParseObject> list, ParseException e) {
                List<SmartPlaceObject> resultList = new ArrayList<SmartPlaceObject>(list);
                callback.done(resultList);
            }
        });
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

    @Override
    public void getUserSmartPlaceInstances(final SmartPlaceInstancesCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query = getQuery(SmartPlaceInstanceParseObject.class);
        ParseUser user = ParseUser.getCurrentUser();
        query.whereEqualTo(SmartPlaceInstanceParseObject.OWNER, user);
        query.findInBackground(new FindCallback<SmartPlaceInstanceParseObject>() {
            @Override
            public void done(List<SmartPlaceInstanceParseObject> list, ParseException e) {
                List<SmartPlaceInstanceObject> resultList = new ArrayList<SmartPlaceInstanceObject>(list);
                callback.done(resultList);
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
    public void getTag(BeaconInfo beaconInfo, final TagCallback callback) {
        ParseQuery<BeaconParseObject> beaconQuery = getBeaconQuery(beaconInfo);
        ParseQuery<TagParseObject> tagQuery = getQuery(TagParseObject.class);
        tagQuery = tagQuery.whereMatchesQuery(TagParseObject.BEACON, beaconQuery);
        tagQuery.getFirstInBackground(new GetCallback<TagParseObject>() {
            @Override
            public void done(TagParseObject tagParseObject, ParseException e) {
                callback.done(tagParseObject);
            }
        });
    }

    @Override
    public void getTag(String smartPlaceId, BeaconInfo beaconInfo, final TagCallback callback) {
        SmartPlaceInstanceParseObject instanceParseObject =
                ParseObject.createWithoutData(SmartPlaceInstanceParseObject.class,
                        smartPlaceId);
        ParseQuery<BeaconParseObject> beaconQuery = getBeaconQuery(beaconInfo);
        ParseQuery<TagParseObject> tagQuery = getQuery(TagParseObject.class);
        tagQuery = tagQuery.whereMatchesQuery(TagParseObject.BEACON, beaconQuery);
        tagQuery = tagQuery.whereEqualTo(TagParseObject.SMARTPLACE_INSTANCE, instanceParseObject);
        tagQuery.getFirstInBackground(new GetCallback<TagParseObject>() {
            @Override
            public void done(TagParseObject tagParseObject, ParseException e) {
                callback.done(tagParseObject);
            }
        });
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
    public void getSmartPlaceConfiguration(String smartPlaceId, final SmartPlaceConfigurationCallback callback) {
        ParseQuery<SmartPlaceInstanceParseObject> query = getQuery(SmartPlaceInstanceParseObject.class);
        ParseUser user = ParseUser.getCurrentUser();
        SmartPlaceParseObject smartPlaceParseObject = ParseObject.createWithoutData(
                SmartPlaceParseObject.class, smartPlaceId);
        query = query.whereEqualTo(SmartPlaceInstanceParseObject.OWNER, user);
        query = query.whereEqualTo(SmartPlaceInstanceParseObject.SMART_PLACE, smartPlaceParseObject);
        query.getFirstInBackground(new GetCallback<SmartPlaceInstanceParseObject>() {
            @Override
            public void done(SmartPlaceInstanceParseObject smartPlaceConfigurationParseObject, ParseException e) {
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

        final SmartPlaceInstanceParseObject smartPlaceConfiguration =
                new SmartPlaceInstanceParseObject(user.getObjectId(), smartPlaceId, jsonObject);
        smartPlaceConfiguration.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(smartPlaceConfiguration);
            }
        });
    }

    @Override
    public void saveSmartPlaceConfiguration(final SmartPlaceInstanceObject object,
                                            final SmartPlaceConfigurationCallback callback) {
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
