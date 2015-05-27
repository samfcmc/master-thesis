package com.sam.smartplacesownersapp.ui;

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
import android.widget.Toast;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceConfigurationObject;
import com.sam.smartplacesownersapp.R;
import com.sam.smartplacesownersapp.SmartPlacesOwnerApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class CategoryMenuFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CategoryListAdapter mAdapter;

    private List<String> categories;

    private SmartPlacesOwnerApplication application;

    private static final String SMART_PLACE_ID = "smartPlaceId";

    private static final int ADD_CATEGORY_REQUEST = 2;

    private SmartPlaceConfigurationObject smartPlaceConfiguration;

    public static CategoryMenuFragment newInstance(String smartPlaceId) {
        CategoryMenuFragment fragment = new CategoryMenuFragment();
        Bundle args = new Bundle();
        args.putString(SMART_PLACE_ID, smartPlaceId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

        this.categories = new ArrayList<String>();
        mAdapter = new CategoryListAdapter(getActivity(), this.categories);
        this.application = (SmartPlacesOwnerApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorymenu, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        TextView emptyView = (TextView) view.findViewById(android.R.id.empty);
        mListView.setEmptyView(emptyView);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String smartPlaceId = getArguments().getString(SMART_PLACE_ID);
        if (this.smartPlaceConfiguration == null) {
            this.application.showProgressDialog(getActivity());
            this.application.getDataStore().getSmartPlaceConfiguration(smartPlaceId,
                    new SmartPlaceConfigurationCallback() {
                        @Override
                        public void done(SmartPlaceConfigurationObject object) {
                            loadCategories(object);
                        }
                    });
        }

        AddFloatingActionButton addButton = (AddFloatingActionButton) view.findViewById(R.id.categorymenu_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButtonClicked();
            }
        });
    }

    private void addButtonClicked() {
        Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
        startActivityForResult(intent, ADD_CATEGORY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ADD_CATEGORY_REQUEST) {
                if (data != null) {
                    String name = data.getStringExtra(NewCategoryActivity.CATEGORY_NAME);
                    logToDisplay("Add category worked " + name);
                    addCategory(name);
                }
            }
        }
    }

    private void addCategory(final String name) {
        JSONObject object = this.smartPlaceConfiguration.getObject();
        if (object == null) {
            object = new JSONObject();
            this.smartPlaceConfiguration.setObject(object);
        }

        try {
            if (!object.has("menu")) {
                JSONArray menuJsonArray = new JSONArray();
                object.put("menu", menuJsonArray);
            }
            JSONArray menu = object.getJSONArray("menu");
            JSONObject categoryJsonObject = new JSONObject();
            categoryJsonObject.put("category", name);
            menu.put(categoryJsonObject);
            this.application.getDataStore().saveSmartPlaceConfiguration(this.smartPlaceConfiguration,
                    new SmartPlaceConfigurationCallback() {
                        @Override
                        public void done(SmartPlaceConfigurationObject object) {
                            categories.add(name);
                            refreshList();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logToDisplay(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadCategories(SmartPlaceConfigurationObject object) {
        this.application.dismissProgressDialog(getActivity());
        this.smartPlaceConfiguration = object;
        this.categories.clear();
        JSONObject configuration = object.getObject();
        if (configuration != null) {
            if (configuration.has("menu")) {
                try {
                    JSONArray menu = configuration.getJSONArray("menu");
                    if (menu.length() > 0) {
                        for (int i = 0; i < menu.length(); i++) {
                            JSONObject category = menu.getJSONObject(i);
                            String categoryName = category.getString("category");
                            this.categories.add(categoryName);
                        }
                        refreshList();
                    } else {
                        listEmpty();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                listEmpty();
            }
        } else {
            listEmpty();
        }
    }

    private void refreshList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setAdapter(mAdapter);
            }
        });

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
        String category = this.categories.get(position);
        mListener.onCategoryClicked(category);
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

    private void listEmpty() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String emptyMessage = getString(R.string.categories_empty);
                setEmptyText(emptyMessage);
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
    public interface OnFragmentInteractionListener {
        void onCategoryClicked(String category);
    }

    private class CategoryListAdapter extends ArrayAdapter<String> {

        public CategoryListAdapter(Context context, List<String> categories) {
            super(context, R.layout.category_list_item, categories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String category = getItem(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.category_list_item, parent, false);
            }
            TextView categoryTextView = (TextView) convertView.findViewById(R.id.category_list_item_textview);
            categoryTextView.setText(category);

            return convertView;
        }
    }

}
