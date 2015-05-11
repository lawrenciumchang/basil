package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;



public class Act_CustomSplitPage extends Activity {

    private final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    private final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    private final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    private final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    private final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

    private String title;
    private String category;
    private String amount;
    private String number;
    private String userName;

    private ArrayAdapter<String> categoryAdapter;

    /**
     * Allows user to input transaction information
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__custom_split_page);

        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        title = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_TITLE);
        category = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_CustomSplitPeoplePage.PASS_NUMBER);

        System.out.println("Current User from Custom Split page: " + userName);

        EditText titleSet = (EditText)findViewById(R.id.customTitle);
        titleSet.setText(title);

        Spinner categorySet = (Spinner)findViewById(R.id.customCategory);
        categoryAdapter = Tab.createCategoriesDropdown(this, categorySet);

        EditText amountSet = (EditText)findViewById(R.id.customAmount);
        amountSet.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountSet.setText(amount);

        EditText numberSet = (EditText)findViewById(R.id.customNumber);
        numberSet.setText(number);

    }

    /**
     * Resumes functionality after user has chosen to create a new Category.
     */
    protected void onResume(){
        super.onResume();

        Spinner categorySet = (Spinner)findViewById(R.id.customCategory);

        if(category != null) {
            for (int i = 0; i < categoryAdapter.getCount(); i++) {
                if (category.equals(categoryAdapter.getItem(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__customized_split_page, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_TabsPage.class
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
                i = new Intent(this, Act_TabsPage.class);
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
//    @Override
//    public void onBackPressed() {
//        Intent i;
//        i = new Intent(this, Act_TabsPage.class);
//        i.putExtra(PASS_CURRENT_USER, userName);
//        startActivityForResult(i, 0);
//    }

    /**
     * Takes user to next page to select users and split transaction information.
     * @param view
     */
    public void customNext(View view){
        EditText customTitle = (EditText)findViewById(R.id.customTitle);
        String title = customTitle.getText().toString();

        Spinner customCategory = (Spinner)findViewById(R.id.customCategory);
        String category = customCategory.getSelectedItem().toString();

        EditText customAmount = (EditText)findViewById(R.id.customAmount);
        String amount = customAmount.getText().toString();

        EditText customNumber = (EditText)findViewById(R.id.customNumber);
        String number = customNumber.getText().toString();

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
        else if(number.matches("1") || number.matches("0")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a number greater than 1.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if((!number.matches("")) || (!number.matches("1"))|| !number.matches("0")){
            Intent intent = new Intent(this, Act_CustomSplitPeoplePage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_NUMBER, number);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
            finish();
        }
    }

}
