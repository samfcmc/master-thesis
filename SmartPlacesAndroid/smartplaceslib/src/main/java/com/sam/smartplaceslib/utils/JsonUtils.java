package com.sam.smartplaceslib.utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * JsonUtils: Contains methods to handle json methods such as read json from raw resource file
 */
public class JsonUtils {

    public static JsonObject readJsonFromRawResource(Application application, int rawResourceId) {
        JsonObject jsonObject = new JsonObject();
        InputStream inputStream = application.getResources().openRawResource(rawResourceId);
        int size = 0;
        try {
            size = inputStream.available();

            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            inputStream.close();

            String json = new String(buffer, "UTF-8");
            jsonObject = new Gson().fromJson(json, JsonObject.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return jsonObject;
        }

    }
}
