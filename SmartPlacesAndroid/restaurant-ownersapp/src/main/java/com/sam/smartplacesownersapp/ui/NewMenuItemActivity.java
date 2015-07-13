package com.sam.smartplacesownersapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sam.smartplacesownersapp.R;

public class NewMenuItemActivity extends ActionBarActivity {

    private EditText nameEditText;
    private EditText priceEditText;
    private Button saveButton;

    public static final String NAME = "name";
    public static final String PRICE = "price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);

        this.nameEditText = (EditText) findViewById(R.id.new_menu_item_name_editText);
        this.priceEditText = (EditText) findViewById(R.id.new_menu_item_price_editText);
        this.saveButton = (Button) findViewById(R.id.new_menu_item_save_button);

        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        String name = this.nameEditText.getText().toString();
        String priceStr = this.priceEditText.getText().toString();
        if (!name.isEmpty() && !priceStr.isEmpty()) {
            double price = Double.parseDouble(priceStr);
            Intent intent = new Intent();
            intent.putExtra(NAME, name);
            intent.putExtra(PRICE, price);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_menu_item, menu);
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
}
