package com.sam.ownersapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.widget.LoginButton;
import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.DataStoreException;
import com.sam.smartplaceslib.datastore.login.LoginCallback;
import com.sam.smartplaceslib.datastore.login.parse.ParseFacebookLoginStrategy;
import com.sam.smartplaceslib.datastore.object.UserObject;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private LoginButton loginButton;
    private SmartPlacesOwnersApplication application;
    private OnFragmentInteractionListener listener;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity " + activity.getClass().getName() +
                    " does not implement " +
                    OnFragmentInteractionListener.class.getName());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.application = (SmartPlacesOwnersApplication) getActivity().getApplication();
        initUI(view);
    }

    private void initUI(View view) {
        this.loginButton = (LoginButton) view.findViewById(R.id.main_login_button);
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

    private void login(ParseFacebookLoginStrategy parseFacebookLoginStrategy) {
        this.application.getDataStore().login(parseFacebookLoginStrategy, new LoginCallback() {
            @Override
            public void done(UserObject user, DataStoreException exception) {
                afterLogin();
            }
        });
    }

    private void afterLogin() {
        this.listener.afterLogin();
    }

    public interface OnFragmentInteractionListener {
        void afterLogin();
    }
}
