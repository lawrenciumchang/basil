package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class Act_IouPage extends Activity {
    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_USER = "com.lawrencium.basil.USER";

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);

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

        // Load categories from Budget side
        Spinner categorySet = (Spinner)findViewById(R.id.spinner1);
        categorySet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                if(parent.getItemAtPosition(pos).toString().equals("Add New Category")) {
                    Intent intent = new Intent(parent.getContext(), Act_NewCategory.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayList<String> items = new ArrayList<String>();
        items.add("Select Category");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }
        if(category != null) {
            for (int i = 0; i < items.size(); i++) {
                if (category.equals(items.get(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

        EditText amountSet = (EditText)findViewById(R.id.editText);
        amountSet.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        amountSet.setText(amount);

        Spinner userSet = (Spinner)findViewById(R.id.spinner2);
        //String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayList<String> items2 = new ArrayList<String>();
        items2.add("Select User");

        String[] userProjection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND
        };
        sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND;
        c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                userProjection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                items2.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND)));
            } while (c.moveToNext());
        }
        if(user != null) {
            for (int i = 0; i < items2.size(); i++) {
                if (user.equals(items2.get(i))) {
                    userSet.setSelection(i);
                }
            }
        }

        db.close();

        EditText title = (EditText)findViewById(R.id.editText2);
        EditText amount = (EditText)findViewById(R.id.editText);

        Bundle b = getIntent().getExtras();
        if(b.getBoolean("IOU")) {
            title.setText(b.getString("TITLE"));
            amount.setText(b.getString("AMOUNT"));
            System.out.println("USER_OWED: " + b.getString("USER_OWED"));
            for (int i = 0; i < items2.size(); i++) {
                if (b.getString("USER_OWED").equals(items2.get(i))) {
                    userSet.setSelection(i);
                    user = b.getString("USER_OWED");
                    System.out.println("User Spinner set to: " + b.getString("USER_OWED") + " (Item "+i+")");
                }
            }
        }

    }

    protected void onResume(){
        super.onResume();

        ArrayList<String> items = new ArrayList<String>();
        createDropdown();
        Spinner categorySet = (Spinner)findViewById(R.id.spinner1);
        items.add("Select Category");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }

        if(category != null) {
            for (int i = 0; i < items.size(); i++) {
                if (category.equals(items.get(i))) {
                    categorySet.setSelection(i);
                }
            }
        }

        Spinner userSet = (Spinner)findViewById(R.id.spinner2);
        //String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayList<String> items2 = new ArrayList<String>();
        items2.add("Select User");

        String[] userProjection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND
        };
        sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND;
        c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                userProjection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                items2.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND)));
            } while (c.moveToNext());
        }
        if(user != null) {
            for (int i = 0; i < items2.size(); i++) {
                if (user.equals(items2.get(i))) {
                    userSet.setSelection(i);
                }
            }
        }

        db.close();
    }

    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        ArrayList<String> items = new ArrayList<String>();
        items.add("Select Category");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                items.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            } while (c.moveToNext());
        }
        items.add("Add New Category");
        //use simple_spinner_item to make the spinner display smaller
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        Spinner dropdown2 = (Spinner)findViewById(R.id.spinner2);
        //String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayList<String> items2 = new ArrayList<String>();
        items2.add("Select User");

        String[] userProjection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND
        };
        sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND;
        c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                userProjection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do {
                items2.add(c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND)));
                Log.i(null, c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND)));
            } while (c.moveToNext());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);
        db.close();
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
            Bundle bOut = new Bundle();
            Bundle bIn = getIntent().getExtras();
            bOut.putString("OWED_TABID", bIn.getString("TAB_ID"));
            bOut.putString("USER_OWED", bIn.getString("USER_OWED"));
            intent.putExtras(bOut);
            startActivity(intent);
        }
    }

}
