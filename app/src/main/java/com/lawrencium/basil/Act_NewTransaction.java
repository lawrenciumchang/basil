package com.lawrencium.basil;

import android.app.Activity;
import android.content.ContentValues;
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
    protected EditText inputName;
    protected EditText inputValue;
    protected Spinner inputCategory;
    SQLiteDbHelper SQLiteDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

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
                null,
                null,
                null,
                null,
                sortOrder
        );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will.
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createTransaction(View view) {
        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();
        Date tempDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        String date = format.format(tempDate);
        ContentValues values = new ContentValues();
        String newName = inputName.getText().toString().trim();
        String newBudget = inputValue.getText().toString().trim();
        String newCategory = inputCategory.getSelectedItem().toString().trim();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, newName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, newCategory);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, newBudget);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);
        long newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
                FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                values);

//        Intent intent = new Intent(this, Act_TransactionConfirm.class);
//        startActivity(intent);
        finish();
    }
}
