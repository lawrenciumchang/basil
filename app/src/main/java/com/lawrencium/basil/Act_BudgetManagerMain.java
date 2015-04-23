package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Act_BudgetManagerMain extends Activity {

//    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);
//    LinearLayout lo_expenseOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager_main);

        TextView progress_overview = (TextView) findViewById(R.id.progress_overview);
        progress_overview.setText(getString(R.string.str_progress) +"%");

//        Calendar calendar = Calendar.getInstance();
//        Date tempDate = new Date();
//        calendar.setTime(tempDate);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        tempDate = calendar.getTime();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
//        String dateLastMonth = format.format(tempDate);
//        int daysThisMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        lo_expenseOverview = (LinearLayout) findViewById(R.id.lo_expenseOverview);
//
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String[] projection = {
//                FeedReaderContract.FeedEntry._ID,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
//        };
//        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE ;
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
//        if(c.moveToFirst()) {
//            do {
//                TextView nextTransaction = new TextView(this);
//                String[] date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE)).split("[ :/]");
//                String text = date[1]+"/"+date[2] + " " +
//                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)) + " " +
//                        //"(" + c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY)) + ") " +
//                        " $" + c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE));
//                nextTransaction.setText(text);
//                nextTransaction.setTextSize(15);
//
//                lo_expenseOverview.addView(nextTransaction);
//
//            } while (c.moveToNext());
//        }
//        db.close();
    }

    public void gotoNewTransaction(View view){
        Intent intent = new Intent(this, Act_NewTransaction.class);
        startActivity(intent);
    }
    public void gotoOverview(View view){
        Intent intent = new Intent(this, Act_BudgetOverview.class);
        startActivity(intent);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
