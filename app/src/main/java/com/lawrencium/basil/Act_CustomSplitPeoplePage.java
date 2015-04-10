package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.text.DecimalFormat;


public class Act_CustomSplitPeoplePage extends Activity {

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER2 = "com.lawrencium.basil.USER2";
    public final static String PASS_TAX_FLAG = "com.lawrencium.basil.TAXFLAG";

    String title;
    String category;
    String amount;
    String number;
    String userName;
    String user2;

    int numToCreate;
    double total;

    Bundle bun;

    int spinID;
    int subID;
    int tipID;
    int checkSpinID;
    int checkSubID;
    int bundleSpinID;
    int bundleSubID;
    int bundleTipID;
    int calcSubID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__custom_split_people_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_EqualSplitPage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPage.PASS_NUMBER);
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        user2 = intent.getStringExtra(Act_EqualSplitConfirmPage.PASS_USER2);
        bun = intent.getExtras();

        numToCreate = Integer.parseInt(number);
        numToCreate = numToCreate - 2;

        System.out.println("Current User from Custom Split Manage Transactions page: " + userName);
        System.out.println("Title: " + title);
        System.out.println("Category: " + category);
        System.out.println("Amount: " + amount);
        System.out.println("Number of people: " + number);

        System.out.println("bundle testing: " + bun.getString("100") + ", " + bun.getString("200") + ", " + bun.get("300"));

        //used for creating new objects dynamically
        LinearLayout ll = (LinearLayout)findViewById(R.id.transactions);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //creates user 1 and 2 and adds information if back button was pressed to preserve information
        createUserDropdown();
        if(bun.getString("200") != null){
            EditText s1 = (EditText)findViewById(R.id.userSubtotal);
            s1.setText(bun.getString("200"));
        }
        if(bun.getString("300") != null){
            EditText t1 = (EditText)findViewById(R.id.userTip);
            t1.setText(bun.getString("300"));
        }

        createUserDropdown2();
        Spinner user2Set = (Spinner)findViewById(R.id.user2);
        String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        if(user2 != null) {
            for (int i = 0; i < items2.length; i++) {
                if (user2.equals(items2[i])) {
                    user2Set.setSelection(i);
                }
            }
        }
        if(bun.getString("201") != null){
            EditText s2 = (EditText)findViewById(R.id.user2Subtotal);
            s2.setText(bun.getString("201"));
        }
        if(bun.getString("301") != null){
            EditText t2 = (EditText)findViewById(R.id.user2Tip);
            t2.setText(bun.getString("301"));
        }

        spinID = 101;
        subID = 201;
        tipID = 301;

        if(bun.getString("102") != null){
            for (int a = 0; a < numToCreate; a++) {
                spinID += 1;
                String idA = Integer.toString(spinID);
                for(int c = 0; c < items2.length; c++) {
                    if (bun.getString(idA).equals(items2[c])) {
                        Spinner userSet = createNewUserDropdown();
                        ll.addView(userSet, ll.getChildCount(), lp);
                        userSet.setId(spinID);
                        userSet.setSelection(c);
                    }
                }

                EditText createSub = createNewUserSubtotal();
                ll.addView(createSub, ll.getChildCount(), lp);
                subID += 1;
                String idB = Integer.toString(subID);
                createSub.setId(subID);
                createSub.setText(bun.getString(idB));

                EditText createTip = createNewUserTip();
                ll.addView(createTip, ll.getChildCount(), lp);
                tipID += 1;
                String idC = Integer.toString(tipID);
                createTip.setId(tipID);
                createTip.setText(bun.getString(idC));
            }
        }
        else {
            //dynamically create based on number of people in transaction, starting with user 3
            for (int i = 0; i < numToCreate; i++) {
                Spinner createDrop = createNewUserDropdown();
                ll.addView(createDrop, ll.getChildCount(), lp);
                spinID += 1;
                createDrop.setId(spinID);

                EditText createSub = createNewUserSubtotal();
                ll.addView(createSub, ll.getChildCount(), lp);
                subID += 1;
                createSub.setId(subID);

                EditText createTip = createNewUserTip();
                ll.addView(createTip, ll.getChildCount(), lp);
                tipID += 1;
                createTip.setId(tipID);
            }
        }
    }

    public void createUserDropdown(){
        Spinner user1 = (Spinner)findViewById(R.id.userName);
        String[] items = new String[]{userName};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        user1.setAdapter(adapter);
        user1.setClickable(false);
    }

    public void createUserDropdown2(){
        Spinner user2 = (Spinner)findViewById(R.id.user2);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user2.setAdapter(adapter);
    }

    public Spinner createNewUserDropdown(){
        Spinner newUser = new Spinner(this);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newUser.setAdapter(adapter);
        return newUser;
    }

    public EditText createNewUserSubtotal(){
        EditText newSubtotal = new EditText(this);
        newSubtotal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newSubtotal.setHint("Subtotal");
        return newSubtotal;
    }

    public EditText createNewUserTip(){
        EditText newTip = new EditText(this);
        newTip.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newTip.setHint("Tip (optional)");
        return newTip;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__custom_split_people_page, menu);
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
                i = new Intent(this, Act_CustomSplitPage.class);
                i.putExtra(PASS_TITLE, title);
                i.putExtra(PASS_CATEGORY, category);
                i.putExtra(PASS_AMOUNT, amount);
                i.putExtra(PASS_NUMBER, number);
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
        i = new Intent(this, Act_CustomSplitPage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_NUMBER, number);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

//    public void itemClicked() {
//        //code to check if this checkbox is checked!
//        CheckBox checkBox = (CheckBox)findViewById(R.id.tax);
//        if(checkBox.isChecked()){
//            checked = true;
//        }
//    }

    public void customPeopleNext(View view){

        checkSpinID = 101;
        checkSubID = 201;
        bundleSpinID = 101;
        bundleSubID = 201;
        bundleTipID = 301;
        calcSubID = 201;

        //if any fields checked below are not met, mark false
        boolean checkUsers = true;
        boolean checkSubs = true;
        boolean taxChecked = false;
        total = 0;

        Intent intent = new Intent(this, Act_CustomSplitConfirmPage.class);

        CheckBox checkBox = (CheckBox)findViewById(R.id.tax);
        if(checkBox.isChecked()){
            taxChecked = true;
        }

        //check that all user boxes are selected
        Spinner u2 = (Spinner)findViewById(R.id.user2);
        if(u2.getSelectedItem().toString().equals("Select User")){
            checkUsers = false;
        }
        for(int a = 0; a < numToCreate; a++){
            checkSpinID += 1;
            System.out.println("Check spin id: " + checkSpinID);
            Spinner spin = (Spinner)findViewById(checkSpinID);
            if(spin.getSelectedItem().toString().equals("Select User")){
                checkUsers = false;
            }
        }
        //check that all subtotal boxes are filled out
        EditText user1Subtotal = (EditText)findViewById(R.id.userSubtotal);
        if(user1Subtotal.getText().toString().equals("")){
            checkSubs = false;
        }
        EditText user2Subtotal = (EditText)findViewById(R.id.user2Subtotal);
        if(user2Subtotal.getText().toString().equals("")){
            checkSubs = false;
        }
        for(int b = 0; b < numToCreate; b++){
            checkSubID += 1;
            EditText txt = (EditText)findViewById(checkSubID);
            if(txt.getText().toString().equals("")){
                checkSubs = false;
            }
        }

//        Bundle bun = new Bundle();

        //pass user 1 and user 2 info into bundle
        bun.putString("100", userName);

        String user2 = u2.getSelectedItem().toString();
        bun.putString("101", user2);

        String sub1 = user1Subtotal.getText().toString();
        bun.putString("200", sub1);

        String sub2 = user2Subtotal.getText().toString();
        bun.putString("201", sub2);

        EditText userTip1 = (EditText)findViewById(R.id.userTip);
        String tip1 = userTip1.getText().toString();
        bun.putString("300", tip1);

        EditText userTip2 = (EditText)findViewById(R.id.user2Tip);
        String tip2 = userTip2.getText().toString();
        bun.putString("301", tip2);


        //BUNDLE
        //put user3 and onwards [name, subtotal, tip] into Bundle
        for(int i = 0; i < numToCreate; i++){
            bundleSpinID += 1;
            bundleSubID += 1;
            bundleTipID += 1;

            Spinner spin = (Spinner)findViewById(bundleSpinID);
            String spinID = Integer.toString(bundleSpinID);
            bun.putString(spinID , spin.getSelectedItem().toString());

            EditText sub = (EditText)findViewById(bundleSubID);
            String subID = Integer.toString(bundleSubID);
            bun.putString(subID, sub.getText().toString());

            EditText tip = (EditText)findViewById(bundleTipID);
            String tipID = Integer.toString(bundleTipID);
            bun.putString(tipID, tip.getText().toString());
        }


        //if check is true, carry out intent to next page
        //else, alert dialog
        if(checkUsers == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select users for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(checkSubs == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter amounts for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(checkUsers && checkSubs){
            //if tax box not checked, make sure subtotals add up to total amount
            if(taxChecked == false) {
                double u1Sub = Double.parseDouble(user1Subtotal.getText().toString());
                total += u1Sub;

                double u2Sub = Double.parseDouble(user2Subtotal.getText().toString());
                total += u2Sub;

                for(int b = 0; b < numToCreate; b++){
                    calcSubID += 1;
                    EditText txt = (EditText)findViewById(calcSubID);
                    double calcSub = Double.parseDouble(txt.getText().toString());
                    total += calcSub;
                }

                Double amnt = Double.parseDouble(amount);
                DecimalFormat dec = new DecimalFormat("0.00");
                String t = dec.format(total);
                String aa = dec.format(amnt);
                System.out.println("Total of Subtotals: " + t);
                System.out.println("Amount Entered: " + aa);

                if(!t.equals(aa)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Subtotals do not add to Total Amount. Please verify your information.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Okay", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    String taxFlag = "notax";
                    intent.putExtra(PASS_TAX_FLAG, taxFlag);

                    intent.putExtra(PASS_TITLE, title);
                    intent.putExtra(PASS_CATEGORY, category);
                    intent.putExtra(PASS_AMOUNT, amount);
                    intent.putExtra(PASS_NUMBER, number);
                    intent.putExtra(PASS_CURRENT_USER, userName);
                    intent.putExtra(PASS_USER2, user2);
                    intent.putExtras(bun);
                    startActivity(intent);
                }
            }
            //if tax box is checked, mark a flag and hand off to next activity (will calculate math there)
            if(taxChecked){
                //check that subtotals do not exceed total amount or are equal to total amount
                double u1Sub = Double.parseDouble(user1Subtotal.getText().toString());
                total += u1Sub;

                double u2Sub = Double.parseDouble(user2Subtotal.getText().toString());
                total += u2Sub;

                for(int b = 0; b < numToCreate; b++){
                    calcSubID += 1;
                    EditText txt = (EditText)findViewById(calcSubID);
                    double calcSub = Double.parseDouble(txt.getText().toString());
                    total += calcSub;
                }

                Double amnt = Double.parseDouble(amount);

                int compare = Double.compare(total, amnt);

                if(compare > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Subtotals exceed Total Amount. Please verify your information.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Okay", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(compare == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Subtotals equal Total Amount. Please verify your information.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Okay", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    String taxFlag = "tax";
                    intent.putExtra(PASS_TAX_FLAG, taxFlag);

                    intent.putExtra(PASS_TITLE, title);
                    intent.putExtra(PASS_CATEGORY, category);
                    intent.putExtra(PASS_AMOUNT, amount);
                    intent.putExtra(PASS_NUMBER, number);
                    intent.putExtra(PASS_CURRENT_USER, userName);
                    intent.putExtra(PASS_USER2, user2);
                    intent.putExtras(bun);
                    startActivity(intent);
                }
            }


        }

    }
}
