package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Act_TabVault extends Activity {

    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    private String userName;

    private final ArrayList<Tab> Tabs = new ArrayList<Tab>();
    private final SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(this);

    /**
     * Displays all tabs in user's database, showing the most recent at the top.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_tabs_page);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }
        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Current User from Tabs Vault page: " + userName);

        String Amount;
        Tab tempTab;
        String UserOwed;
        String UserOwing;
        double AmountOwed;
        String Category;
        String Title;
        String Date;
        String out = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__tab_vault);

        SQLiteDatabase db = tabDbHelper.getReadableDatabase();
        String[] projection ={
            FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWED,
            FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWING,
            FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT,
            FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIES,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TABID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        };

        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " DESC";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                Title = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                UserOwed = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWED));
                UserOwing = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWING));
                Amount = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT));

                AmountOwed = Double.parseDouble(Amount);
                Category = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIES));


                Date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));
                tempTab = new Tab(UserOwed, UserOwing, AmountOwed, Category, Title,0);

                tempTab.setDate(Date);
                Tabs.add(tempTab);

                DecimalFormat dec = new DecimalFormat("0.00");

                System.out.println("Tab Created: " + tempTab);
                System.out.println("Amount: "+dec.format(AmountOwed));

                TextView vault = (TextView)findViewById(R.id.vault);
                out +=tempTab.toString()+"\n";
                vault.setText(out);

            } while (c.moveToNext());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__tab_vault, menu);
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
     * Returns user to previous page.
     */
    @Override
    public void onBackPressed() {
        Intent i;
        i = new Intent(this, Act_TabsPage.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

}
