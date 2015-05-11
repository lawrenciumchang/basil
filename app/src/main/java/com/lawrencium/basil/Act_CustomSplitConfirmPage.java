package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;


public class Act_CustomSplitConfirmPage extends Activity {

    private String title;
    private String category;
    private String amount;
    private String number;
    private String userName;
    private String user2;
    private Bundle bun;
    private String taxFlag;
    private String output = "";

    private int num;
    private int numToCreate;

    private int bundleSpinID;
    private int bundleSubID;
    private int bundleTipID;

    private double tipSum = 0;
    private double subtotalSum = 0;
    private double tax = 0;
    private double taxSum = 0;

    private String[] people;
    private double[] prices;

    private final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    private final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    private final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    private final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    private final static String PASS_USER2 = "com.lawrencium.basil.USER2";
    public final static String PASS_TAX_FLAG = "com.lawrencium.basil.TAXFLAG";

    /**
     * Prints on screen a confirmation page, showing the user the transaction totals for each user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__custom_split_confirm_page);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_TITLE);
        category = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_NUMBER);
        userName = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_CURRENT_USER);
        user2 = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_USER2);
        taxFlag = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_TAX_FLAG);

        bun = intent.getExtras();
        DecimalFormat dec = new DecimalFormat("0.00");



        System.out.println("Title: " + title);
        System.out.println("Category: " + category);
        System.out.println("Amount: " + amount);
        System.out.println("Number of people: " + number);
        System.out.println("Current User from Custom Split Confirm Page: " + userName);
        System.out.println("User 2: " + user2);
        System.out.println("Tax Flag: " + taxFlag);

        num = Integer.parseInt(number);
        numToCreate = num - 2;
        people = new String[num];
        prices = new double[num];

        bundleSpinID = 99;
        bundleSubID = 199;
        bundleTipID = 299;


        //no tax, nothing to calculate, print info out to user
        if(taxFlag.matches("notax")){
            TextView display = (TextView)findViewById(R.id.customDisplay);

            //add user 1 and 2 to string first
            //check if no tip was added, make it display "0"

            String spinID;
            String subID;
            String tipID;
            double t;
            double s;

            for(int j = 0; j < num; j++){
                bundleSpinID += 1;
                bundleSubID += 1;
                bundleTipID += 1;

                spinID = Integer.toString(bundleSpinID);
                subID = Integer.toString(bundleSubID);
                tipID = Integer.toString(bundleTipID);

                output += bun.getString(spinID) + ": $" + bun.getString(subID) + ", with $" + bun.getString(tipID) + " tip" + "<br/>";
                subID = Integer.toString(bundleSubID);
                s = Double.parseDouble(bun.getString(subID));
                subtotalSum += s;
                t = Double.parseDouble(bun.getString(tipID));
                tipSum += t;

                people[j] = bun.getString(spinID);
                prices[j] = s+t;
                System.out.println(people[j]+" owes "+prices[j]);
            }

            output += "Total Tip: $" + dec.format(tipSum) + "<br/>";


            double amnt = Double.parseDouble(amount);
            amnt += tipSum;
            output += "Subtotal: $" + dec.format(subtotalSum)+"<br/>";
            output += "<b>Total</b>: $" + dec.format(amnt);

            display.setText(Html.fromHtml(output));
        }
        else if(taxFlag.matches("tax")){
            TextView display = (TextView)findViewById(R.id.customDisplay);

            //calculate tax
            double amnt = Double.parseDouble(amount);

            //add user 1 and 2's subtotals to sum first

            String subID;
            double s;

            for(int k = 0; k < num; k++){
                bundleSubID += 1;
                subID = Integer.toString(bundleSubID);
                s = Double.parseDouble(bun.getString(subID));
                subtotalSum += s;
            }

            // in 1.xx format
            tax = amnt / subtotalSum;

            //reset bundleSubID
            bundleSubID = 199;

            //add user 1 and 2 to string first

            String spinID;
            String tipID;
            double tx;
            double t;

            for(int l = 0; l < num; l++){
                bundleSpinID += 1;
                bundleSubID += 1;
                bundleTipID += 1;

                spinID = Integer.toString(bundleSpinID);
                subID = Integer.toString(bundleSubID);
                tipID = Integer.toString(bundleTipID);

                s = Double.parseDouble(bun.getString(subID));
                tx = s*(tax-1);
                output += bun.getString(spinID) + ": $" + bun.getString(subID) + ", with $" + dec.format(tx) + " tax and $" + bun.getString(tipID) + " tip" + "<br/>";

                t = Double.parseDouble(bun.getString(tipID));
                tipSum += t;
                taxSum += tx;
                people[l] = bun.getString(spinID);
                prices[l] = s+t+tx;
                System.out.println(people[l]+" owes "+prices[l]);
            }
            DecimalFormat perc = new DecimalFormat("0.##%");
            System.out.println("Tax Percentage: "+((tax-1)*100));
            output += "Subtotal: $" + dec.format(subtotalSum)+"<br/>";

            output += "Total Tax (" + perc.format((tax-1)) + "): $" + dec.format(taxSum) + "<br/>";

            output += "Total Tip: $" + dec.format(tipSum) + "<br/>";

            double totalAmount = Double.parseDouble(amount);
            totalAmount += tipSum;
            output += "<b>Total</b>: $" + dec.format(totalAmount);

            display.setText(Html.fromHtml(output));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__custom_confirm_page, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_CustomSplitPeoplePage.class
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
                i = new Intent(this, Act_CustomSplitPeoplePage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_NUMBER, number);
                i.putExtra(PASS_CURRENT_USER, userName);
                i.putExtra(PASS_USER2, user2);
                i.putExtra(PASS_TAX_FLAG, taxFlag);
                i.putExtras(bun);
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
        i = new Intent(this, Act_CustomSplitPeoplePage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_NUMBER, number);
        i.putExtra(PASS_CURRENT_USER, userName);
        i.putExtra(PASS_USER2, user2);
        i.putExtra(PASS_TAX_FLAG, taxFlag);
        i.putExtras(bun);
        startActivityForResult(i, 0);
        finish();
    }

    /**
     * Confirms the payment.
     * Takes user to the Tabs home page so that the transaction can no longer be modified.
     * Saves the information to the Tabs database.
     * @param view
     */
    public void confirmCustom(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your request has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchIntent();
            }});
        Tab.addTabsToDatabase(this, prices, people, category, title, amount);
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
