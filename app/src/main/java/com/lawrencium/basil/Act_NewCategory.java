package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Act_NewCategory extends Activity {
    protected EditText inputName;
    protected EditText inputBudget;
    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        inputName = (EditText)findViewById(R.id.inputName);
        inputBudget = (EditText)findViewById(R.id.inputBudget);
        inputBudget.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_category, menu);
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
            ContentValues values = new ContentValues();
            String newName = inputName.getText().toString().trim();
            String newBudget = inputBudget.getText().toString().trim();

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, newName);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, newBudget);
            long newRowId = db.insert(
                    FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                    FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                    values);

            finish();
        }
    }

    public void fillFields(final String name, final String budget) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                inputName.setText(name);
                inputBudget.setText(budget);
            }
        });
    }
}
