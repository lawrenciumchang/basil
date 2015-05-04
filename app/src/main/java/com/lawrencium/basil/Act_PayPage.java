package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.lawrencium.basil.james.backend.messaging.Messaging;

import java.io.IOException;


public class Act_PayPage extends Activity {

    private static Messaging msg = null;
    String title;
    String category;
    String amount;
    String user;
    String userName;
    SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(this);

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER = "com.lawrencium.basil.USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_IouPage.PASS_TITLE);
        category = intent.getStringExtra(Act_IouPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_IouPage.PASS_AMOUNT);
        user = intent.getStringExtra(Act_IouPage.PASS_USER);
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Title: " + title);
        System.out.println("Category: " + category);
        System.out.println("Amount: " + amount);
        System.out.println("User: " + user);
        System.out.println("Current User: " + userName);

        setContentView(R.layout.activity_act__pay_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView displayUser1 = (TextView)findViewById(R.id.payMessage1);
        displayUser1.setText(userName + ", you owe " + user);

        TextView displayUser2 = (TextView)findViewById(R.id.payMessage2);
        displayUser2.setText("$" + amount + " for " + title + " (" + category + ").");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__pay_page, menu);
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
                i = new Intent(this, Act_IouPage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_USER, user);
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
        i = new Intent(this, Act_IouPage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_USER, user);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

    public void confirmPay(View view){
        SQLiteDatabase db = tabDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String tabId;
        String date;
        int temp;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your payment has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                launchIntent();
            }});

        //need to format double to look like currency
        double tempAmount = Double.parseDouble(amount);
        tempAmount *= -1;
        amount = Double.toString(tempAmount);
        //System.out.println("Temp Amount: "+tempAmount);

        Tab tempTab = new Tab(user, userName, tempAmount, category, title);
        tempAmount *= -1;
        amount = Double.toString(tempAmount);
        Budget.newTransaction(db, title, amount, category);
        /*temp = IouRequestTab.getInstance().getCreatedTab().getTabId();
        tabId = Integer.toString(temp);
        date = IouRequestTab.getInstance().getCreatedTab().getDate();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWED, user);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWING, userName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT, amount);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIES, category);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TABID, tabId);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);

        long newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                values);*/

        // Get email of user
        String owedEmail = "";
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL
        };
        String filter = FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND + " = \'" +
                user + "\'";
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
            owedEmail = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL));
        }
        String owedTabId = getIntent().getExtras().getString("OWED_TABID");
        new GcmSendAsyncTask(this, userName, owedEmail,  userName+tempTab.sendTabMsg()+owedTabId+"**").execute();

//        if(newRowId >= 0) {
            AlertDialog dialog = builder.create();
            dialog.show();
//        }

    }

    public void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }
}
