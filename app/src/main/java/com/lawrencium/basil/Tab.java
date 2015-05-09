package com.lawrencium.basil;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by James on 3/23/2015.
 */
public class Tab {
    private String UserOwed;
    private String UserOwing;
    private double AmountOwed;
    private String Category;
    private String Title;
    private String Date;
    private long transactionId;




    //Think of ways to identify different tabs

    //May create constructor that makes a tab for only ID for Tab equals methods

    //Normal Constructor
    public Tab(String userOwed, String userOwing, double amountOwed, String category, String title, long transaction) {
        UserOwed = userOwed;
        UserOwing = userOwing;
        AmountOwed = amountOwed;
        Category = category;
        Title = title;
        transactionId = transaction;
        Date tempDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm:ssa");
        Date = format.format(tempDate);
    }

    //Copy Constructor
    public Tab(Tab T) {
        UserOwed = T.getUserOwed();
        UserOwing = T.getUserOwing();
        AmountOwed = T.getAmountOwed();
        Category = T.getCategory();
        Title = T.getTitle();
        transactionId = T.getTransactionId();
        Date = T.getDate();
    }

    public String getUserOwed() {
        return UserOwed;
    }

    public void setUserOwed(String userOwed) {
        UserOwed = userOwed;
    }

    public String getUserOwing() {
        return UserOwing;
    }

    public void setUserOwing(String userOwing) {
        UserOwing = userOwing;
    }

    public double getAmountOwed() {
        return AmountOwed;
    }

    public void setAmountOwed(double amountOwed) {
        AmountOwed = amountOwed;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Tab tab = (Tab) o;
//
//        if (TabId != tab.TabId) return false;
//
//        return true;
//    }

    public String reformatDate(String date){

        String tempString = "";
        int firstBackSlash = date.indexOf('/');
        int secondBackSlash = date.indexOf('/', firstBackSlash+1);

        tempString = date.substring(firstBackSlash+1, secondBackSlash)+"/";
        tempString += date.substring(secondBackSlash+1, secondBackSlash+3)+"/";
        tempString += date.substring(2, firstBackSlash)/*+" "*/;

//        firstBackSlash = date.indexOf(':');
//        secondBackSlash = date.indexOf(':', firstBackSlash+1);
//        tempString += date.substring(firstBackSlash-2, firstBackSlash)+":";
//        tempString += date.substring(firstBackSlash+1, secondBackSlash)+" ";
//        tempString += date.substring(secondBackSlash+3, date.length());


        return tempString;
    }
    public String sendTabMsg(){
        DecimalFormat dec = new DecimalFormat("0.00");
        String amnt = dec.format(AmountOwed);
        String msg = "**"+UserOwing+"**"+UserOwed+"**"+Title+"**"+Category+"**"+amnt+"**"+Date+"**";

        return msg;
    }

    @Override
    public String toString(){
        DecimalFormat dec = new DecimalFormat("0.00");
        String amnt = "";

        String printAmount = dec.format(AmountOwed);
        if(printAmount.startsWith("-")){
            printAmount = printAmount.substring(1);
            amnt += "-$" + printAmount;
        }
        else{
            amnt += "$" + printAmount;
        }

        return "User: " + UserOwing + "\n" +
               "Amount Owed: " + amnt + "\n" +
               "Title: " + Title + "\n" +
               "Category: " + Category + "\n" +
               "Date of Transaction: " + reformatDate(Date) + "\n";
    }

//    public static long newTab(Context context, String title, String userName, String user, String amount, String category, String tabId, String date, String newTransactionId) {
    public long newTab(Context context) {
        SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(context);
        SQLiteDatabase db = tabDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, Title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWED, UserOwed);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWING, UserOwing);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT, AmountOwed);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIES, Category);

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, Date);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSACTIONID, transactionId);
        long newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                values);

        // Get email of user
        String owingEmail = "";
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL
        };
        String filter = FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND + " = \'" +
               UserOwing + "\'";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                projection,
                filter,
                null,
                null,
                null,
                null
        );
        if (c.moveToFirst()) {
            owingEmail = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL));
        }

        db.close();

        new GcmSendAsyncTask(context, UserOwed, owingEmail, UserOwing+sendTabMsg()+newRowId+"**").execute();

        return newRowId;
    }

    public static void addTabsToDatabase(Context context, double[] prices, String[] people, String category, String title, String amount){
        SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(context);
        SQLiteDatabase db = tabDbHelper.getWritableDatabase();

        int num = people.length;
        Tab tempTab;

        long newTransactionId = Budget.newTransaction(db, title, amount, category);

        for(int i = 1; i < num; i++ ) {
            // Create new corresponding transaction
            // Store tab in db
            tempTab = new Tab(people[0], people[i], prices[i], category, title, newTransactionId);
            long tabId = tempTab.newTab(context);

        }
    }

    /**
     * createDown - blah blah
     * @param context
     * @param dropdown
     * @param table
     * @param column
     * @param placeholder
     * @return
     */
    public static ArrayAdapter<String> createDropdown(Context context, Spinner dropdown, String table, String column, String placeholder){
        SQLiteDbHelper mDbHelper = new SQLiteDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ArrayList<String> categoryItems= new ArrayList<String>();
        if(placeholder != null)
            categoryItems.add(placeholder);
        String[] projection = {
                column
        };
        String sortOrder = column;
        Cursor c = db.query(
                table,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                categoryItems.add(c.getString(c.getColumnIndexOrThrow(column)));
            } while (c.moveToNext());
        }
        // Use simple_spinner_item to make the spinner display smaller
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        db.close();
        return adapter;
    }
    public static ArrayAdapter<String> createCategoriesDropdown(Context context, Spinner dropdown) {
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                if(parent.getItemAtPosition(pos).toString().equals("Add New Category")) {
                    Intent intent = new Intent(parent.getContext(), Act_NewCategory.class);
                    parent.getContext().startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> categoryAdapter = Tab.createDropdown(context, dropdown, FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Select Category");
        categoryAdapter.add("Add New Category");
        dropdown.setAdapter(categoryAdapter);

        return categoryAdapter;
    }

}
