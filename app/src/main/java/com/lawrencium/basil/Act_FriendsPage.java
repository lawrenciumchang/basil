package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;


public class Act_FriendsPage extends Activity {

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__friends_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);

        Spinner friends = (Spinner)findViewById(R.id.spinFriends);

        //code from Act_EqualSplitPage.java for Activity to start on spinner select-----------------
        /*Spinner categorySet = (Spinner)findViewById(R.id.equalCategory);
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
        db.close();
        if(category != null) {
            for (int i = 0; i < items.size(); i++) {
                if (category.equals(items.get(i))) {
                    categorySet.setSelection(i);
                }
            }
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__friends_page, menu);
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
                i = new Intent(this, Act_BudgetBuddy.class);
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
        i = new Intent(this, Act_BudgetBuddy.class);
        i.putExtra(PASS_CURRENT_USER, userName);
        startActivityForResult(i, 0);
    }


}
