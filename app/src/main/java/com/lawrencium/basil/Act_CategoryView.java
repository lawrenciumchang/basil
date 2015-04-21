package com.lawrencium.basil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.lawrencium.basil.R.id.txt_week1;


public class Act_CategoryView extends Activity {

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);
    LinearLayout lo_week1;
    LinearLayout lo_week2;
    LinearLayout lo_week3;
    LinearLayout lo_week4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();

        Bundle bundle = getIntent().getExtras();
        String catName = bundle.getString("CAT_NAME");
        String catTotal = bundle.getString("CAT_TOTAL");


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

        lo_week1 = (LinearLayout) findViewById(R.id.lo_week1);
        // hide until its title is clicked
        lo_week1.setVisibility(View.GONE);

        lo_week2 = (LinearLayout) findViewById(R.id.lo_week2);
        // hide until its title is clicked
        lo_week2.setVisibility(View.GONE);

        lo_week3 = (LinearLayout) findViewById(R.id.lo_week3);
        // hide until its title is clicked
        lo_week3.setVisibility(View.GONE);

        lo_week4 = (LinearLayout) findViewById(R.id.lo_week4);
        // hide until its title is clicked
        lo_week4.setVisibility(View.GONE);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " DESC";
        String filter = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " > \'" + dateLastMonth + "\' AND " +
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " = \'" + catName + "\'";
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
                TextView nextTransaction = new TextView(this);
                String[] date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE)).split("[ :/]");
                String text = "[" + c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)) + "] " +
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)) + " " +
                        //"(" + c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY)) + ") " +
                        date[2]+"/"+date[1] + " $" +
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT));
                nextTransaction.setText(text);

                switch(calculateWeek(Integer.parseInt(date[2]), daysThisMonth)) {
                    case 1: lo_week1.addView(nextTransaction); break;
                    case 2: lo_week2.addView(nextTransaction); break;
                    case 3: lo_week3.addView(nextTransaction); break;
                    case 4: lo_week4.addView(nextTransaction); break;
                }
            } while (c.moveToNext());
        }
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_category_view, menu);
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

//        switch(v.getId()){
//            case R.id.week1:
//                if(txt_week1.isShown()){
//                    Fx.slide_up(this, txt_week1);
//                    txt_week1.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week1.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week1);
//                }
//                break;
//            case R.id.week2:
//                if(txt_week2.isShown()){
//                    Fx.slide_up(this, txt_week2);
//                    txt_week2.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week2.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week2);
//                }
//                break;
//            case R.id.week3:
//                if(txt_week3.isShown()){
//                    Fx.slide_up(this, txt_week3);
//                    txt_week3.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week3.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week3);
//                }
//                break;
//            case R.id.week4:
//                if(txt_week4.isShown()){
//                    Fx.slide_up(this, txt_week4);
//                    txt_week4.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week4.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week4);
//                }
//
//
//        }


    }

    /**
     * calculateWeek - calculates which week the given date is in
     * @param day
     * @param daysMax
     * @return
     * @throws java.lang.IllegalArgumentException If the date or number of days in the month is invalid
     */
    private int calculateWeek(int day, int daysMax) {
        final int[] feb = {7, 14, 21, 28};
        final int[] leapFeb = {8, 15, 26, 29};
        final int[] small = {8, 16, 23, 30};
        final int[] big = {8, 16, 24, 31};
        int[] weekRange = big;

        if(day > daysMax)
            throw new IllegalArgumentException("The date ["+day+"] cannot be greater than the number of days in the month");
        switch(daysMax) {
            case 28: weekRange = feb; break;
            case 29: weekRange = leapFeb; break;
            case 30: weekRange = small; break;
            case 31: weekRange = big; break;
            default: throw new IllegalArgumentException(daysMax + " is not a valid number of days in a month");
        }
        for(int i=0; i<4; i++) {
            if(day <= weekRange[i])
                return i+1;
        }
        return 0;
    }

}