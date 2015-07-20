package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class DataStoreCredentials {
    private String clientId;
    private String clientKey;
    private String facebookAppId;

    public DataStoreCredentials(String clientId, String clientKey, String facebookAppId) {
        this.clientId = clientId;
        this.clientKey = clientKey;
        this.facebookAppId = facebookAppId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public static DataStoreCredentials fromJsonRawResource(Application application, int jsonRawResId) throws IOException {
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

        return new DataStoreCredentials(appId, clientKey, facebookAppId);
    }
}

