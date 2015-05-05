package com.sam.smartplacesownersapp.ui;

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
import android.widget.Toast;

import com.sam.smartplaceslib.bluetooth.BeaconScanCallback;
import com.sam.smartplacesownersapp.R;

import com.sam.smartplacesownersapp.SmartPlacesOwnerApplication;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBeaconListFragmentInteractionListener}
 * interface.
 */
public class BeaconListFragment extends Fragment implements AbsListView.OnItemClickListener, BeaconScanCallback<Beacon> {

    private OnBeaconListFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private List<Beacon> beacons;

    private SmartPlacesOwnerApplication application;

    public static BeaconListFragment newInstance() {
        BeaconListFragment fragment = new BeaconListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeaconListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.beacons = new ArrayList<Beacon>();
        mAdapter = new BeaconObjectListAdapter(getActivity(), this.beacons);
        this.application = (SmartPlacesOwnerApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beaconobject, container, false);

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
        this.application.getBeaconsManager().startScan(getActivity(), this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBeaconListFragmentInteractionListener) activity;
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
            Beacon beacon = this.beacons.get(position);
            String uuid = beacon.getId1().toHexString();
            int major = beacon.getId2().toInt();
            int minor = beacon.getId3().toInt();
            mListener.onBeaconSelected(uuid, major, minor);
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
    public void beaconsFound(Collection<Beacon> beacons) {
        if(!beacons.isEmpty()) {
            logToDisplay("Beacons found");
            this.application.getBeaconsManager().stopScan();
            for(Beacon beacon : beacons) {
                this.beacons.add(beacon);
            }
            refreshBeaconsList();
        }
    }

    private void refreshBeaconsList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setAdapter(mAdapter);
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
    public interface OnBeaconListFragmentInteractionListener {
        public void onBeaconSelected(String uuid, int major, int minor);
    }

    private class BeaconObjectListAdapter extends ArrayAdapter<Beacon> {

        public BeaconObjectListAdapter(Context context, List<Beacon> beacons) {
            super(context, R.layout.beacon_list_item, beacons);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.beacon_list_item, parent, false);
            }

            Beacon beacon = getItem(position);
            TextView uuidTextView = (TextView) convertView.findViewById(R.id.beacon_uuid_textView);
            TextView majorTextView = (TextView) convertView.findViewById(R.id.beacon_major_textView);
            TextView minorTextView = (TextView) convertView.findViewById(R.id.beacon_minor_textView);
            TextView distanceTextView = (TextView) convertView.findViewById(R.id.beacon_distance_textView);

            uuidTextView.setText(beacon.getId1().toHexString());
            majorTextView.setText(beacon.getId2().toHexString());
            minorTextView.setText(beacon.getId3().toHexString());
            distanceTextView.setText(beacon.getDistance() + "");

            return convertView;
        }
    }

    private void logToDisplay(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
