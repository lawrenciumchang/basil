package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Act_TabsPage extends Activity {
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_tabs_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Current User from Tabs page: " + userName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
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
                i = new Intent(this, Act_BudgetBuddy.class);
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
        i = new Intent(this, Act_BudgetBuddy.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

    public void equalSplit(View view){
        Intent intent = new Intent(this, Act_EqualSplitPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    public void customizedSplit(View view){
        Toast.makeText(getApplicationContext(), "Will be added soon :^)", Toast.LENGTH_SHORT).show();
    }

    public void iou(View view){
        Intent intent = new Intent(this, Act_IouPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    public void viewTabs(View view){
        Intent intent = new Intent(this, Act_TabVault.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }
}
