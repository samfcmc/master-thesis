package com.sam.ownersapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sam.ownersapp.R;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowSmartPlaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowSmartPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSmartPlaceFragment extends Fragment {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private String id;
    private String name;
    private String description;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param smartPlaceObject SmartPlaceObject instance.
     * @return A new instance of fragment ShowSmartPlaceFragment.
     */
    public static ShowSmartPlaceFragment newInstance(SmartPlaceObject smartPlaceObject) {
        ShowSmartPlaceFragment fragment = new ShowSmartPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ID, smartPlaceObject.getId());
        args.putString(NAME, smartPlaceObject.getName());
        args.putString(DESCRIPTION, smartPlaceObject.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    public ShowSmartPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getString(ID);
            this.name = getArguments().getString(NAME);
            this.description = getArguments().getString(DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_smart_place, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nameTextView = (TextView) view.findViewById(R.id.show_smart_place_name_textView);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.show_smart_place_description_textView);
        Button createInstanceButton = (Button) view.findViewById(R.id.show_smart_place_create_instance_button);

        nameTextView.setText(this.name);
        descriptionTextView.setText(this.description);
        createInstanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInstance();
            }
        });
    }

    private void createInstance() {
        mListener.onCreateInstanceClick(this.id);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onCreateInstanceClick(String id);
    }

}
