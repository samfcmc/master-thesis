package com.sam.smartplaceslib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.web.OnPageLoadedCallback;
import com.sam.smartplaceslib.web.SmartPlacesWebViewClient;

import org.json.JSONObject;

/**
 *
 */
public class SmartPlacesWebView extends WebView {

    private static final String TAG_FOUND_FUNCTION = "tagFound";
    private static final String SMART_PLACES = "SmartPlaces";

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

    public void tagFound(TagObject tag) {
        callSmartPlacesJSMethod(TAG_FOUND_FUNCTION, tag.getData());
    }

}
