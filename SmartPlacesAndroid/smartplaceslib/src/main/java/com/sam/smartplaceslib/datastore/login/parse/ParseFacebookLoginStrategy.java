package com.sam.smartplaceslib.datastore.login.parse;

import android.app.Activity;
import android.content.Intent;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.object.parse.UserParseObject;

import java.util.ArrayList;

/**
 *
 */
public class ParseFacebookLoginStrategy implements LoginStrategy {
    private Activity activity;

    public ParseFacebookLoginStrategy(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void login(final LoginCallback callback) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this.activity, new ArrayList<String>(), new LogInCallback() {

            @Override
            public void done(ParseUser parseUser, ParseException e) {
                UserParseObject userParseObject = new UserParseObject(parseUser);
                DataStoreException exception = new DataStoreException(e);
                callback.done(userParseObject, exception);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

}
