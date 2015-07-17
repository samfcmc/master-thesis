package com.sam.ownersapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.ownersapp.R;
import com.sam.ownersapp.SmartPlacesOwnersApplication;
import com.sam.smartplaceslib.datastore.callback.DeleteCallback;
import com.sam.smartplaceslib.datastore.object.SmartPlaceInstanceObject;

import static com.sam.ownersapp.R.id.show_smart_place_instance_message_textview;

public class ShowSmartPlaceInstanceActivity extends AppCompatActivity implements DeleteCallback {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String MESSAGE = "description";
    public static final String RESULT_DELETE = "delete";

    private SmartPlacesOwnersApplication application;

    public static Intent getIntent(Context context, SmartPlaceInstanceObject smartPlaceInstanceObject) {
        Intent intent = new Intent(context, ShowSmartPlaceInstanceActivity.class);
        intent.putExtra(ID, smartPlaceInstanceObject.getId());
        intent.putExtra(TITLE, smartPlaceInstanceObject.getTitle());
        intent.putExtra(MESSAGE, smartPlaceInstanceObject.getMessage());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_smart_place_instance);
        this.application = (SmartPlacesOwnersApplication) getApplication();
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        String message = intent.getStringExtra(MESSAGE);
        TextView titleTextView = (TextView) findViewById(R.id.show_smart_place_instance_title_textview);
        TextView messageTextView = (TextView) findViewById(show_smart_place_instance_message_textview);
        ImageView editImageView = (ImageView) findViewById(R.id.show_smart_place_instance_edit_imageview);
        Button deleteButton = (Button) findViewById(R.id.show_smart_place_instance_delete_button);

        titleTextView.setText(title);
        messageTextView.setText(message);

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSmartPlaceInstance();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSmartPlaceInstance();
            }
        });
    }

    private void deleteSmartPlaceInstance() {
        String id = getIntent().getStringExtra(ID);
        this.application.getDataStore().deleteSmartPlaceInstance(id, this);
    }

    private void editSmartPlaceInstance() {
        Intent activityIntent = getIntent();
        String id = activityIntent.getStringExtra(ID);
        String title = activityIntent.getStringExtra(TITLE);
        String message = activityIntent.getStringExtra(MESSAGE);
        Intent intent = UpdateSmartPlaceInstanceActivity.getIntent(this, id, title, message);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_smart_place_instance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deleted() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_DELETE, getIntent().getStringExtra(ID));
        setResult(RESULT_OK, intent);
        finish();
    }
}
