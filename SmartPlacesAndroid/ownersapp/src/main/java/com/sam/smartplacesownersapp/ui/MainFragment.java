package com.sam.smartplacesownersapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.LoginStrategy;
import com.sam.smartplaceslib.datastore.login.parse.ParseFacebookLoginStrategy;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.SmartPlacesOwnerApplication;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private LoginButton loginButton;
    private SmartPlacesOwnerApplication application;
    private String test;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loginButton = (LoginButton) view.findViewById(R.id.main_login_button);
        this.application = (SmartPlacesOwnerApplication) getActivity().getApplication();

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              loginWithFacebook();
            }
        });
    }

    private void loginWithFacebook() {
        login(new ParseFacebookLoginStrategy(getActivity()));
    }

    private void login(LoginStrategy loginStrategy) {
        this.application.getDataStore().login(loginStrategy, new LoginCallback<ParseUser, ParseException>() {
            @Override
            public void done(ParseUser user, ParseException exception) {
                afterLogin(user);
            }
        });
    }

    private void afterLogin(ParseUser user) {
        logToDisplay("User logged in " + user.getUsername());
        MainActivity activity = (MainActivity) getActivity();
        activity.afterLogin();
    }

    private void logToDisplay(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
