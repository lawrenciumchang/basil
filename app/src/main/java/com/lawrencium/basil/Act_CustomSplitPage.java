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

import java.util.ArrayList;


public class Act_CustomSplitPage extends Activity {

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

    ArrayAdapter<String> categoryAdapter;

    /**
     * Allows user to input transaction information
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__custom_split_page);
//        createDropdown();
        getActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        title = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_TITLE);
        category = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_NUMBER);

        System.out.println("Current User from Custom Split page: " + userName);

        EditText titleSet = (EditText)findViewById(R.id.customTitle);
        titleSet.setText(title);

        Spinner categorySet = (Spinner)findViewById(R.id.customCategory);
        categoryAdapter = Tab.createCategoriesDropdown(this, categorySet);
//        categorySet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
//                if(parent.getItemAtPosition(pos).toString().equals("Add New Category")) {
//                    Intent intent = new Intent(parent.getContext(), Act_NewCategory.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        ArrayList<String> items = new ArrayList<String>();
//        items.add("Select Category");
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String[] projection = {
//                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
//        };
//        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
//        Cursor c = db.query(
//                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                sortOrder
//        );
//        if(c.moveToFirst()) {
//            do {
//                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
//            } while (c.moveToNext());
//        }
//        db.close();
//        if(category != null) {
//            for (int i = 0; i < items.size(); i++) {
//                if (category.equals(items.get(i))) {
//                    categorySet.setSelection(i);
//                }
//            }
//        }

        EditText amountSet = (EditText)findViewById(R.id.customAmount);
        amountSet.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountSet.setText(amount);

        EditText numberSet = (EditText)findViewById(R.id.customNumber);
        numberSet.setText(number);

    }

    /**
     * Resumes functionality after user has chosen to create a new Category.
     */
    protected void onResume(){
        super.onResume();

//        ArrayList<String> items = new ArrayList<String>();
//        createDropdown();
        Spinner categorySet = (Spinner)findViewById(R.id.customCategory);
//        items.add("Select Category");
//
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String[] projection = {
//                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
//        };
//        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
//        Cursor c = db.query(
//                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                sortOrder
//        );
//        if(c.moveToFirst()) {
//            do {
//                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
//            } while (c.moveToNext());
//        }
//        db.close();
//
//        if(category != null) {
//            for (int i = 0; i < items.size(); i++) {
//                if (category.equals(items.get(i))) {
//                    categorySet.setSelection(i);
//                }
//            }
//        }
        if(category != null) {
            for (int i = 0; i < categoryAdapter.getCount(); i++) {
                if (category.equals(categoryAdapter.getItem(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

    }

    /**
     * Creates a dropdown for users and categories.
     */
    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.customCategory);
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
        getMenuInflater().inflate(R.menu.menu_act__customized_split_page, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_TabsPage.class
     */
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

    /**
     * Takes user to previous page.
     */
    @Override
    public void onBackPressed() {
        Intent i;
        i = new Intent(this, Act_TabsPage.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

    /**
     * Takes user to next page to select users and split transaction information.
     * @param view
     */
    public void customNext(View view){
        EditText customTitle = (EditText)findViewById(R.id.customTitle);
        String title = customTitle.getText().toString();

        Spinner customCategory = (Spinner)findViewById(R.id.customCategory);
        String category = customCategory.getSelectedItem().toString();

        EditText customAmount = (EditText)findViewById(R.id.customAmount);
        String amount = customAmount.getText().toString();

        EditText customNumber = (EditText)findViewById(R.id.customNumber);
        String number = customNumber.getText().toString();

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
            Intent intent = new Intent(this, Act_CustomSplitPeoplePage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_NUMBER, number);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

}
