package com.sam.smartplacesownersapp.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sam.smartplaceslib.datastore.callback.BeaconCallback;
import com.sam.smartplaceslib.datastore.object.BeaconObject;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.SmartPlacesOwnerApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeaconConfigFragment.OnBeaconConfigFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeaconConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeaconConfigFragment extends Fragment implements BeaconCallback{

    private OnBeaconConfigFragmentInteractionListener mListener;
    private static final String UUID = "uuid";
    private static final String MAJOR = "major";
    private static final String MINOR= "minor";

    private String uuid;
    private int major;
    private int minor;

    private SmartPlacesOwnerApplication application;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BeaconConfigFragment.
     */
    public static BeaconConfigFragment newInstance(String uuid, int major, int minor) {
        BeaconConfigFragment fragment = new BeaconConfigFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UUID, uuid);
        bundle.putInt(MAJOR, major);
        bundle.putInt(MINOR, minor);
        fragment.setArguments(bundle);
        return fragment;
    }

    public BeaconConfigFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.uuid = getArguments().getString(UUID);
            this.major = getArguments().getInt(MAJOR);
            this.minor = getArguments().getInt(MINOR);
        }
        this.application = (SmartPlacesOwnerApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beacon_config, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.application.getDataStore().getBeacon(this.uuid, this.major, this.minor, this);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBeaconConfigFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void done(final BeaconObject object) {
        if(object != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshView(object);
                }
            });
        }
    }

    private void refreshView(BeaconObject object) {
        TextView uuidTextView = (TextView) getView().findViewById(R.id.beacon_config_uuid_textView);
        TextView majorTextView = (TextView) getView().findViewById(R.id.beacon_config_major_textView);
        TextView minorTextView = (TextView) getView().findViewById(R.id.beacon_config_minor_textView);
        EditText tableEditText = (EditText) getView().findViewById(R.id.beacon_config_table_editText);
        uuidTextView.setText(object.getUUID());
        majorTextView.setText(object.getMajor() + "");
        minorTextView.setText(object.getMinor() + "");
        JSONObject json = object.getObject();
        if(json.has("table")){
            try {
                String tableNumberText = json.getInt("table") + "";
                tableEditText.setText(tableNumberText);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBeaconConfigFragmentInteractionListener {
    }

}
