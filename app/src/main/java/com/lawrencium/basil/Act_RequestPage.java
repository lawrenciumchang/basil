package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



public class Act_RequestPage extends Activity {

    private String title;
    private String category ;
    private String amount ;
    private String user;
    private String userName;
    private final SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(this);

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER = "com.lawrencium.basil.USER";

    /**
     * Prints a confirmation of how much another user owes current user.
     * @param savedInstanceState
     */
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

        setContentView(R.layout.activity_act__request_page);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }
        TextView displayUser1 = (TextView)findViewById(R.id.requestMessage1);
        displayUser1.setText(userName + ", " + user + " owes you");

        TextView displayUser2 = (TextView)findViewById(R.id.requestMessage2);
        displayUser2.setText("$" + amount + " for " + title + " (" + category + ").");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__request_page, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_IouPage.class
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
                i = new Intent(this, Act_IouPage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_USER, user);
                i.putExtra(PASS_CURRENT_USER, userName);
                startActivityForResult(i, 0);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Takes user to previous page.
     */
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
        finish();
    }

    /**
     * Confirms the payment.
     * Takes user to the Tabs home page so that the transaction can no longer be modified.
     * Saves the information to the Tabs database.
     * @param view
     */
    public void confirmRequest(View view){
        SQLiteDatabase db = tabDbHelper.getWritableDatabase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your request has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                launchIntent();
            }});
        double tempAmount = Double.parseDouble(amount);

        System.out.println("Temp Amount: "+tempAmount);

        // Create new corresponding transaction
        long newTransactionId = Budget.newTransaction(db, title, amount, category);
        // Store tab in db
        Tab tempTab = new Tab(userName, user, tempAmount, category, title, newTransactionId);
        long tabId = tempTab.newTab(this);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Sends user to the Tabs home page.
     */
    void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
        finish();
    }
}
