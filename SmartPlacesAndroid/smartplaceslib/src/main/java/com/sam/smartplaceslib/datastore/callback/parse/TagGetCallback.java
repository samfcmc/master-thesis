package com.sam.smartplaceslib.datastore.callback.parse;

import com.sam.smartplaceslib.datastore.callback.DataStoreCallback;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplaceslib.datastore.object.parse.TagParseObject;

/**
 *
 */
public class TagGetCallback extends ParseDataStoreGetCallback<TagParseObject> {

    public TagGetCallback(DataStoreCallback<TagObject> callback) {
        super(callback);
    }

}
