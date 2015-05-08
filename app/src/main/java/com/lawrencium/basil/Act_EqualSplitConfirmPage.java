package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;


public class Act_EqualSplitConfirmPage extends Activity {


    String title;
    String category;
    String amount;
    String number;
    String userName;
    String user2;
    Bundle b;
    String users = "";

    SQLiteDbHelper tabDbHelper = new SQLiteDbHelper(this);

    ArrayList<Tab> Tabs = new ArrayList<Tab>();
    String[] ppl;
    double[] pricesSPlit;

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER2 = "com.lawrencium.basil.USER2";

    int num;
    int numToCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__equal_split_confirm_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_EqualSplitPage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPage.PASS_NUMBER);
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        user2 = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_USER2);

        b = intent.getExtras();

        num = Integer.parseInt(number);
        numToCreate = num - 2;

        double total = Double.parseDouble(amount);
        double[] tempArr;
        String[] tempPeople;
        String testString ="";
        LinkedHashSet tempSet;

        System.out.println("Current User from Equal Split Confirmation: " + userName);
        System.out.println("User 2: " + user2);

        for(int i = 0; i < num; i++){
            String id = Integer.toString(i);
            System.out.println("Bundle: " + b.getString(id));
        }

        //for 2 people total
        if(num == 2) {
            tempPeople = getPeople(b, num);
            Double n = (double) num;
            total = total/n;
            DecimalFormat dec = new DecimalFormat("0.00");
            String t = dec.format(total);
            TextView display = (TextView) findViewById(R.id.equalDisplay);

            tempSet=ranPerson(num);
            tempArr = priceSplit(num, amount, tempSet);
            System.out.println("Random set: "+tempArr);
            testString = paymentOut(tempArr, tempPeople);
            System.out.println("Who Owes: "+testString);
            display.setText(testString+ " for " + title + " (" + category + ").");
            //Tabs = createTabs(tempArr, tempPeople, category, title);
            ppl = tempPeople;
            pricesSPlit = tempArr;
        }
        //for 3 people or more total
        else{
            tempPeople = getPeople(b, num);
            Double n = (double) num;
            total = total/n;
            DecimalFormat dec = new DecimalFormat("0.00");

            TextView display = (TextView) findViewById(R.id.equalDisplay);

            //users += " and " + b.getString("3");
            tempSet=ranPerson(num);
            tempArr = priceSplit(num, amount,tempSet);
            System.out.println("Random set: "+tempArr);


            String t = dec.format(tempArr[1]);
            if(priceEqualCheck(tempArr))
                display.setText(getNames(tempPeople) + " each owe you $" + t + " for " + title + " (" + category + ").");
            else
                display.setText(paymentOut(tempArr, tempPeople)+ " for " + title + " (" + category + ").");

            ppl = tempPeople;
            pricesSPlit = tempArr;
            //Tabs = createTabs(tempArr, tempPeople, category, title);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__equal_split_confirm_page, menu);
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
                i = new Intent(this, Act_EqualSplitPeoplePage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_NUMBER, number);
                i.putExtra(PASS_CURRENT_USER, userName);
                i.putExtra(PASS_USER2, user2);
                i.putExtras(b);
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
        i = new Intent(this, Act_EqualSplitPeoplePage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_NUMBER, number);
        i.putExtra(PASS_CURRENT_USER, userName);
        i.putExtra(PASS_USER2, user2);
        i.putExtras(b);
        startActivityForResult(i, 0);
    }

    public void confirmEqual(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your request has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchIntent();
            }});

        Tab.addTabsToDatabase(this, pricesSPlit, ppl, category, title, amount);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
    }



    private LinkedHashSet<Integer> ranPerson(int numPeople){

        LinkedHashSet<Integer> tempRan = new LinkedHashSet<Integer>();
        Random ran = new Random();
        int temp;

        while(tempRan.size() < numPeople){
            temp = ran.nextInt(numPeople);
            tempRan.add(temp);
        }
        return tempRan;
    }

    //ToDo: Handle the edge cases i.e. divide by zero
    private double[] priceSplit(int numPeople, String price, LinkedHashSet<Integer> tempSet){
        //LinkedHashSet<Integer> tempSet=ranPerson(numPeople);
        System.out.println("Random People: "+tempSet);

        String tester = "Prices: ";

        double[] tempArr = new double[tempSet.size()];
        Integer temp = numPeople;
        double total = Double.parseDouble(price);
        System.out.println("Number of People: "+temp+" Price: "+price+" Price Double: "+total);
        BigDecimal bdPrice = new BigDecimal(price);

        BigDecimal bdNumPeople = new BigDecimal(temp.toString());

        BigDecimal bdTempD = bdPrice.divide(bdNumPeople, 2, BigDecimal.ROUND_DOWN);
        double equalPrice = bdTempD.doubleValue();
        System.out.println("Equal Price: "+equalPrice);

        //Testing remainder
        BigDecimal bdTempR = bdPrice.remainder(bdTempD);

        double tempRem = bdTempR.doubleValue()*100;
        int rem = (int)tempRem;

//        System.out.println("Divide: "+bdTempD);
//        System.out.println("Remainder: "+bdTempR);
//        System.out.println("Int Remainder: "+rem);

        for(int pricePay : tempSet){
            if(rem>0){
                tempArr[pricePay] = equalPrice + .01;
                rem--;
            }
            else
                tempArr[pricePay] = equalPrice;
            tester += tempArr[pricePay]+" ";
        }
        System.out.println(tester);
        return tempArr;
    }


    private String paymentOut(double[] prices, String[] people){
        String payments ="";
        int num = people.length;
        DecimalFormat dec = new DecimalFormat("0.00");

        if(num == 2){
            payments = people[1]+" owes you $"+dec.format(prices[1]);
        }
        else{
            for(int i = 1; i < num-1; i++){
                payments += people[i]+" owes you $"+dec.format(prices[i])+", ";
            }
            payments += "and "+people[num-1]+" owes you $"+dec.format(prices[num-1]);
        }

        return payments;

    }

    private String getNames(String[] names){
        String nameList ="";
        int num = names.length;
        DecimalFormat dec = new DecimalFormat("0.00");

        if(num == 3){
            nameList = names[1]+" and "+names[2];
        }
        else{
            for(int i = 1; i < num-1; i++){
                nameList += names[i]+", ";
            }
            nameList += "and "+names[num-1];
        }

        return nameList;
    }

    private String[] getPeople(Bundle b, int numPpl){
        String[] people = new String[numPpl];
        String temp ="";

        String test = "People in Bundle: ";

        for(int i =0; i < numPpl; i++){
            temp = i+"";
            people[i] = b.getString(temp);
           test += people[i]+" ";
        }
        System.out.println(test);
        return people;
    }

    private boolean priceEqualCheck(double[] prices){
        if(prices.length == 0 || prices.length == 1)
            return true;
        double tempPrice = prices[0];
        for(int i = 1; i < prices.length; i++){
            if(tempPrice != prices[i])
                return false;
        }
        return true;
    }


}
