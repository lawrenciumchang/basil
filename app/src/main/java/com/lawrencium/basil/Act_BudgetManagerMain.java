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

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);
    private BigDecimal MonthLeftOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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

        LinearLayout ll = (LinearLayout) findViewById(R.id.lo_budgetmain);

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
                String id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                String value = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE));
                String date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));
                TextView nextTransaction = getTransactionTextView(
                        /*"["+id+"] "+*/c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                        value,
                        date,
                        null);
                ll.addView(nextTransaction);

                /*if(date.compareTo(dayOneThisMonth) > 0) {
                    BigDecimal transactionValue = new BigDecimal(value);
                    total = total.add(transactionValue);
                }*/
            } while (c.moveToNext());
        }
        c.close();
    }

    private TextView getTransactionTextView(String title, String value, String date, String category) {
        TextView nextTransaction = new TextView(this);

        BigDecimal bdValue = new BigDecimal(value);
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMinimumIntegerDigits(1);
        String valueStr = df.format(bdValue);

        String[] splitDate = date.split("[ :/]");
        String text = splitDate[1]+"/"+splitDate[2] + " " +
                title + " " +
                " $" + valueStr;
        nextTransaction.setText(text);
        nextTransaction.setTextSize(15);
        return nextTransaction;
    }

    public void gotoNewTransaction(View view){
        Intent intent = new Intent(this, Act_NewTransaction.class);
        startActivityForResult(intent, 0);
    }
    public void gotoOverview(View view){
        Intent intent = new Intent(this, Act_BudgetOverview.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    LinearLayout ll = (LinearLayout) findViewById(R.id.lo_budgetmain);

                    TextView nextTransaction = getTransactionTextView(
                            bundle.getString("TITLE"),
                            bundle.getString("VALUE"),
                            bundle.getString("DATE"),
                            null);

                    ll.addView(nextTransaction, 3);
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_manager, menu);
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
//                i = new Intent(this, Act_BudgetBuddy.class);
//                startActivityForResult(i, 0);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

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

        LinearLayout ll = (LinearLayout) findViewById(R.id.lo_budgetmain);

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
                String id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                String value = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE));
                String date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));
                /*TextView nextTransaction = getTransactionTextView(
                        *//*"["+id+"] "+*//*c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                        value,
                        date,
                        null);
                ll.addView(nextTransaction);*/
                if(date.compareTo(dayOneThisMonth) > 0) {
                    BigDecimal transactionValue = new BigDecimal(value);
                    total = total.add(transactionValue);
                }
            } while (c.moveToNext());
        }
        c.close();
        // Get sum of category values
        String[] catProjection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE
        };
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
        MonthLeftOver = totalBudget.subtract(total);
        //set text here monthLeftOver
        if(MonthLeftOver.compareTo(new BigDecimal(BigInteger.ZERO)) == -1) {
            MonthLeftOver = MonthLeftOver.negate();
            TextView monthLeftOverView = (TextView) findViewById(R.id.monthLeftOverTxt);
            monthLeftOverView.setText("$" + MonthLeftOver + " over");
            monthLeftOverView.setTextColor(Color.parseColor("#ffd81500"));
        }else{
            TextView monthLeftOverView = (TextView) findViewById(R.id.monthLeftOverTxt);
            monthLeftOverView.setText("$" + MonthLeftOver + " left");
        }
        try {
            percentProgress = percentProgress.divide(totalBudget, BigDecimal.ROUND_HALF_DOWN);
        } catch(ArithmeticException e) {
            if(e.getMessage() == "Division by zero"){
                percentProgress = new BigDecimal(BigInteger.ZERO);
            }
        }

        graphProgress.setProgress(percentProgress.intValue());
        textProgress.setText(percentProgress + "%");

    }
}
