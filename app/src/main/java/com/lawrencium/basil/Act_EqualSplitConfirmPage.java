package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.Random;


public class Act_EqualSplitConfirmPage extends Activity {


    private String title;
    private String category;
    private String amount;
    private String number;
    private String userName;
    private String user2;
    private Bundle b;
    private String[] ppl;
    private double[] pricesSplit;

    private final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    private final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    private final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    private final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER2 = "com.lawrencium.basil.USER2";

    private int num;

    /**
     * Prints on screen a confirmation page, showing the user the transaction totals for each user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__equal_split_confirm_page);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_EqualSplitPage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPage.PASS_NUMBER);
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        user2 = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_USER2);

        b = intent.getExtras();

        num = Integer.parseInt(number);

        double[] tempArr;
        String[] tempPeople;
        String testString;
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
            TextView display = (TextView) findViewById(R.id.equalDisplay);

            tempSet = ranPerson(num);
            tempArr = priceSplit(num, amount, tempSet);
            System.out.println("Random set: "+tempArr);
            testString = paymentOut(tempArr, tempPeople);
            System.out.println("Who Owes: "+testString);
            display.setText(testString+ " for " + title + " (" + category + ").");
            ppl = tempPeople;
            pricesSplit = tempArr;
        }
        //for 3 people or more total
        else{
            tempPeople = getPeople(b, num);
            DecimalFormat dec = new DecimalFormat("0.00");

            TextView display = (TextView) findViewById(R.id.equalDisplay);
            tempSet = ranPerson(num);
            tempArr = priceSplit(num, amount,tempSet);
            System.out.println("Random set: "+tempArr);

            String t = dec.format(tempArr[1]);
            if(priceEqualCheck(tempArr))
                display.setText(getNames(tempPeople) + " each owe you $" + t + " for " + title + " (" + category + ").");
            else
                display.setText(paymentOut(tempArr, tempPeople)+ " for " + title + " (" + category + ").");

            ppl = tempPeople;
            pricesSplit = tempArr;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__equal_split_confirm_page, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_EqualSplitPeoplePage.class
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
                i = new Intent(this, Act_EqualSplitPeoplePage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_NUMBER, number);
                i.putExtra(PASS_CURRENT_USER, userName);
                i.putExtra(PASS_USER2, user2);
                i.putExtras(b);
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
        i = new Intent(this, Act_EqualSplitPeoplePage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_NUMBER, number);
        i.putExtra(PASS_CURRENT_USER, userName);
        i.putExtra(PASS_USER2, user2);
        i.putExtras(b);
        startActivityForResult(i, 0);
        finish();
    }

    /**
     * Confirms the payment.
     * Takes user to the Tabs home page so that the transaction can no longer be modified.
     * Saves the information to the Tabs database.
     * @param view
     */
    public void confirmEqual(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your request has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchIntent();
            }});

        Tab.addTabsToDatabase(this, pricesSplit, ppl, category, title, amount);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Sends user to Tabs home page.
     */
    void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PASS_CURRENT_USER, userName);
        startActivity(intent);
        finish();
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
        String temp;

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
