package com.sam.smartplacesownerapp.datastore.login;

/**
 *
 */
public interface LogoutCallback<ExceptionType> {
    public void done(ExceptionType exception);
}
