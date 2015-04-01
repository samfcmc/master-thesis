package com.sam.smartplacesclientapp.datastore.login;

/**
 *
 */
public interface LogoutCallback<ExceptionType> {
    public void done(ExceptionType exception);
}
