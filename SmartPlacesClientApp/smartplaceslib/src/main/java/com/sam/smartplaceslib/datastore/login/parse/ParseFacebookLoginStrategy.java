package com.sam.smartplaceslib.datastore.login.parse;

import android.app.Activity;
import android.content.Intent;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;

import java.util.ArrayList;

/**
 *
 */
public class ParseFacebookLoginStrategy implements LoginStrategy<ParseUser, ParseException> {
    private Activity activity;

    public ParseFacebookLoginStrategy(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void login(final LoginCallback<ParseUser, ParseException> callback) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this.activity, new ArrayList<String>(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                callback.done(parseUser, e);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

}
