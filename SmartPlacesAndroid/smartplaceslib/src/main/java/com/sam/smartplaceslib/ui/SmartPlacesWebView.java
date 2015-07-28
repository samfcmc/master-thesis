package com.sam.smartplaceslib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.web.OnPageLoadedCallback;
import com.sam.smartplaceslib.web.SmartPlacesWebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 *
 */
public class SmartPlacesWebView extends WebView {

    private static final String TAG_FOUND_FUNCTION = "tagFound";
    private static final String SMART_PLACES = "SmartPlaces";
    private static final String UUID = "uuid";
    private static final String MAJOR = "major";
    private static final String MINOR = "minor";
    private static final String BEACONS_SCANNED = "beaconsScanned";
    private static final String INIT = "init";
    private static final String DISTANCE = "distance";

    public SmartPlacesWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWebChromeClient(new WebChromeClient());
        getSettings().setJavaScriptEnabled(true);
    }

    public void setOnPageLoadedCallback(OnPageLoadedCallback callback) {
        setWebViewClient(new SmartPlacesWebViewClient(callback));
    }

    private void callJSFunction(String functionName, String arguments) {
        String url = String.format("javascript:%s(%s)", functionName, arguments);
        loadUrl(url);
    }

    private void callJSFunction(String functionName, JSONObject jsonObject) {
        callJSFunction(functionName, jsonObject.toString());
    }

    private void callSmartPlacesJSMethod(String methodName, String arguments) {
        String functionPath = String.format("%s['%s']", SMART_PLACES, methodName);
        callJSFunction(functionPath, arguments);
    }

    private void callSmartPlacesJSMethod(String methodName, JSONObject jsonObject) {
        callSmartPlacesJSMethod(methodName, jsonObject.toString());
    }

    private void callSmartPlacesJSMethod(String methodName, JSONArray jsonArray) {
        callSmartPlacesJSMethod(methodName, jsonArray.toString());
    }

    public void tagFound(TagObject tag) {
        callSmartPlacesJSMethod(TAG_FOUND_FUNCTION, tag.getData());
    }

    public void beaconsScanned(List<BeaconInfo> beaconInfoList) throws JSONException {
        JSONArray beaconsJsonArray = new JSONArray();
        for (BeaconInfo beaconInfo : beaconInfoList) {
            JSONObject beaconJsonObject = new JSONObject();
            beaconJsonObject.put(UUID, beaconInfo.getUuid());
            beaconJsonObject.put(MAJOR, beaconInfo.getMajor());
            beaconJsonObject.put(MINOR, beaconInfo.getMinor());
            beaconJsonObject.put(DISTANCE, beaconInfo.getDistance());
            beaconsJsonArray.put(beaconJsonObject);
        }
        callSmartPlacesJSMethod(BEACONS_SCANNED, beaconsJsonArray);
    }

    public void init(String instanceId) {
        String argument = String.format("\"%s\"", instanceId);
        callSmartPlacesJSMethod(INIT, argument);
    }
}
