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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Act_BudgetManagerMain extends Activity {

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manager_main);

        TextView progress_overview = (TextView) findViewById(R.id.progress_overview);
        progress_overview.setText(getString(R.string.str_progress) +"%");

        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        calendar.add(Calendar.MONTH, -2);
        tempDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        String dateLastMonth = format.format(tempDate);
        LinearLayout ll = (LinearLayout) findViewById(R.id.lo_budgetmain);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " DESC";
        String filter = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " > \'" + dateLastMonth + "\'";
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
                TextView nextTransaction = getTransactionTextView(
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE)),
                        null);
                ll.addView(nextTransaction);
            } while (c.moveToNext());
        }
        db.close();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
