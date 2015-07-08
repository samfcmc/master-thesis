package com.sam.smartplaceslib.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 *
 */
public class SmartPlacesWebViewClient extends WebViewClient {

    private OnPageLoadedCallback callback;

    public SmartPlacesWebViewClient(OnPageLoadedCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        this.callback.done();
    }
}
