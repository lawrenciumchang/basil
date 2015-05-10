package com.lawrencium.basil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;




public class Act_TabsPage extends Activity {
    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    private String userName;
    private final SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(this);

    /**
     * Creates the layout for the Tabs home page.
     * Link to pages:   Tabs Vault
     *                  Equal Split
     *                  Custom Split
     *                  IOU
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
        SharedPreferences prefs = getSharedPreferences(Act_BudgetBuddy.class.getSimpleName(),
                Context.MODE_PRIVATE);
        userName = prefs.getString("user_name", "");

        System.out.println("Current User from Tabs page: " + userName);

        TextView recent = (TextView)findViewById(R.id.recent1);

        String Amount;
        Tab tempTab;
        String UserOwed;
        String UserOwing;
        double AmountOwed;
        String Category;
        String Title;

        String Date;
        String out = "";

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
        int limitThree = 0;
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

                out += tempTab.toString() + "\n";
                recent.setText(out);
                limitThree += 1;
                if(limitThree == 3){
                    break;
                }
            } while (c.moveToNext());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_BudgetBuddy.class
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
                i = new Intent(this, Act_BudgetBuddy.class);
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
//    @Override
//    public void onBackPressed() {
//        Intent i;
//        i = new Intent(this, Act_BudgetBuddy.class);
//        i.putExtra(PASS_CURRENT_USER, userName);
//        startActivityForResult(i, 0);
//    }

    /**
     * Takes user to Equal Split page.
     * @param view
     */
    public void equalSplit(View view){
        Intent intent = new Intent(this, Act_EqualSplitPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    /**
     * Takes user to Custom Split page.
     * @param view
     */
    public void customSplit(View view){
        Intent intent = new Intent(this, Act_CustomSplitPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    /**
     * Takes user to IOU page.
     * @param view
     */
    public void iou(View view){
        Intent intent = new Intent(this, Act_IouPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    /**
     * Takes user to Tabs Vault page.
     * @param view
     */
    public void viewTabs(View view){
        Intent intent = new Intent(this, Act_TabVault.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }
}
