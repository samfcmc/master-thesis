package com.sam.smartplaceslib.datastore.login;

/**
 *
 */
public interface LoginCallback<UserType, ExceptionType> {
    public void done(UserType user, ExceptionType exception);
}
