package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Act_SignInPage extends Activity {
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__sign_in_page);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }
        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Current User from Log In page: " + userName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__sign_in_page, menu);
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

    public void userName(View view){
//        Intent intent = new Intent(this, Act_BudgetBuddy.class);
        final EditText editText = (EditText)findViewById(R.id.editText3);
        String userName = editText.getText().toString();
//        intent.putExtra(PASS_CURRENT_USER, userName);

        if(userName.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter your name.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Welcome, " + userName + "!");
            builder.setCancelable(false);
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    launchIntent();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
//        startActivity(intent);
    }

    void launchIntent(){
        Intent intent = new Intent(this, Act_BudgetBuddy.class);
        EditText editText = (EditText)findViewById(R.id.editText3);
        String userName = editText.getText().toString();
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }
}
