package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


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

    SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(this);
    ArrayList<Tab> Tabs = new ArrayList<Tab>();
    String[] people;
    double[] prices;

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


            String spinID = "";
            String subID ="";
            String tipID ="";
            double t;
            double s;
            // for(int j = 0; j < numToCreate; j++){
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

            String spinID = "";
            //String subID ="";
            String tipID ="";
            //double s;
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
            //output += "Total Tax (" + dec.format((tax-1)*100) + "%): $" + dec.format(taxSum) + "<br/>";
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
        Tabs = createTabs(prices, people, category, title);
        addTabsToDatabase();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }

    private ArrayList<Tab> createTabs(double[] prices, String[] people, String category, String title){

        ArrayList<Tab> Tabs = new ArrayList<Tab>();
        int num = people.length;


        for(int i = 1; i < num; i++ ) {
            IouRequestTab.getInstance().createTab(people[0], people[i], prices[i], category, title);
            Tabs.add(IouRequestTab.getInstance().getCreatedTab());
        }

        return Tabs;

    }

    private void addTabsToDatabase(){
        SQLiteDatabase db = tabDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String tabId;
        String date;
        int temp;
        String tempAmount; // = Double.parseDouble(amount);



        for(Tab T : Tabs) {
            DecimalFormat dec = new DecimalFormat("0.00");
            temp = T.getTabId();
            tabId = Integer.toString(temp);
            date = T.getDate();
            tempAmount = dec.format(T.getAmountOwed());

            System.out.println("Amount Saved: "+tempAmount);

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWED, userName);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_USEROWING, T.getUserOwing());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AMOUNT, tempAmount);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIES, category);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TABID, tabId);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);
            long newRowId = db.insert(
                    FeedReaderContract.FeedEntry.TABLE_NAME_TABS,
                    FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                    values);
        }
    }
}
