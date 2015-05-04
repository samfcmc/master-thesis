package com.sam.smartplaceslib.datastore.login;

/**
 *
 */
public interface LogoutCallback<ExceptionType> {
    public void done(ExceptionType exception);
}
