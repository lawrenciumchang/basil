package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class Act_EqualSplitPage extends Activity {
    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String title;
    String category;
    String amount;
    String number;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__equal_split_page);
        createDropdown();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        title = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPeoplePage.PASS_NUMBER);

        System.out.println("Current User from Equal Split page: " + userName);

        EditText titleSet = (EditText)findViewById(R.id.equalTitle);
        titleSet.setText(title);

        Spinner categorySet = (Spinner)findViewById(R.id.equalCategory);
        String[] items = new String[]{"Select Category", "Restaurants", "Groceries", "Shopping", "Entertainment"};
        if(category != null) {
            for (int i = 0; i < items.length; i++) {
                if (category.equals(items[i])) {
                    categorySet.setSelection(i);
                }
            }
        }

        EditText amountSet = (EditText)findViewById(R.id.equalAmount);
        amountSet.setText(amount);

        EditText numberSet = (EditText)findViewById(R.id.equalNumber);
        numberSet.setText(amount);

    }

    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.equalCategory);
        String[] items = new String[]{"Select Category", "Restaurants", "Groceries", "Shopping", "Entertainment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__equal_split_page, menu);
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
                i = new Intent(this, Act_TabsPage.class);
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
        i = new Intent(this, Act_TabsPage.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

    public void equalNext(View view){
        EditText equalTitle = (EditText)findViewById(R.id.equalTitle);
        String title = equalTitle.getText().toString();

        Spinner equalCategory = (Spinner)findViewById(R.id.equalCategory);
        String category = equalCategory.getSelectedItem().toString();

        EditText equalAmount = (EditText)findViewById(R.id.equalAmount);
        String amount = equalAmount.getText().toString();

        EditText equalNumber = (EditText)findViewById(R.id.equalNumber);
        String number = equalNumber.getText().toString();

        if(title.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(amount.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a total amount for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(number.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter the number of people for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(number.matches("1")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a number greater than 1.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if((!number.matches("")) || (!number.matches("1"))){
//            int num = Integer.parseInt(number);
//            if(num > 4) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Splitting between parties greater than 4 is only supported in the Pro Version.");
//                builder.setCancelable(true);
//                builder.setPositiveButton("I have too many friends", null);
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//            else{
                Intent intent = new Intent(this, Act_EqualSplitPeoplePage.class);
                intent.putExtra(PASS_TITLE, title);
                intent.putExtra(PASS_CATEGORY, category);
                intent.putExtra(PASS_AMOUNT, amount);
                intent.putExtra(PASS_NUMBER, number);
                intent.putExtra(PASS_CURRENT_USER, userName);
                startActivity(intent);
//            }
        }
    }
}
