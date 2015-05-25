package com.sam.smartplacesownersapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sam.smartplacesownersapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigMenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Button menuButton;
    private Button tablesButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConfigMenuFragment.
     */
    public static ConfigMenuFragment newInstance() {
        ConfigMenuFragment fragment = new ConfigMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ConfigMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.menuButton = (Button) view.findViewById(R.id.config_menu_button);
        this.tablesButton = (Button) view.findViewById(R.id.config_tables_button);

        this.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButtonClicked();
            }
        });

        this.tablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tablesButtonClicked();
            }
        });
    }

    private void tablesButtonClicked() {
        mListener.onTablesButtonClick();
    }

    private void menuButtonClicked() {
        mListener.onMenuButtonClick();
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
        void onMenuButtonClick();
        void onTablesButtonClick();
    }

}
