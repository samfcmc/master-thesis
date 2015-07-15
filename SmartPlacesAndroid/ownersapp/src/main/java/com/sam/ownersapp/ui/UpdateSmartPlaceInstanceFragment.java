package com.sam.ownersapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateSmartPlaceInstanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateSmartPlaceInstanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateSmartPlaceInstanceFragment extends Fragment implements SmartPlaceInstanceCallback {

    private OnFragmentInteractionListener mListener;

    private static final String SMART_PLACE = "smartPlace";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String EDIT = "edit";

    private String smartPlaceId;

    private EditText titleEditText;
    private EditText messageEditText;

    private SmartPlacesOwnersApplication application;

    private boolean editMode;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UpdateSmartPlaceInstanceFragment.
     */
    public static UpdateSmartPlaceInstanceFragment newInstance(String smartPlaceId) {
        UpdateSmartPlaceInstanceFragment fragment = new UpdateSmartPlaceInstanceFragment();
        Bundle args = new Bundle();
        args.putString(SMART_PLACE, smartPlaceId);
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdateSmartPlaceInstanceFragment newInstance(SmartPlaceInstanceObject smartPlaceInstanceObject) {
        UpdateSmartPlaceInstanceFragment fragment = new UpdateSmartPlaceInstanceFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, smartPlaceInstanceObject.getTitle());
        args.putString(MESSAGE, smartPlaceInstanceObject.getMessage());
        args.putString(ID, smartPlaceInstanceObject.getId());
        args.putBoolean(EDIT, true);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateSmartPlaceInstanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.editMode = getArguments().getBoolean(EDIT, false);
        }
        this.application = (SmartPlacesOwnersApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_smart_place_instance, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.titleEditText = (EditText) view.findViewById(R.id.update_smart_place_instance_title_editText);
        this.messageEditText = (EditText) view.findViewById(R.id.update_smart_place_instance_message_editText);

        if (this.editMode) {
            String title = getArguments().getString(TITLE);
            String message = getArguments().getString(MESSAGE);
            this.titleEditText.setText(title);
            this.messageEditText.setText(message);
        }

        Button saveButton = (Button) view.findViewById(R.id.update_smart_place_instance_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });
    }

    private void onSaveClicked() {
        String title = this.titleEditText.getText().toString();
        String message = this.messageEditText.getText().toString();
        if (this.editMode) {
            String id = getArguments().getString(ID);
            updateSmartPlaceInstance(id, title, message);
        } else {
            String smartPlaceId = getArguments().getString(SMART_PLACE);
            createSmartPlaceInstance(smartPlaceId, title, message);
        }

    }

    private void createSmartPlaceInstance(String smartPlaceId, String title, String message) {
        this.application.getDataStore().createSmartPlaceInstance(smartPlaceId, title, message, this);
    }

    private void updateSmartPlaceInstance(String id, String title, String message) {
        this.application.getDataStore().updateSmartPlaceInstance(id, title, message, this);
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

    @Override
    public void done(SmartPlaceInstanceObject object) {
        mListener.smartPlaceInstanceSaved(object);
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
        void smartPlaceInstanceSaved(SmartPlaceInstanceObject object);
    }

}
