package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Act_CustomSplitPeoplePage extends Activity {

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String title;
    String category;
    String amount;
    String number;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__custom_split_people_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_EqualSplitPage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPage.PASS_NUMBER);
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Current User from Custom Split Manage Transactions page: " + userName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__custom_split_people_page, menu);
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
                i = new Intent(this, Act_CustomSplitPage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_NUMBER, number);
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
        i = new Intent(this, Act_CustomSplitPage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_NUMBER, number);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }


}
