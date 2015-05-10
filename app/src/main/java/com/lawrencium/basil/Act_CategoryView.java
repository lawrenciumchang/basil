package com.lawrencium.basil;


import android.app.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class Act_CategoryView extends Activity {

    private final SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);
    private LinearLayout lo_week1;
    private LinearLayout lo_week2;
    private LinearLayout lo_week3;
    private LinearLayout lo_week4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        Bundle bundle = getIntent().getExtras();
        String catName = bundle.getString("CAT_NAME");
        int graphMax = bundle.getInt("GRAPH_MAX");
        int graphProgress = bundle.getInt("GRAPH_PROGRESS");
        int graphSecondary = bundle.getInt("GRAPH_SECONDARY");
        BigDecimal catLeftOver = new BigDecimal(bundle.getString("CAT_LEFTOVER"));
        BigDecimal quartLeftOver = new BigDecimal(bundle.getString("QUART_LEFTOVER"));
        TextView progress_overview = (TextView) findViewById(R.id.progress_overview);
        progress_overview.setText(graphSecondary*100/graphMax +"%");

        //Calculates where the user is for their budget this month. Checks to see if they are
        //over and by how much or how much is left for the month.
        if(catLeftOver.compareTo(new BigDecimal(BigInteger.ZERO)) == -1){
            catLeftOver = catLeftOver.negate();
            TextView catLeft = (TextView) findViewById(R.id.catAmountLeftTxt);
            catLeft.setText("Monthly: $"+catLeftOver+" over");
            catLeft.setTextColor(Color.parseColor("#ffd81500"));
        }else {
            TextView overallCatLeft = (TextView) findViewById(R.id.catAmountLeftTxt);
            overallCatLeft.setText("Monthly: $" + catLeftOver + " left");
        }

        //Calculates where the user is for their budget this quarter. Checks to see if they are
        //over and by how much or how much is left for the quarter.
        if(quartLeftOver.compareTo(new BigDecimal(BigInteger.ZERO)) == -1){
            quartLeftOver = quartLeftOver.negate();
            TextView quartLeft = (TextView) findViewById(R.id.quartAmountLeftTxt);
            quartLeft.setText("Quarterly: $"+quartLeftOver+" over");
            quartLeft.setTextColor(Color.parseColor("#ffd81500"));
        }else {
            TextView quartLeft = (TextView) findViewById(R.id.quartAmountLeftTxt);
            quartLeft.setText("Quarterly: $" + quartLeftOver + " left");
            quartLeft.setTextColor(Color.parseColor("#44aa00"));
        }

        ProgressBar catGraph = (ProgressBar) findViewById(R.id.catProgessBar);
        catGraph.setMax(graphMax);
        catGraph.setProgress(graphProgress);
        catGraph.setSecondaryProgress(graphSecondary);
        if(graphSecondary >= graphMax) {
            catGraph.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_maxed));
        }
        else if(graphProgress >= graphMax) {
            catGraph.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_warning));
        }

        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        tempDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        String dateLastMonth = format.format(tempDate);
        int daysThisMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        TextView monthlyExpense = (TextView) findViewById(R.id.monthlyExpense);
        monthlyExpense.setText(catName +" Monthly Expenses");

        int boundsDate[] = Budget.calculateBoundsDate();
        int boundsDate1 = boundsDate[1]-1;
        int boundsDate2 = boundsDate[2]-1;
        int boundsDate3 = boundsDate[3]-1;
        int boundsDate4 = boundsDate[4]-1;

        lo_week1 = (LinearLayout) findViewById(R.id.lo_week1);
        // hide until its title is clicked
        lo_week1.setVisibility(View.GONE);
        TextView txtWeek1 = (TextView) findViewById(R.id.txt_week1);
        txtWeek1.setText("--------Quarter 1: " + boundsDate[5]+"/0"+boundsDate[0] +" - "+boundsDate[5]+"/0"+boundsDate1 + "--------");
        txtWeek1.setTypeface(null, Typeface.BOLD);

        lo_week2 = (LinearLayout) findViewById(R.id.lo_week2);
        // hide until its title is clicked
        lo_week2.setVisibility(View.GONE);
        TextView txtWeek2 = (TextView) findViewById(R.id.txt_week2);
        txtWeek2.setText("--------Quarter 2: " + boundsDate[5]+"/0"+boundsDate[1] +" - "+boundsDate[5]+"/"+boundsDate2 +"--------");
        txtWeek2.setTypeface(null, Typeface.BOLD);

        lo_week3 = (LinearLayout) findViewById(R.id.lo_week3);
        // hide until its title is clicked
        lo_week3.setVisibility(View.GONE);
        TextView txtWeek3 = (TextView) findViewById(R.id.txt_week3);
        txtWeek3.setText("--------Quarter 3: " + boundsDate[5]+"/"+boundsDate[2] +" - "+boundsDate[5]+"/"+boundsDate3 +"--------");
        txtWeek3.setTypeface(null, Typeface.BOLD);

        lo_week4 = (LinearLayout) findViewById(R.id.lo_week4);
        // hide until its title is clicked
        lo_week4.setVisibility(View.GONE);
        TextView txtWeek4 = (TextView) findViewById(R.id.txt_week4);
        txtWeek4.setText("--------Quarter 4: " + boundsDate[5]+"/"+boundsDate[3] +" - "+boundsDate[5]+"/"+boundsDate4 +"--------");
        txtWeek4.setTypeface(null, Typeface.BOLD);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String[] projection = {
//                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
//        };
//        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE;
//        String filter = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " > \'" + dateLastMonth + "\' AND " +
//                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " = \'" + catName + "\'";
//        Cursor c = db.query(
//                FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
//                projection,
//                filter,
//                null,
//                null,
//                null,
//                sortOrder
//        );
        Cursor c = Budget.getTransactions(db, FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " > \'" + dateLastMonth + "\' AND " +
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " = \'" + catName + "\'");
        if(c.moveToFirst()) {
            //creates transaction textViews in a linearLayout. The textViews are filtered into the
            //correct quarter for viewing purposes
            do {
                LinearLayout nextTransaction;
                String[] date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE)).split("[ :/]");

                nextTransaction = Budget.getTransactionRow(this,
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE))
                );


                switch(Budget.calculateWeek(Integer.parseInt(date[2]), daysThisMonth)) {
                    case 0: lo_week1.addView(nextTransaction); break;
                    case 1: lo_week2.addView(nextTransaction); break;
                    case 2: lo_week3.addView(nextTransaction); break;
                    case 3: lo_week4.addView(nextTransaction); break;
                }
            } while (c.moveToNext());
        }
        db.close();
    }

    /**
     * This adds items to the action bar if it is present.
     * @param menu button inflates the menu options on the top right corner of the screen
     * @return returns true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_category_view, menu);
        return true;
    }

    /**
     * For the back button on the top left button
     * @param item back arrow used for parent functionality
     * @return return needs to be true in order to return you to the previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:

                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * This method allows you to hide and view selected content specifically quarterly
     * transactions
     * @param v selected view that you want to see or hide away
     */
    public void toggle_contents(View v){

        switch(v.getId()){
            case R.id.week1:
                lo_week1.setVisibility( lo_week1.isShown()
                        ? View.GONE
                        : View.VISIBLE );
                break;
            case R.id.week2:
                lo_week2.setVisibility( lo_week2.isShown()
                        ? View.GONE
                        : View.VISIBLE );
                break;
            case R.id.week3:
                lo_week3.setVisibility( lo_week3.isShown()
                        ? View.GONE
                        : View.VISIBLE );
                break;
            case R.id.week4:
                lo_week4.setVisibility( lo_week4.isShown()
                        ? View.GONE
                        : View.VISIBLE );
        }
    }
}
