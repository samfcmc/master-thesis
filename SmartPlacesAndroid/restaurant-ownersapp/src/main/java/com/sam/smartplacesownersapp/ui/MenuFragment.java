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

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceConfigurationCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;
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
public class MenuFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MenuListAdapter mAdapter;

    private static final String SMART_PLACE_ID = "smartPlaceId";
    private static final String CATEGORY = "category";

    private String category;

    private List<MenuItem> menu;

    private SmartPlacesOwnerApplication application;

    private SmartPlaceInstanceObject object;

    AddFloatingActionButton addButton;

    private static final int ADD_MENU_ITEM_REQUEST = 2;

    public static MenuFragment newInstance(String smartPlaceId,
                                           String category) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(SMART_PLACE_ID, smartPlaceId);
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.category = getArguments().getString(CATEGORY);
        }

        this.menu = new ArrayList<>();
        mAdapter = new MenuListAdapter(getActivity(), this.menu);
        this.application = (SmartPlacesOwnerApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        TextView emptyTextView = (TextView) view.findViewById(android.R.id.empty);
        mListView.setEmptyView(emptyTextView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String smartPlaceId = getArguments().getString(SMART_PLACE_ID);
        this.application.getDataStore().getSmartPlaceConfiguration(smartPlaceId, new SmartPlaceConfigurationCallback() {
            @Override
            public void done(SmartPlaceInstanceObject object) {
                loadMenu(object);
            }
        });

        this.addButton = (AddFloatingActionButton) view.findViewById(R.id.menu_add_button);
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMenuItem();
            }
        });
    }

    private void openAddMenuItem() {
        Intent intent = new Intent(getActivity(), NewMenuItemActivity.class);
        startActivityForResult(intent, ADD_MENU_ITEM_REQUEST);
    }

    private void loadMenu(SmartPlaceInstanceObject object) {
        this.object = object;
        JSONObject configuration = object.getData();
        try {
            if (configuration.has("menu")) {
                JSONArray menuJsonArray = getMenuForCategory(configuration.getJSONArray("menu"), this.category);
                if (menuJsonArray.length() > 0) {
                    for (int i = 0; i < menuJsonArray.length(); i++) {
                        JSONObject jsonObject = menuJsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        double price = jsonObject.getDouble("price");
                        MenuItem item = new MenuItem(name, price);
                        this.menu.add(item);
                    }
                    refreshList();
                } else {
                    listEmpty();
                }
            } else {
                listEmpty();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    private JSONArray getMenuForCategory(JSONArray array, String name) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String category = jsonObject.getString("category");
            if (category.equals(name)) {
                if (jsonObject.has("menu")) {
                    return jsonObject.getJSONArray("menu");
                } else {
                    return new JSONArray();
                }
            }
        }
        return new JSONArray();
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

    private void listEmpty() {
        final String message = getString(R.string.menu_empty);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setEmptyText(message);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ADD_MENU_ITEM_REQUEST) {
                if (data != null) {
                    String name = data.getStringExtra(NewMenuItemActivity.NAME);
                    double price = data.getDoubleExtra(NewMenuItemActivity.PRICE, 0);
                    addMenuItem(name, price);
                }
            }
        }
    }

    private void addMenuItem(final String name, final double price) {
        JSONObject jsonObject = this.object.getData();

        try {
            JSONObject menuItemJsonObject = new JSONObject();
            menuItemJsonObject.put("name", name);
            menuItemJsonObject.put("price", price);
            if (!jsonObject.has("menu")) {
                jsonObject.put("menu", new JSONObject());
            }
            JSONArray menuJsonArray = jsonObject.getJSONArray("menu");
            for (int i = 0; i < menuJsonArray.length(); i++) {
                JSONObject categoryJsonObject = menuJsonArray.getJSONObject(i);
                String categoryName = categoryJsonObject.getString("category");
                if (categoryName.equals(this.category)) {
                    if (!categoryJsonObject.has("menu")) {
                        categoryJsonObject.put("menu", new JSONArray());
                    }
                    JSONArray categoryMenuJsonArray = categoryJsonObject.getJSONArray("menu");
                    categoryMenuJsonArray.put(menuItemJsonObject);
                    this.object.setData(jsonObject);
                    this.application.getDataStore().saveSmartPlaceConfiguration(this.object,
                            new SmartPlaceConfigurationCallback() {
                                @Override
                                public void done(SmartPlaceInstanceObject object) {
                                    updateList(object, name, price);
                                }
                            });
                }
            }
        } catch (JSONException e) {

        }

    }

    private void updateList(SmartPlaceInstanceObject object, String name, double price) {
        this.object = object;
        MenuItem menuItem = new MenuItem(name, price);
        this.menu.add(menuItem);
        refreshList();
    }

    private class MenuItem {
        private String name;
        private double price;

        public MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    private class MenuListAdapter extends ArrayAdapter<MenuItem> {

        public MenuListAdapter(Context context, List<MenuItem> menu) {
            super(context, R.layout.menu_list_item_layout, menu);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuItem item = getItem(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_list_item_layout, parent, false);
            }

            TextView nameTextView = (TextView) convertView.findViewById(R.id.menu_list_item_name_textView);
            TextView priceTextView = (TextView) convertView.findViewById(R.id.menu_list_item_price_textView);

            nameTextView.setText(item.getName());
            priceTextView.setText(item.getPrice() + "");

            return convertView;
        }
    }

}
