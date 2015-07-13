package com.sam.smartplacesownersapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sam.smartplaceslib.datastore.BeaconInfo;
import com.sam.smartplaceslib.datastore.callback.TagCallback;
import com.sam.smartplaceslib.datastore.object.TagObject;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.RestaurantOwnerApplication;

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
public class BeaconConfigFragment extends Fragment implements TagCallback {

    private OnBeaconConfigFragmentInteractionListener mListener;
    private static final String UUID = "uuid";
    private static final String MAJOR = "major";
    private static final String MINOR = "minor";
    private static final String CONFIGURATION = "configuration";

    private String configuration;
    private BeaconInfo beaconInfo;

    private RestaurantOwnerApplication application;

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
            String uuid = getArguments().getString(UUID);
            int major = getArguments().getInt(MAJOR);
            int minor = getArguments().getInt(MINOR);
            this.beaconInfo = new BeaconInfo(uuid, major, minor);
            this.configuration = getArguments().getString(CONFIGURATION);
        }
        this.application = (RestaurantOwnerApplication) getActivity().getApplication();
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
        this.application.getDataStore().getTag(this.beaconInfo, this);
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
    public void done(final TagObject object) {
        this.application.dismissProgressDialog(getActivity());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (object == null) {
                    createTag();
                } else {
                    refreshView(object);
                }
            }
        });
    }

    private void createTag() {
        this.application.getDataStore().createTag(this.beaconInfo, this.configuration, new TagCallback() {
            @Override
            public void done(TagObject object) {
                refreshView(object);
            }
        });
    }

    private void refreshView(final TagObject tag) {
        uuidTextView.setText(this.beaconInfo.getUuid());
        majorTextView.setText(this.beaconInfo.getMajor() + "");
        minorTextView.setText(this.beaconInfo.getMinor() + "");
        JSONObject json = tag.getData();
        if (json.has("table")) {
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
                    updateTag(tag);
                } catch (JSONException e) {
                    //TODO: Change this...
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateTag(TagObject object) throws JSONException {
        String tableText = this.tableEditText.getText().toString();
        int tableNumber = Integer.parseInt(tableText);
        JSONObject jsonObject = new JSONObject();
        String smartPlaceConfiguationId = getArguments().getString(CONFIGURATION);
        jsonObject.put("table", tableNumber);
        //object.setData(jsonObject);
        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().saveTag(object, jsonObject,
                new TagCallback() {
                    @Override
                    public void done(TagObject object) {
                        application.dismissProgressDialog(getActivity());
                        mListener.onSaveTag(object);
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
        void onSaveTag(TagObject object);
    }

}
