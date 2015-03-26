package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Act_EqualSplitPeoplePage extends Activity {
    String title;
    String category;
    String amount;
    String number;
    String userName;

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__equal_split_people_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        title = intent.getStringExtra(Act_EqualSplitPage.PASS_TITLE);
        category = intent.getStringExtra(Act_EqualSplitPage.PASS_CATEGORY);
        amount = intent.getStringExtra(Act_EqualSplitPage.PASS_AMOUNT);
        number = intent.getStringExtra(Act_EqualSplitPage.PASS_NUMBER);
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        System.out.println("Title: " + title);
        System.out.println("Category: " + category);
        System.out.println("Amount: " + amount);
        System.out.println("Number of people: " + number);
        System.out.println("Current User: " + userName);

        createUserDropdown();
        createUserDropdown2();

        if(number.matches("2")){
            //hide all boxes but first 2
            Spinner u3 = (Spinner)findViewById(R.id.u3);
            u3.setVisibility(View.GONE);

            Spinner u4 = (Spinner)findViewById(R.id.u4);
            u4.setVisibility(View.GONE);
        }

        if(number.matches("3")){
            createDropdown3();
            Spinner u4 = (Spinner)findViewById(R.id.u4);
            u4.setVisibility(View.GONE);
        }
        if(number.matches("4")){
            createDropdown3();
            createDropdown4();
        }

    }

    public void createUserDropdown(){
        Spinner user1 = (Spinner)findViewById(R.id.user1);
        String[] items = new String[]{userName};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        user1.setAdapter(adapter);
    }

    public void createUserDropdown2(){
        Spinner user2 = (Spinner)findViewById(R.id.user2);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        user2.setAdapter(adapter);
    }

    public void createDropdown3(){
        Spinner dropdown = (Spinner)findViewById(R.id.u3);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
    }

    public void createDropdown4(){
        Spinner dropdown = (Spinner)findViewById(R.id.u4);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__equal_split_people_page, menu);
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
                i = new Intent(this, Act_EqualSplitPage.class);
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
        i = new Intent(this, Act_EqualSplitPage.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

}
