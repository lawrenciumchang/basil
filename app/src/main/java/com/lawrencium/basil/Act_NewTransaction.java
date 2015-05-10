package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Act_NewTransaction extends Activity {
    private EditText inputName;
    private EditText inputValue;
    private Spinner inputCategory;
    private final SQLiteDbHelper SQLiteDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }
        SQLiteDatabase db = SQLiteDbHelper.getReadableDatabase();
        inputName = (EditText)findViewById(R.id.inputName);
        inputValue = (EditText)findViewById(R.id.inputValue);
        inputCategory = (Spinner)findViewById(R.id.spinnerCategories);
        inputValue.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});

        String[] projection = {
            FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
        Cursor c = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
            projection,
            null, null, null, null,
            sortOrder
        );
        //create a spinner with all the categories the user has created
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Select Category");
        if(c.moveToFirst()) {
            do {
                adapter.add(
                    c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE))
                );
            } while (c.moveToNext());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputCategory.setAdapter(adapter);
    }

    /**
     * This adds items to the action bar if it is present.
     * @param menu button inflates the menu options on the top right corner of the screen
     * @return returns true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_transaction, menu);
        return true;
    }

    /**
     * For the back button on the top left button
     * @param item back arrow used for parent functionality
     * @return return needs to be true in order to return you to the previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:

                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Create a transaction. This method is mindful of empty fields and will have an associated
     * message for specific cases. The case where a category with a $0 budget is considered.
     * @param view
     */
    public void createTransaction(View view) {
        String name = inputName.getText().toString();
        String value = inputValue.getText().toString();
        String category = inputCategory.getSelectedItem().toString();

        if(name.matches("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(value.matches("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter an amount for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(value.matches("0")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a valid amount for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();
            Date tempDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
            String date = dateFormat.format(tempDate);
            String newName = inputName.getText().toString().trim();
            String newBudget = inputValue.getText().toString().trim();
            String newCategory = inputCategory.getSelectedItem().toString().trim();

            Budget.newTransaction(db, newName, newBudget, newCategory);

            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", newName);
            bundle.putString("CATEGORY", newCategory);
            bundle.putString("VALUE", newBudget);
            bundle.putString("DATE", date);
            resultIntent.putExtras(bundle);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
