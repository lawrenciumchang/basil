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

    String title;
    String category;
    String amount;
    String number;
    String userName;
    String user2;
    Bundle bun;
    String taxFlag;
    String output = "";

    int num;
    int numToCreate;

    int bundleSpinID;
    int bundleSubID;
    int bundleTipID;

    double tipSum = 0;
    double subtotalSum = 0;
    double tax = 0;
    double taxSum = 0;

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER2 = "com.lawrencium.basil.USER2";
    public final static String PASS_TAX_FLAG = "com.lawrencium.basil.TAXFLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__custom_split_confirm_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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

        bundleSpinID = 101;
        bundleSubID = 201;
        bundleTipID = 301;

//        System.out.println("Bundle 1: " + bun.getString("100") + ", " + bun.getString("200") + ", " + bun.getString("300"));
//        System.out.println("Bundle 2: " + bun.getString("101") + ", " + bun.getString("201") + ", " + bun.getString("301"));

        //for testing
//        for(int i = 0; i < numToCreate; i++){
//            bundleSpinID += 1;
//            bundleSubID += 1;
//            bundleTipID += 1;
//
//            String spinID = Integer.toString(bundleSpinID);
//            String subID = Integer.toString(bundleSubID);
//            String tipID = Integer.toString(bundleTipID);
//
//            System.out.println("Bundle: User - " + bun.getString(spinID) + " Subtotal - " + bun.getString(subID) + " Tip - " + bun.getString(tipID));
//        }

        //no tax, nothing to calculate, print info out to user
        if(taxFlag.matches("notax")){
            TextView display = (TextView)findViewById(R.id.customDisplay);

            //add user 1 and 2 to string first
            //check if no tip was added, make it display "0"
            if(bun.getString("300").matches("")){
                bun.putString("300", "0.00");
            }
            output += bun.getString("100") + ": $" + bun.getString("200") + ", with $" + bun.getString("300") + " tip" + "<br/>";
            double t1 = Double.parseDouble(bun.getString("300"));
            tipSum += t1;

            if(bun.getString("301").matches("")){
                bun.putString("301", "0.00");
            }
            output += bun.getString("101") + ": $" + bun.getString("201") + ", with $" + bun.getString("301") + " tip" + "<br/>";
            double t2 = Double.parseDouble(bun.getString("301"));
            tipSum += t2;

            for(int j = 0; j < numToCreate; j++){
                bundleSpinID += 1;
                bundleSubID += 1;
                bundleTipID += 1;

                String spinID = Integer.toString(bundleSpinID);
                String subID = Integer.toString(bundleSubID);
                String tipID = Integer.toString(bundleTipID);

                if(bun.getString(tipID).matches("")){
                    bun.putString(tipID, "0.00");
                }
                output += bun.getString(spinID) + ": $" + bun.getString(subID) + ", with $" + bun.getString(tipID) + " tip" + "<br/>";

                double t = Double.parseDouble(bun.getString(tipID));
                tipSum += t;
            }

//            if(tipSum == 0){
//                output += "Total Tip: $0" + "<br/>";
//            }
//            else {
                output += "Total Tip: $" + dec.format(tipSum) + "<br/>";
//            }

            double amnt = Double.parseDouble(amount);
            amnt += tipSum;
            output += "<b>Total</b>: $" + dec.format(amnt);

            display.setText(Html.fromHtml(output));
        }
        else if(taxFlag.matches("tax")){
            TextView display = (TextView)findViewById(R.id.customDisplay);

            //calculate tax
            double amnt = Double.parseDouble(amount);

            //add user 1 and 2's subtotals to sum first
            double s1 = Double.parseDouble(bun.getString("200"));
            subtotalSum += s1;

            double s2 = Double.parseDouble(bun.getString("201"));
            subtotalSum += s2;

            for(int k = 0; k < numToCreate; k++){
                bundleSubID += 1;
                String subID = Integer.toString(bundleSubID);
                double s = Double.parseDouble(bun.getString(subID));
                subtotalSum += s;
            }

            // in 1.xx format
            tax = amnt / subtotalSum;

            //reset bundleSubID
            bundleSubID = 201;

            //add user 1 and 2 to string first
            //check if no tip was added, make it display "0"
            if(bun.getString("300").matches("")){
                bun.putString("300", "0");
            }
            double tax1 = s1*(tax-1);
            output += bun.getString("100") + ": $" + bun.getString("200") + ", with $" + dec.format(tax1) + " tax and $" + bun.getString("300") + " tip" + "<br/>";
            int t1 = Integer.parseInt(bun.getString("300"));
            tipSum += t1;
            double sub1 = Double.parseDouble(bun.getString("200"));
            double tx1 = sub1*(tax-1);
            taxSum += tx1;

            if(bun.getString("301").matches("")){
                bun.putString("301", "0");
            }
            double tax2 = s2*(tax-1);
            output += bun.getString("101") + ": $" + bun.getString("201") + ", with $" + dec.format(tax2) + " tax and $" + bun.getString("301") + " tip" + "<br/>";
            int t2 = Integer.parseInt(bun.getString("301"));
            tipSum += t2;
            double sub2 = Double.parseDouble(bun.getString("201"));
            double tx2 = sub2*(tax-1);
            taxSum += tx2;

            for(int l = 0; l < numToCreate; l++){
                bundleSpinID += 1;
                bundleSubID += 1;
                bundleTipID += 1;

                String spinID = Integer.toString(bundleSpinID);
                String subID = Integer.toString(bundleSubID);
                String tipID = Integer.toString(bundleTipID);

                if(bun.getString(tipID).matches("")){
                    bun.putString(tipID, "0");
                }
                double s = Double.parseDouble(bun.getString(subID));
                double tx = s*(tax-1);
                output += bun.getString(spinID) + ": $" + bun.getString(subID) + ", with $" + dec.format(tx) + " tax and $" + bun.getString(tipID) + " tip" + "<br/>";

                double t = Double.parseDouble(bun.getString(tipID));
                tipSum += t;
                taxSum += tx;
            }

            output += "Total Tax (" + dec.format((tax-1)*100) + "%): " + dec.format(taxSum) + "<br/>";

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
                break;
            default:
                break;
        }
        return true;
    }

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
    }

    public void confirmCustom(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your request has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchIntent();
            }});
//        addTabsToDatabase();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

}
