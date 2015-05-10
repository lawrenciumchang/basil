package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.math.BigDecimal;



public class Act_IouPage extends Activity {
    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_USER = "com.lawrencium.basil.USER";

    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    private final SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    private String title;
    private String category;
    private String amount;
    private String user;
    private String userName;

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> userAdapter;

    /**
     * Allows user to input transaction information geared toward one user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__iou_page);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        Intent intent = getIntent();

        SharedPreferences prefs = getSharedPreferences(Act_BudgetBuddy.class.getSimpleName(),
                Context.MODE_PRIVATE);
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
        payButton.setVisibility(View.GONE);

        // Load categories from Budget side
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        EditText amountSet = (EditText)findViewById(R.id.editText);
        amountSet.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountSet.setText(amount);

        EditText title = (EditText)findViewById(R.id.editText2);
        EditText amount = (EditText)findViewById(R.id.editText);

        Bundle b = getIntent().getExtras();

        prefs = getSharedPreferences(Act_BudgetBuddy.class.getSimpleName(),
                Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "");
        if(b.getBoolean("IOU")) {
            Spinner userSet = (Spinner)findViewById(R.id.spinner2);
            userAdapter = Tab.createDropdown(this, userSet, FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS, FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, "Select User");

            requestButton.setVisibility(View.GONE);
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
                        FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSACTIONID
                };
                String filter = FeedReaderContract.FeedEntry._ID + " = \'" + b.getString("TAB_ID") + "\'";
                Cursor c = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                        updateProjection, filter, null, null, null, null
                );
                if(c.moveToFirst()) {
                    Long tabId = c.getLong(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    String tabValue = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT));
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
                    BigDecimal oldTabValue = new BigDecimal(tabValue);
                    BigDecimal oldTransValue = new BigDecimal(transactionValue);
                    BigDecimal iouPayment = new BigDecimal(b.getString("AMOUNT"));
                    BigDecimal tabBalance = oldTabValue.subtract(iouPayment);
                    BigDecimal transBalance = oldTransValue.subtract(iouPayment);
                    Log.i("New Transaction Value", transBalance.toString());

                    if(tabBalance.toString().equals("0.00")) {
                        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_TABS, FeedReaderContract.FeedEntry._ID+"="+tabId, null);
                        Log.i("Tab deleted", ""+tabId);
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT, transBalance.toString());
                        filter = FeedReaderContract.FeedEntry._ID + " = \'" + tabId + "\'";
                        int numUpdated = db.update(FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                                values, filter, null);
                        Log.i("Tab updated", ""+tabId);
                    }
                    if(transBalance.toString().equals("0.00")) {
                        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS, FeedReaderContract.FeedEntry._ID+"="+transactionId, null);
                        Log.i("Transaction deleted", ""+transactionId);
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, transBalance.toString());
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
            for (int i = 0; i < userAdapter.getCount(); i++) {
                if (userOwed.equals(userAdapter.getItem(i))) {
                    userSet.setSelection(i);
                    user = userOwed;
                }
            }
        }

        db.close();

    }

    /**
     * Resumes functionality after user has chosen to create a new Category.
     */
    protected void onResume(){
        super.onResume();

        Spinner categorySet = (Spinner)findViewById(R.id.spinner1);
        Spinner userSet = (Spinner)findViewById(R.id.spinner2);

        categoryAdapter = Tab.createCategoriesDropdown(this, categorySet);
        userAdapter = Tab.createDropdown(this, userSet, FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS, FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, "Select User");

        if(category != null) {
            for (int i = 0; i < categoryAdapter.getCount(); i++) {
                if (category.equals(categoryAdapter.getItem(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

        if(user != null) {
            for (int i = 0; i < userAdapter.getCount(); i++) {
                if (user.equals(userAdapter.getItem(i))) {
                    userSet.setSelection(i);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__iou_page, menu);
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
//    @Override
//    public void onBackPressed() {
//        Intent i;
//        i = new Intent(this, Act_TabsPage.class);
//        i.putExtra(PASS_CURRENT_USER, userName);
//        startActivityForResult(i, 0);
//    }

    /**
     * Takes user to Request page.
     * @param view
     */
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
            finish();
        }
    }

    /**
     * Takes user to Pay page.
     * @param view
     */
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
            finish();
        }
    }

}
