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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


public class Act_EqualSplitPage extends Activity {
    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    String title;
    String category;
    String amount;
    String number;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__equal_split_page);
        createDropdown();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        title = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_NUMBER);

        System.out.println("Current User from Equal Split page: " + userName);

        EditText titleSet = (EditText)findViewById(R.id.equalTitle);
        titleSet.setText(title);

        Spinner categorySet = (Spinner)findViewById(R.id.equalCategory);
        categorySet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                if(parent.getItemAtPosition(pos).toString().equals("Add New Category")) {
                    Intent intent = new Intent(parent.getContext(), Act_NewCategory.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayList<String> items = new ArrayList<String>();
        items.add("Select Category");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
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
        if(c.moveToFirst()) {
            do {
                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }
        db.close();
        if(category != null) {
            for (int i = 0; i < items.size(); i++) {
                if (category.equals(items.get(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

        EditText amountSet = (EditText)findViewById(R.id.equalAmount);
        amountSet.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountSet.setText(amount);

        EditText numberSet = (EditText)findViewById(R.id.equalNumber);
        numberSet.setText(number);

    }

    protected void onResume(){
        super.onResume();

        ArrayList<String> items = new ArrayList<String>();
        createDropdown();
        Spinner categorySet = (Spinner)findViewById(R.id.equalCategory);
        items.add("Select Category");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
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
        if(c.moveToFirst()) {
            do {
                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }
        db.close();

        if(category != null) {
            for (int i = 0; i < items.size(); i++) {
                if (category.equals(items.get(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

    }

    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.equalCategory);
        ArrayList<String> items = new ArrayList<String>();
        items.add("Select Category");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
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
        if(c.moveToFirst()) {
            do {
                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }
        db.close();
        items.add("Add New Category");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__equal_split_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);

        Intent i;

        switch (item.getItemId())
        {
            case android.R.id.home:
                i = new Intent(this, Act_TabsPage.class);
                i.putExtra(PASS_CURRENT_USER, userName);
                startActivityForResult(i, 0);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i;
        i = new Intent(this, Act_TabsPage.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

    public void equalNext(View view){
        EditText equalTitle = (EditText)findViewById(R.id.equalTitle);
        String title = equalTitle.getText().toString();

        Spinner equalCategory = (Spinner)findViewById(R.id.equalCategory);
        String category = equalCategory.getSelectedItem().toString();

        EditText equalAmount = (EditText)findViewById(R.id.equalAmount);
        String amount = equalAmount.getText().toString();

        EditText equalNumber = (EditText)findViewById(R.id.equalNumber);
        String number = equalNumber.getText().toString();

        if(title.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(amount.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a total amount for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(number.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter the number of people for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(number.matches("1") || number.matches("0")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a number greater than 1.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if((!number.matches("")) || (!number.matches("1"))|| !number.matches("0")){
            Intent intent = new Intent(this, Act_EqualSplitPeoplePage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_NUMBER, number);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }
}
