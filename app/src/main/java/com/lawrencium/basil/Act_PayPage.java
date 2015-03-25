package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Act_PayPage extends Activity {
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String title;
    String category;
    String amount;
    String user;
    String userName;


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


        //this isn't working..
//        TextView displayTitle = (TextView)findViewById(R.id.textView3);
//        displayTitle.setText(title);

        setContentView(R.layout.activity_act__pay_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
                i.putExtra(PASS_CURRENT_USER, userName);
                startActivityForResult(i, 0);
                break;
            default:
                break;
        }
        return true;
    }

    public void confirmPay(View view){
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

        TabVault.getInstance().getTempIou().createTab(user, userName, tempAmount, category, title);

        if(TabVault.getInstance().addTab(TabVault.getInstance().getTempIou().getCreatedTab())) {
            System.out.println("Amount: " + tempAmount);
            System.out.println("Tab ID: " + TabVault.getInstance().getTempIou().getCreatedTab().getTabId());
            System.out.println("Vault: " + TabVault.getInstance().getTabs());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }
}
