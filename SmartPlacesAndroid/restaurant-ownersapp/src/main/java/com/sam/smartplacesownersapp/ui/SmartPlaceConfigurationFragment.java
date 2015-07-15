package com.sam.smartplacesownersapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.RestaurantOwnerApplication;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SmartPlaceConfigurationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SmartPlaceConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmartPlaceConfigurationFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Button saveButton;
    private EditText nameEditText;
    private EditText messageEditText;

    private RestaurantOwnerApplication application;

    private static final String SMART_PLACE = "smartPlace";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SmartPlaceConfigurationFragment.
     */
    public static SmartPlaceConfigurationFragment newInstance(String smartPlaceId) {
        SmartPlaceConfigurationFragment fragment = new SmartPlaceConfigurationFragment();
        Bundle args = new Bundle();
        args.putString(SMART_PLACE, smartPlaceId);
        fragment.setArguments(args);
        return fragment;
    }

    public SmartPlaceConfigurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        this.application = (RestaurantOwnerApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smart_place_configuration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.saveButton = (Button) view.findViewById(R.id.smart_place_configuration_save_button);
        this.nameEditText = (EditText) view.findViewById(R.id.smart_place_configuration_name_editText);
        this.messageEditText = (EditText) view.findViewById(R.id.smart_place_configuration_message_editText);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveConfiguration();
            }
        });
    }

    private void saveConfiguration() {
        String name = this.nameEditText.getText().toString();
        String message = this.messageEditText.getText().toString();
        String smartPlaceId = getArguments().getString(SMART_PLACE);
        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().createSmartPlaceInstance(smartPlaceId, name, message,
                new SmartPlaceInstanceCallback() {
                    @Override
                    public void done(SmartPlaceInstanceObject object) {
                        onSaveSmartPlaceConfiguration(object);
                    }
                });
    }

    private void onSaveSmartPlaceConfiguration(SmartPlaceInstanceObject object) {
        this.application.dismissProgressDialog(getActivity());
        mListener.onSmartPlaceConfigurationSaved(object);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
    public interface OnFragmentInteractionListener {
        void onSmartPlaceConfigurationSaved(SmartPlaceInstanceObject object);
    }

}
