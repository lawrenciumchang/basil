package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Act_BudgetManagerMain extends Activity {

    private final SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager_main);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        // Save the dates for the first day of this month and two months ago
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        calendar.setTime(today);     // Resetting time of day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // First day of this month
        calendar.setTime(today);     // Two months ago
        calendar.add(Calendar.MONTH, -1);
        String twoMonthsAgo = format.format(calendar.getTime());

        LinearLayout ll = (LinearLayout) findViewById(R.id.lo_budgetmain);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = Budget.getTransactions(db, FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " > \'" + twoMonthsAgo + "\'");
        if(c.moveToFirst()) {
            do {
                //Transaction listed with the most recent transaction at the top for the past 2
                //months, this falls under the expense heading.
                String value = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE));
                String date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));
                LinearLayout nextTransaction = Budget.getTransactionRow(this,
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                        value,
                        date
                );
                ll.addView(nextTransaction);

            } while (c.moveToNext());
        }
        c.close();
    }

    /**
     * Sends the user to the transaction page to create a new transaction.
     * @param view
     */
    public void gotoNewTransaction(View view){
        Intent intent = new Intent(this, Act_NewTransaction.class);
        startActivityForResult(intent, 0);
    }

    /**
     * Sends the user to the Overview page with all the categories that the user has created
     * @param view
     */
    public void gotoOverview(View view){
        Intent intent = new Intent(this, Act_BudgetOverview.class);
        startActivity(intent);
    }

    /**
     * This method checks to see if the transaction was created properly, if it did it takes the
     * requestCode to see where the result is coming from and creates a transaction using the data
     * the user entered in. This transaction will be added into the Expense Overview on the main page.
     * @param requestCode Gets activity the result is coming from
     * @param resultCode Checks to see if the activity was completed successfully
     * @param data Data from creating a transaction
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    LinearLayout ll = (LinearLayout) findViewById(R.id.lo_budgetmain);

                    LinearLayout nextTransaction = Budget.getTransactionRow(this,
                            bundle.getString("TITLE"),
                            bundle.getString("VALUE"),
                            bundle.getString("DATE")
                    );

                    ll.addView(nextTransaction, 3);
                }
                break;
            }
        }
    }

    /**
     * This adds items to the action bar if it is present.
     * @param menu button inflates the menu options on the top right corner of the screen
     * @return returns true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_manager, menu);
        return true;
    }

    /**
     * For the back button on the top left button
     * @param item back arrow used for parent functionality
     * @return return needs to be true in order to return you to the previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId())
        {
            case android.R.id.home:
//                i = new Intent(this, Act_BudgetBuddy.class);
//                startActivityForResult(i, 0);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * This method does several things.
     * It saves the dates for the first of the current month as well as current day a month ago.
     * Gets transactions from the past 60 days.
     * Creates the overall monthly budget but adding up all the category budget values.
     * Set text for the the amount of money left or how much the user has gone over the budget.
     */
    public void onResume(){
        super.onResume();

        TextView textProgress = (TextView) findViewById(R.id.progress_overview);
        ProgressBar graphProgress = (ProgressBar) findViewById(R.id.graph_overview);

        // Save the dates for the first day of this month and two months ago
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        calendar.setTime(today);     // Resetting time of day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // First day of this month
        String dayOneThisMonth = format.format(calendar.getTime());
        calendar.setTime(today);     // Two months ago
        calendar.add(Calendar.MONTH, -1);
        String twoMonthsAgo = format.format(calendar.getTime());

        BigDecimal total = new BigDecimal(BigInteger.ZERO);

        // Get transactions from the last 60 days
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " DESC";
        String filter = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " > \'" + twoMonthsAgo + "\'";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
                projection,
                filter,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                String value = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE));
                String date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));

                if(date.compareTo(dayOneThisMonth) > 0) {
                    BigDecimal transactionValue = new BigDecimal(value);
                    total = total.add(transactionValue);
                }
            } while (c.moveToNext());
        }
        c.close();
        // Get sum of category values

        c = db.rawQuery("SELECT SUM("+ FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE +
                ") AS myTotal FROM " + FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES, null);
        String catSum;
        if(c.moveToFirst()) {
            catSum = c.getString(c.getColumnIndex("myTotal"));
            if(catSum == null)
                catSum = "0.00";
        }
        else
            catSum = "0";
        System.out.println("myTotal = "+catSum);
        db.close();

        BigDecimal totalBudget = new BigDecimal(catSum);
        BigDecimal percentProgress = total.multiply(new BigDecimal(100));
        BigDecimal monthLeftOver = totalBudget.subtract(total);
        //set text here monthLeftOver
        if(monthLeftOver.compareTo(new BigDecimal(BigInteger.ZERO)) == -1) {
            monthLeftOver = monthLeftOver.negate();
            TextView monthLeftOverView = (TextView) findViewById(R.id.monthLeftOverTxt);
            monthLeftOverView.setText("$" + monthLeftOver + " over");
            monthLeftOverView.setTextColor(Color.parseColor("#ffd81500"));
            TextView percentage = (TextView) findViewById(R.id.progress_overview);
            percentage.setTextColor(Color.parseColor("#ff000000"));
            ProgressBar graph = (ProgressBar) findViewById(R.id.graph_overview);
            graph.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_maxed));
        }else{
            TextView monthLeftOverView = (TextView) findViewById(R.id.monthLeftOverTxt);
            monthLeftOverView.setText("$" + monthLeftOver + " left");
        }
        try {
            percentProgress = percentProgress.divide(totalBudget, BigDecimal.ROUND_HALF_DOWN);
        } catch(ArithmeticException e) {
            if(e.getMessage().equals("Division by zero")){
                percentProgress = new BigDecimal(BigInteger.ZERO);
            }
        }

        graphProgress.setProgress(percentProgress.intValue());
        textProgress.setText(percentProgress + "%");

    }
}
