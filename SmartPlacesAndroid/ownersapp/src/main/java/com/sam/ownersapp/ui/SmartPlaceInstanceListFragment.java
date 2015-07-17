package com.sam.ownersapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.sam.smartplaceslib.datastore.callback.DeleteCallback;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstancesCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
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
public class SmartPlaceInstanceListFragment extends Fragment implements AbsListView.OnItemClickListener,
        SmartPlaceInstancesCallback, DeleteCallback {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private SmartPlaceInstanceListAdapter mAdapter;

    private SmartPlacesOwnersApplication application;

    private static final int REQUEST_SHOW_INSTANCE = 3;

    public static SmartPlaceInstanceListFragment newInstance() {
        SmartPlaceInstanceListFragment fragment = new SmartPlaceInstanceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SmartPlaceInstanceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SmartPlaceInstanceListAdapter(new ArrayList<SmartPlaceInstanceObject>());
        this.application = (SmartPlacesOwnersApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smartplaceinstancelist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Empty text view
        TextView emptyTextView = (TextView) view.findViewById(android.R.id.empty);
        mListView.setEmptyView(emptyTextView);

        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().getUserSmartPlaceInstances(this);
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
        SmartPlaceInstanceObject object = mAdapter.getItem(position);
        showSmartPlaceInstance(object);
    }

    private void showSmartPlaceInstance(SmartPlaceInstanceObject object) {
        Intent intent = ShowSmartPlaceInstanceActivity.getIntent(getActivity(), object);
        startActivityForResult(intent, REQUEST_SHOW_INSTANCE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SHOW_INSTANCE) {
            if (resultCode == Activity.RESULT_OK) {
                String id = data.getStringExtra(ShowSmartPlaceInstanceActivity.RESULT_DELETE);
                if (id != null) {
                    this.application.showProgressDialog(getActivity());
                    this.application.getDataStore().getUserSmartPlaceInstances(this);
                }
            }
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
    public void done(List<SmartPlaceInstanceObject> list) {
        this.application.dismissProgressDialog(getActivity());
        refreshListView(list);
        if (list.isEmpty()) {
            String emptyText = getString(R.string.smart_place_instances_list_empty);
            setEmptyText(emptyText);
        }
    }

    private void refreshListView(List<SmartPlaceInstanceObject> list) {
        mAdapter = new SmartPlaceInstanceListAdapter(list);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void deleted() {
        this.application.showProgressDialog(getActivity());
        this.application.getDataStore().getUserSmartPlaceInstances(this);
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
    }

    private class SmartPlaceInstanceListAdapter extends ArrayAdapter<SmartPlaceInstanceObject> {

        public SmartPlaceInstanceListAdapter(List<SmartPlaceInstanceObject> list) {
            super(getActivity(), R.layout.smart_place_instance_list_item_layout, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater
                        .inflate(R.layout.smart_place_instance_list_item_layout, parent, false);
                TextView titleTextView = (TextView) convertView
                        .findViewById(R.id.smart_place_instance_list_item_title_textView);
                TextView messageTextView = (TextView) convertView
                        .findViewById(R.id.smart_place_instance_list_item_message_textView);
                TextView smartPlaceNameTextView = (TextView)
                        convertView.findViewById(R.id.smart_place_instance_list_item_smart_place_textView);
                SmartPlaceInstanceViewHolder viewHolder = new SmartPlaceInstanceViewHolder(titleTextView, messageTextView,
                        smartPlaceNameTextView);
                convertView.setTag(viewHolder);
            }
            SmartPlaceInstanceViewHolder viewHolder = (SmartPlaceInstanceViewHolder) convertView.getTag();
            SmartPlaceInstanceObject item = getItem(position);
            viewHolder.updateView(item);

            return convertView;
        }
    }

    private class SmartPlaceInstanceViewHolder implements ViewHolder<SmartPlaceInstanceObject> {
        public TextView titleTextView;
        public TextView messageTextView;
        public TextView smartPlaceNameTextView;

        public SmartPlaceInstanceViewHolder(TextView titleTextView, TextView messageTextView,
                                            TextView smartPlaceNameTextView) {
            this.titleTextView = titleTextView;
            this.messageTextView = messageTextView;
            this.smartPlaceNameTextView = smartPlaceNameTextView;
        }

        @Override
        public void updateView(SmartPlaceInstanceObject object) {
            this.titleTextView.setText(object.getTitle());
            this.messageTextView.setText(object.getMessage());
            this.smartPlaceNameTextView.setText(object.getSmartPlace().getName());
        }
    }

}
