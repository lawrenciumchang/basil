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


public class Act_IouPage extends Activity {
    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_USER = "com.lawrencium.basil.USER";

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String title;
    String category;
    String amount;
    String user;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__iou_page);
        createDropdown();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        title = intent.getStringExtra(Act_PayPage.PASS_TITLE);
        title = intent.getStringExtra(Act_RequestPage.PASS_TITLE);
        category = intent.getStringExtra(Act_PayPage.PASS_CATEGORY);
        category = intent.getStringExtra(Act_RequestPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_PayPage.PASS_AMOUNT);
        amount = intent.getStringExtra(Act_RequestPage.PASS_AMOUNT);
        user = intent.getStringExtra(Act_PayPage.PASS_USER);
        user = intent.getStringExtra(Act_RequestPage.PASS_USER);

        System.out.println("Current User from IOU page: " + userName);

        EditText titleSet = (EditText)findViewById(R.id.editText2);
        titleSet.setText(title);

        Spinner categorySet = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Select Category", "Restaurants", "Groceries", "Shopping", "Entertainment"};
        if(category != null) {
            for (int i = 0; i < items.length; i++) {
                if (category.equals(items[i])) {
                    categorySet.setSelection(i);
                }
            }
        }

        EditText amountSet = (EditText)findViewById(R.id.editText);
        amountSet.setText(amount);

        Spinner userSet = (Spinner)findViewById(R.id.spinner2);
        String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        if(user != null) {
            for (int i = 0; i < items2.length; i++) {
                if (user.equals(items2[i])) {
                    userSet.setSelection(i);
                }
            }
        }

    }

    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Select Category", "Restaurants", "Groceries", "Shopping", "Entertainment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        Spinner dropdown2 = (Spinner)findViewById(R.id.spinner2);
        String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__iou_page, menu);
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

    public void requestClick(View view){

        EditText editText2 = (EditText)findViewById(R.id.editText2);
        String title = editText2.getText().toString();

        Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        String category = spinner1.getSelectedItem().toString();

        EditText editText = (EditText)findViewById(R.id.editText);
        String amount = editText.getText().toString();

        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        String user = spinner2.getSelectedItem().toString();

        if(title.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(amount.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter an amount for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(user.matches("Select User")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a user for your request.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Intent intent = new Intent(this, Act_RequestPage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_USER, user);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

    public void payClick(View view){

        EditText editText2 = (EditText)findViewById(R.id.editText2);
        String title = editText2.getText().toString();

        Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        String category = spinner1.getSelectedItem().toString();

        EditText editText = (EditText)findViewById(R.id.editText);
        String amount = editText.getText().toString();

        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        String user = spinner2.getSelectedItem().toString();

        if(title.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a title for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(category.matches("Select Category")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a category for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(amount.matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter an amount for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(user.matches("Select User")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select a user for your payment.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else{
            Intent intent = new Intent(this, Act_PayPage.class);
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_USER, user);
            intent.putExtra(PASS_CURRENT_USER, userName);
            startActivity(intent);
        }
    }

}
