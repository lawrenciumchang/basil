package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Act_NewCategory extends Activity {
    private EditText inputName;
    private EditText inputBudget;
    final SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        //set-up for category creation
        inputName = (EditText)findViewById(R.id.inputName);
        inputBudget = (EditText)findViewById(R.id.inputBudget);
        inputBudget.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
    }

    /**
     * This adds items to the action bar if it is present.
     * @param menu button inflates the menu options on the top right corner of the screen
     * @return returns true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_category, menu);
        return true;
    }

    /**
     * Handles action bar item clicks here. The action bar will automatically handle clicks on the
     * Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     * @param item back arrow used for parent functionality
     * @return return needs to be true in order to return you to the previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Category creation. This method creates a category based off of user input. It also takes care
     * of cases with empty fields and will give the user an error message. Data associated with the
     * category will be stored into the database.
     * @param view
     */
    public void createCategory(View view){
        String name = inputName.getText().toString();
        String value = inputBudget.getText().toString();

        if(name.matches("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your request.");
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
        else {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            String newName = inputName.getText().toString().trim();
//            String newBudget = inputBudget.getText().toString().trim();
//
//            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, newName);
//            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, newBudget);
//            long newRowId = db.insert(
//                    FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
//                    FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
//                    values);
            Budget.newCategory(db, inputName.getText().toString().trim(), inputBudget.getText().toString().trim());

            finish();
        }
    }

}
