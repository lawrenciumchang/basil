package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class Act_TabVault extends Activity {

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String userName;

    private ArrayList<Tab> Tabs = new ArrayList<Tab>();
    TabsDbHelper tabDbHelper = new TabsDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_tabs_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Current User from Tabs Vault page: " + userName);


        String Amount;
        String tabId;
        Tab tempTab;
         String UserOwed;
         String UserOwing;
         double AmountOwed;
         String Category;
         String Title;
         int TabId;
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

        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " ASC";
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
               tabId = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TABID));
               TabId = Integer.parseInt(tabId);
               Date = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));
               tempTab = new Tab(UserOwed, UserOwing, AmountOwed, Category, Title);
               tempTab.setTabId(TabId);
               tempTab.setDate(Date);
               Tabs.add(tempTab);
               System.out.println("Tab Created: " + tempTab);
                System.out.println("Amount: "+AmountOwed);

                TextView vault = (TextView)findViewById(R.id.vault);
                out +=tempTab.toString();
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

}
