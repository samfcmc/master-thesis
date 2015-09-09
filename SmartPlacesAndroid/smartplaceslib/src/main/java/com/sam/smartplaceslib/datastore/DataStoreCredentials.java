package com.sam.smartplaceslib.datastore;

import android.app.Application;

import com.google.gson.JsonObject;
import com.sam.smartplaceslib.utils.JsonUtils;

import java.io.IOException;

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
        JsonObject jsonObject = JsonUtils.readJsonFromRawResource(application, jsonRawResId);
        String appId = jsonObject.get("appId").getAsString();
        String clientKey = jsonObject.get("clientKey").getAsString();
        String facebookAppId = jsonObject.get("facebookAppId").getAsString();

        return new DataStoreCredentials(appId, clientKey, facebookAppId);
    }
}

