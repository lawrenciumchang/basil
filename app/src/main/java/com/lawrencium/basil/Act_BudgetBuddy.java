package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Act_BudgetBuddy extends Activity {
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_budget_buddy);

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Current User: " + userName);

        //logged out
        if(userName == null){
            Button button2 = (Button)findViewById(R.id.button6);
            button2.setVisibility(View.GONE);

            TextView loggedInAs = (TextView)findViewById(R.id.loggedInAs);
            loggedInAs.setVisibility(View.GONE);
        }

        //logged in
        else if(userName != null){
            Button button = (Button)findViewById(R.id.button);
            button.setVisibility(View.GONE);

            TextView loggedInAs = (TextView)findViewById(R.id.loggedInAs);
            loggedInAs.setText("Logged in as " + userName);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_buddy, menu);
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
            Toast.makeText(this,"What would you like to see in the settings?", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        Intent i;
//        i = new Intent(this, Act_BudgetBuddy.class);
//        i.putExtra(PASS_CURRENT_USER, userName);
//        startActivityForResult(i, 0);
    }

    public void logIn(View view){
//        Intent intent = new Intent(this, login.class);
//        startActivity(intent);
    }

    public void signIn(View view){
        Intent intent = new Intent(this, Act_SignInPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    public void signOut(View view){
        userName = null;
        Intent i;
        i = new Intent(this, Act_BudgetBuddy.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
        Toast.makeText(getApplicationContext(), "You have been successfully logged out.", Toast.LENGTH_SHORT).show();
    }

    public void budgetView(View view){
        if(userName == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please log in before managing your finances.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            Toast.makeText(getApplicationContext(), "This is Evan and Annie's job!", Toast.LENGTH_SHORT).show();
        }
    }

    public void tabsView(View view){
        if(userName == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please log in before managing your finances.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Intent intent = new Intent(this, Act_TabsPage.class);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

    public void googleSignIn(View v){

        Intent intent = new Intent(this, Act_LoginPage.class);
        startActivity(intent);

    }
}
