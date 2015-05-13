package com.sam.smartplacesownersapp.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private static final String CONFIGURATION = "configuration";

    private String uuid;
    private int major;
    private int minor;

    private SmartPlacesOwnerApplication application;

    private TextView uuidTextView;
    private TextView majorTextView;
    private TextView minorTextView;
    private EditText tableEditText;
    private Button saveButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BeaconConfigFragment.
     */
    public static BeaconConfigFragment newInstance(String uuid, int major, int minor,
                                                   String smartPlaceConfigurationId) {
        BeaconConfigFragment fragment = new BeaconConfigFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UUID, uuid);
        bundle.putInt(MAJOR, major);
        bundle.putInt(MINOR, minor);
        bundle.putString(CONFIGURATION, smartPlaceConfigurationId);
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
        this.uuidTextView = (TextView) view.findViewById(R.id.beacon_config_uuid_textView);
        this.majorTextView = (TextView) view.findViewById(R.id.beacon_config_major_textView);
        this.minorTextView = (TextView) view.findViewById(R.id.beacon_config_minor_textView);
        this.tableEditText = (EditText) view.findViewById(R.id.beacon_config_table_editText);
        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().getBeacon(this.uuid, this.major, this.minor, this);
        this.saveButton = (Button) view.findViewById(R.id.beacon_config_save_button);
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
        this.application.dismissProgressDialog(getActivity());
        if(object != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshView(object);
                }
            });
        }
    }

    private void refreshView(final BeaconObject object) {
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
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveBeacon(object);
                } catch (JSONException e) {
                    //TODO: Change this...
                    e.printStackTrace();
                }
            }
        });

    }

    private void saveBeacon(BeaconObject object) throws JSONException {
        String tableText = this.tableEditText.getText().toString();
        int tableNumber = Integer.parseInt(tableText);
        JSONObject jsonObject = new JSONObject();
        String smartPlaceConfiguationId = getArguments().getString(CONFIGURATION);
        jsonObject.put("table", tableNumber);
        object.setObject(jsonObject);
        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().saveBeacon(object, smartPlaceConfiguationId,
                new BeaconCallback() {
            @Override
            public void done(BeaconObject object) {
                application.dismissProgressDialog(getActivity());
                mListener.onSaveBeacon(object);
            }
        });
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
        void onSaveBeacon(BeaconObject object);
    }

}
