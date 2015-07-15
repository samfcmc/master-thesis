package com.sam.ownersapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.callback.SmartPlacesCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;
import com.sam.smartplaceslib.ui.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SmartPlaceListFragment extends Fragment implements AbsListView.OnItemClickListener, SmartPlacesCallback {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    private List<SmartPlaceObject> smartPlaceObjects;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private SmartPlacesOwnersApplication application;

    public static SmartPlaceListFragment newInstance() {
        SmartPlaceListFragment fragment = new SmartPlaceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SmartPlaceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.smartPlaceObjects = new ArrayList<>();
        mAdapter = new SmartPlaceListAdapter(this.smartPlaceObjects);
        this.application = (SmartPlacesOwnersApplication) getActivity().getApplication();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get smart places from the backend
        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().getSmartPlaces(this);
    }

    private void refreshListView(List<SmartPlaceObject> list) {
        this.smartPlaceObjects = list;
        this.mAdapter = new SmartPlaceListAdapter(this.smartPlaceObjects);
        this.mListView.setAdapter(this.mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smartplacelist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            SmartPlaceObject item = this.smartPlaceObjects.get(position);
            mListener.onSmartPlaceSelected(item);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void done(List<SmartPlaceObject> list) {
        this.application.dismissProgressDialog(getActivity());
        if (list.isEmpty()) {
            String emptyText = getString(R.string.smart_places_list_empty);
            setEmptyText(emptyText);
        } else {
            refreshListView(list);
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
    public interface OnFragmentInteractionListener {
        void onSmartPlaceSelected(SmartPlaceObject smartPlaceObject);
    }

    private class SmartPlaceListAdapter extends ArrayAdapter<SmartPlaceObject> {


        public SmartPlaceListAdapter(List<SmartPlaceObject> list) {
            super(getActivity(), R.layout.smart_place_list_item_layout, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.smart_place_list_item_layout, parent, false);
                TextView nameTextView = (TextView) convertView
                        .findViewById(R.id.smart_place_list_item_name_textView);
                TextView shortDescriptionTextView = (TextView) convertView
                        .findViewById(R.id.smart_place_list_item_short_description_textView);
                SmartPlaceViewHolder viewHolder = new SmartPlaceViewHolder(nameTextView, shortDescriptionTextView);
                convertView.setTag(viewHolder);
            }
            SmartPlaceObject item = getItem(position);
            SmartPlaceViewHolder viewHolder = (SmartPlaceViewHolder) convertView.getTag();
            viewHolder.updateView(item);

            return convertView;
        }
    }

    private class SmartPlaceViewHolder implements ViewHolder<SmartPlaceObject> {
        public TextView nameTextView;
        public TextView shortDescriptionTextView;

        private static final int SHORT_DESCRIPTION_SIZE = 25;

        public SmartPlaceViewHolder(TextView nameTextView, TextView shortDescriptionTextView) {
            this.nameTextView = nameTextView;
            this.shortDescriptionTextView = shortDescriptionTextView;
        }

        @Override
        public void updateView(SmartPlaceObject object) {
            this.nameTextView.setText(object.getName());
            String shortDescription = String.format("%s ...",
                    object.getDescription().substring(0, SHORT_DESCRIPTION_SIZE));
            this.shortDescriptionTextView.setText(shortDescription);
        }
    }


}
