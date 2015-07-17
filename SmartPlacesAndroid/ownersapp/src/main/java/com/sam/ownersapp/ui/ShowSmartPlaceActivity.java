package com.sam.ownersapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sam.ownersapp.R;
import com.sam.smartplaceslib.datastore.object.SmartPlaceObject;

public class ShowSmartPlaceActivity extends AppCompatActivity {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private String id;
    private String name;
    private String description;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param smartPlaceObject SmartPlaceObject instance.
     * @return A new instance of intent for ShowSmartPlaceActivity.
     */
    public static Intent getIntent(SmartPlaceObject smartPlaceObject, Context context) {
        Intent intent = new Intent(context, ShowSmartPlaceActivity.class);
        intent.putExtra(ID, smartPlaceObject.getId());
        intent.putExtra(NAME, smartPlaceObject.getName());
        intent.putExtra(DESCRIPTION, smartPlaceObject.getDescription());
        return intent;
    }

    public ShowSmartPlaceActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_smart_place);
        Intent intent = getIntent();
        this.id = intent.getStringExtra(ID);
        this.name = intent.getStringExtra(NAME);
        this.description = intent.getStringExtra(DESCRIPTION);
        TextView nameTextView = (TextView) findViewById(R.id.show_smart_place_name_textView);
        TextView descriptionTextView = (TextView) findViewById(R.id.show_smart_place_description_textView);
        Button createInstanceButton = (Button) findViewById(R.id.show_smart_place_create_instance_button);

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
        //TODO:
    }


}
