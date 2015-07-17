package com.sam.ownersapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.callback.SmartPlaceInstanceCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;

public class UpdateSmartPlaceInstanceActivity extends AppCompatActivity implements SmartPlaceInstanceCallback {

    private static final String SMART_PLACE = "smartPlace";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String EDIT = "edit";

    private EditText titleEditText;
    private EditText messageEditText;

    private SmartPlacesOwnersApplication application;

    private boolean editMode;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of intent for UpdateSmartPlaceInstanceActivity.
     */
    public static Intent getIntent(Context context, String smartPlaceId) {
        Intent intent = new Intent(context, UpdateSmartPlaceInstanceActivity.class);
        intent.putExtra(SMART_PLACE, smartPlaceId);
        return intent;
    }

    public static Intent getIntent(Context context, SmartPlaceInstanceObject smartPlaceInstanceObject) {
        Intent intent = new Intent(context, UpdateSmartPlaceInstanceActivity.class);
        intent.putExtra(TITLE, smartPlaceInstanceObject.getTitle());
        intent.putExtra(MESSAGE, smartPlaceInstanceObject.getMessage());
        intent.putExtra(ID, smartPlaceInstanceObject.getId());
        intent.putExtra(EDIT, true);
        return intent;
    }

    public UpdateSmartPlaceInstanceActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_smart_place_instance);
        Intent intent = getIntent();
        this.editMode = intent.getBooleanExtra(EDIT, false);
        this.application = (SmartPlacesOwnersApplication) getApplication();
        this.titleEditText = (EditText) findViewById(R.id.update_smart_place_instance_title_editText);
        this.messageEditText = (EditText) findViewById(R.id.update_smart_place_instance_message_editText);

        if (this.editMode) {
            String title = intent.getStringExtra(TITLE);
            String message = intent.getStringExtra(MESSAGE);
            this.titleEditText.setText(title);
            this.messageEditText.setText(message);
        }

        Button saveButton = (Button) findViewById(R.id.update_smart_place_instance_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });
    }

    private void onSaveClicked() {
        Intent intent = getIntent();
        String title = this.titleEditText.getText().toString();
        String message = this.messageEditText.getText().toString();
        if (this.editMode) {
            String id = intent.getStringExtra(ID);
            updateSmartPlaceInstance(id, title, message);
        } else {
            String smartPlaceId = intent.getStringExtra(SMART_PLACE);
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
    public void done(SmartPlaceInstanceObject object) {
        Intent intent = new Intent();
        intent.putExtra(TITLE, object.getTitle());
        intent.putExtra(MESSAGE, object.getMessage());
        setResult(RESULT_OK, intent);
        finish();
    }

}
