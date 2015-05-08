package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;


public class Act_IouPage extends Activity {
    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_USER = "com.lawrencium.basil.USER";

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    String title;
    String category;
    String amount;
    String user;
    String userName;

    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__iou_page);
        createDropdown();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        SharedPreferences prefs = getSharedPreferences(Act_BudgetBuddy.class.getSimpleName(),
                getApplicationContext().MODE_PRIVATE);
        userName = prefs.getString("user_name", "");
        title = intent.getStringExtra(Act_PayPage.PASS_TITLE);
        title = intent.getStringExtra(Act_RequestPage.PASS_TITLE);
        category = intent.getStringExtra(Act_PayPage.PASS_CATEGORY);
        category = intent.getStringExtra(Act_RequestPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_PayPage.PASS_AMOUNT);
        amount = intent.getStringExtra(Act_RequestPage.PASS_AMOUNT);
        user = intent.getStringExtra(Act_PayPage.PASS_USER);
        user = intent.getStringExtra(Act_RequestPage.PASS_USER);

        System.out.println("Current User from IOU page: " + userName);

        EditText titleSet = (EditText)findViewById(R.id.editText2);
        titleSet.setText(title);

        Button requestButton = (Button) findViewById(R.id.button);
        Button payButton = (Button) findViewById(R.id.button2);
        payButton.setVisibility(View.INVISIBLE);

        // Load categories from Budget side
        Spinner categorySet = (Spinner)findViewById(R.id.spinner1);
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
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        EditText amountSet = (EditText)findViewById(R.id.editText);
        amountSet.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountSet.setText(amount);

        EditText title = (EditText)findViewById(R.id.editText2);
        EditText amount = (EditText)findViewById(R.id.editText);

        Bundle b = getIntent().getExtras();

         prefs = getSharedPreferences(Act_BudgetBuddy.class.getSimpleName(),
                getApplicationContext().MODE_PRIVATE);
        String userName = prefs.getString("user_name", "");
        if(b.getBoolean("IOU")) {
            requestButton.setVisibility(View.INVISIBLE);
            payButton.setVisibility(View.VISIBLE);
            String userOwed = b.getString("USER_OWED");
            System.out.println("User Name: "+userName+" - User Owed: "+userOwed);
            if(userOwed.equals(userName)) {
                // transaction equalizing magic
                System.out.println("Names match");

                Log.i("Tab ID", b.getString("TAB_ID"));

                long transactionId;
                String[] updateProjection = {
                        FeedReaderContract.FeedEntry._ID,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSACTIONID
                };
                String filter = FeedReaderContract.FeedEntry._ID + " = \'" + b.getString("TAB_ID") + "\'";
                Cursor c = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                        updateProjection, filter, null, null, null, null
                );
                if(c.moveToFirst()) {
                    Long tabId = c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    transactionId = c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSACTIONID));
                    Log.i("Transaction ID from Tab", ""+transactionId);

                    String[] transactionProjection = {
                            FeedReaderContract.FeedEntry._ID,
                            FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE
                    };
                    filter = FeedReaderContract.FeedEntry._ID + " = \'" + transactionId + "\'";
                    c = db.query(
                            FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
                            transactionProjection, filter, null, null, null, null
                    );
                    String transactionValue = "";
                    if(c.moveToFirst()) {
                        Log.i("Tran ID from Tran DB", c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
                        transactionValue = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE));
                        Log.i("Transaction Value", transactionValue);
                    }
                    BigDecimal oldValue = new BigDecimal(transactionValue);
                    BigDecimal iouPayment = new BigDecimal(b.getString("AMOUNT"));
                    BigDecimal balance = oldValue.add(iouPayment);
                    Log.i("New Transaction Value", balance.toString());

                    if(balance.toString().equals("0.00")) {
                        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS, FeedReaderContract.FeedEntry._ID+"="+transactionId, null);
                        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_TABS, FeedReaderContract.FeedEntry._ID+"="+tabId, null);
                        Log.i("Transaction deleted", ""+transactionId);
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, balance.toString());
                        filter = FeedReaderContract.FeedEntry._ID + " = \'" + transactionId + "\'";
                        int numUpdated = db.update(FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
                                values, filter, null);
                        Log.i("Transaction updated", ""+transactionId);
                    }
                }


                finish();
            }
            title.setText(b.getString("TITLE"));
            amount.setText(b.getString("AMOUNT"));
        }

        db.close();

    }

    protected void onResume(){
        super.onResume();

        Spinner categorySet = (Spinner)findViewById(R.id.spinner1);
        if(category != null) {
            for (int i = 0; i < categoryAdapter.getCount(); i++) {
                if (category.equals(categoryAdapter.getItem(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

        Spinner userSet = (Spinner)findViewById(R.id.spinner2);
        if(user != null) {
            for (int i = 0; i < userAdapter.getCount(); i++) {
                if (user.equals(userAdapter.getItem(i))) {
                    userSet.setSelection(i);
                }
            }
        }
    }

    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        ArrayList<String> categoryItems= new ArrayList<String>();
        categoryItems.add("Select Category");
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
                categoryItems.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }
        categoryItems.add("Add New Category");
        // Use simple_spinner_item to make the spinner display smaller
        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(categoryAdapter);

        Spinner dropdown2 = (Spinner)findViewById(R.id.spinner2);
        ArrayList<String> userItems = new ArrayList<String>();
        userItems.add("Select User");

        String[] userProjection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND
        };
        sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND;
        c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                userProjection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                userItems.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND)));
                Log.i(null, c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND)));
            } while (c.moveToNext());
        }
        userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userItems);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(userAdapter);
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__iou_page, menu);
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

    public void requestClick(View view){

        EditText editText2 = (EditText)findViewById(R.id.editText2);
        String title = editText2.getText().toString();

        Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        String category = spinner1.getSelectedItem().toString();

        EditText editText = (EditText)findViewById(R.id.editText);
        String amount = editText.getText().toString();

        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        String user = spinner2.getSelectedItem().toString();

        if(title.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(amount.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter an amount for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(user.matches("Select User")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a user for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Intent intent = new Intent(this, Act_RequestPage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_USER, user);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

    public void payClick(View view){

        EditText editText2 = (EditText)findViewById(R.id.editText2);
        String title = editText2.getText().toString();

        Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        String category = spinner1.getSelectedItem().toString();

        EditText editText = (EditText)findViewById(R.id.editText);
        String amount = editText.getText().toString();

        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        String user = spinner2.getSelectedItem().toString();

        if(title.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(amount.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter an amount for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(user.matches("Select User")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a user for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else{
            Intent intent = new Intent(this, Act_PayPage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_USER, user);
            intent.putExtra(PASS_CURRENT_USER, userName);
            Bundle bOut = new Bundle();
            Bundle bIn = getIntent().getExtras();
            bOut.putString("OWED_TABID", bIn.getString("TAB_ID"));
            bOut.putString("USER_OWED", bIn.getString("USER_OWED"));
            intent.putExtras(bOut);
            startActivity(intent);
        }
    }

}
