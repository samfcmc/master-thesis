package com.sam.smartplaceslib.datastore.object.parse;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.object.UserObject;

/**
 *
 */
public class UserParseObject implements UserObject {

    private ParseUser user;

    public UserParseObject(ParseUser user) {
        this.user = user;
    }

    public UserParseObject(ParseObject object) {
        ParseUser user = ParseUser.createWithoutData(ParseUser.class, object.getObjectId());
        //user.setUsername(object.getString(object.getString(USERNAME)));
        this.user = user;
    }

    @Override
    public String getId() {
        return this.user.getObjectId();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }
}
