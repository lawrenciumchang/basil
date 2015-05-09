package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class Act_EqualSplitPeoplePage extends Activity {
    String title;
    String category;
    String amount;
    String number;
    String userName;
    String user2;
    Bundle b;

    public final static String PASS_TITLE = "com.lawrencium.basil.TITLE";
    public final static String PASS_CATEGORY = "com.lawrencium.basil.CATEGORY";
    public final static String PASS_AMOUNT = "com.lawrencium.basil.AMOUNT";
    public final static String PASS_NUMBER = "com.lawrencium.basil.NUMBER";
    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    public final static String PASS_USER2 = "com.lawrencium.basil.USER2";

    int numToCreate;
//    boolean check = true;

    /**
     * Creates dropdown menus equal to the number of users selected on previous page.
     * @param savedInstanceState
     */
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

        user2 = intent.getStringExtra(Act_EqualSplitConfirmPage.PASS_USER2);
        b = intent.getExtras();

        numToCreate = Integer.parseInt(number);
        numToCreate = numToCreate - 2;

        //used for creating new objects dynamically
        LinearLayout ll = (LinearLayout)findViewById(R.id.spinnerLayout);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        System.out.println("Title: " + title);
        System.out.println("Category: " + category);
        System.out.println("Amount: " + amount);
        System.out.println("Number of people: " + number);
        System.out.println("Current User: " + userName);

//        createUserDropdown();
        EditText userHeader = (EditText)findViewById(R.id.user1);
        userHeader.setText(userName);

//        createUserDropdown2();
        Spinner user2Set = (Spinner)findViewById(R.id.user2);
        ArrayAdapter<String> friendsSet = Tab.createDropdown(this, user2Set, FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS, FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, "Select User");
//        String[] items2 = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        if(user2 != null) {
            for (int i = 0; i < friendsSet.getCount(); i++) {
                if (user2.equals(friendsSet.getItem(i))) {
                    user2Set.setSelection(i);
                }
            }
        }

        //adds selected users upon pressing back button from next page
        if(b.getString("2") != null) {
            for (int a = 0; a < numToCreate; a++) {
                String id = Integer.toString(a+2);
                for(int c = 0; c < friendsSet.getCount(); c++) {
                    if (b.getString(id).equals(friendsSet.getItem(c))) {
//                        Spinner userSet = (Spinner)findViewById(a);
//                        System.out.println("find spinner: " + userSet);
//                        Spinner userSet = createNewUserDropdown();
                        Spinner userSet = new Spinner(this);
                        Tab.createDropdown(this, userSet, FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS, FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, "Select User");
                        ll.addView(userSet, ll.getChildCount(), lp);
                        userSet.setId(a+2);
                        userSet.setSelection(c);
                    }
                }
            }
        }
        //dynamically creates Spinners for number of users
        else {
            for (int i = 0; i < numToCreate; i++) {
//                Spinner createNew = createNewUserDropdown();
                Spinner createNew = new Spinner(this);
                Tab.createDropdown(this, createNew, FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS, FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, "Select User");
                ll.addView(createNew, ll.getChildCount(), lp);
                createNew.setId(i+2);
            }
        }

    }

    /**
     * Used to create a dropdown for the current user.
     */
    public void createUserDropdown(){
        Spinner user1 = (Spinner)findViewById(R.id.user1);
        String[] items = new String[]{userName};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        user1.setAdapter(adapter);
        user1.setClickable(false);
    }

    /**
     * Used to create a dropdown for selected the second user.
     */
    public void createUserDropdown2(){
        Spinner user2 = (Spinner)findViewById(R.id.user2);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user2.setAdapter(adapter);
    }

    /**
     * Used to dynamically add select user dropdowns.
     * @return
     */
    public Spinner createNewUserDropdown(){
        Spinner newUser = new Spinner(this);
        String[] items = new String[]{"Select User", "Annie", "Evan", "Lawrence", "James"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newUser.setAdapter(adapter);
        return newUser;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__equal_split_people_page, menu);
        return true;
    }

    /**
     * Creates a back action bar to take user to previous page.
     * @param item  Back action bar
     * @return      Act_EqualSplitPage.class
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
                i = new Intent(this, Act_EqualSplitPage.class);
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

    /**
     * Takes user to previous page.
     */
    @Override
    public void onBackPressed() {
        Intent i;
        i = new Intent(this, Act_EqualSplitPage.class);
        i.putExtra(PASS_TITLE, title);
        i.putExtra(PASS_CATEGORY, category);
        i.putExtra(PASS_AMOUNT, amount);
        i.putExtra(PASS_NUMBER, number);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }

    /**
     * Takes user to the confirmation page.
     * @param view
     */
    public void equalPeopleNext(View view){

        boolean check = true;

        Intent intent = new Intent(this, Act_EqualSplitConfirmPage.class);

        //check and see if any fields are blank
        Spinner u2 = (Spinner)findViewById(R.id.user2);
        if(u2.getSelectedItem().toString().equals("Select User")){
            check = false;
        }
        for(int a = 0; a < numToCreate; a++){
            Spinner spin = (Spinner)findViewById(a+2);
            if(spin.getSelectedItem().toString().equals("Select User")){
                check = false;
            }
        }

        //BUNDLE NEWS!
        //bundle includes userName (you), user2, and anything else
        //userName starts at "0"
        b.putString("0", userName);
        String user2 = u2.getSelectedItem().toString();
        b.putString("1", user2);

//        Bundle b = new Bundle();

        //put stuff into the Bundle
        for(int i = 0; i < numToCreate; i++){
            Spinner spin = (Spinner)findViewById(i+2);
            String id = Integer.toString(i+2);
            b.putString(id, spin.getSelectedItem().toString());
        }

        if(check){
            intent.putExtra(PASS_TITLE, title);
            intent.putExtra(PASS_CATEGORY, category);
            intent.putExtra(PASS_AMOUNT, amount);
            intent.putExtra(PASS_NUMBER, number);
            intent.putExtra(PASS_CURRENT_USER, userName);
            intent.putExtra(PASS_USER2, user2);
            intent.putExtras(b);
            startActivity(intent);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please select users for your transaction.");
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
