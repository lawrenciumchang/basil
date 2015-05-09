package com.lawrencium.basil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.lawrencium.basil.james.backend.registration.Registration;
import com.lawrencium.basil.james.backend.registration.model.CollectionResponseStringCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Act_FriendsPage extends Activity {

    public final static String PASS_CURRENT_USER = "com.lawrencium.basil.CURRENTUSER";
    Registration regService = null;
    private static ArrayList<ArrayList<String>> friendsList;
    String userName;
    Boolean waitFriends = false;
    SQLiteDbHelper SQLiteDbHelper = new SQLiteDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__friends_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userName = intent.getStringExtra(Act_SignInPage.PASS_CURRENT_USER);
        viewAllFriends();
        while(!waitFriends){

        }

        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();

        for(ArrayList<String> aS: friendsList){
            System.out.println("sop: "+aS);
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FRIEND, aS.get(0));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL, aS.get(1));
            long newRowId = db.insert(
                    FeedReaderContract.FeedEntry.TABLE_NAME_FRIENDS,
                    FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                    values);
            Log.v("Database", aS.get(0) + " " + aS.get(1));
        }

        createDropdown();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__friends_page, menu);
        return true;
    }
    public void createDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinFriends);
        ArrayList<String> items = new ArrayList<String>();
        items.add("Select Friend");

        for(ArrayList<String> temp : friendsList){
            if(!temp.isEmpty())
                items.add(temp.get(0));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
    }

    public void viewAllFriends(){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                if (regService == null) {
                    Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl("https://eternal-ruler-92119.appspot.com/_ah/api/");
                    regService = builder.build();
                }

                String msg = "";
                try {

                    CollectionResponseStringCollection temp3 = regService.listFriends().execute();
                    List<List<String>> test = temp3.getItems();

                    ListIterator<List<String>> litr2 = test.listIterator();

                    friendsList = new ArrayList<ArrayList<String>>();

                    ArrayList<String> tempArr = new ArrayList<String>();

                    while (litr2.hasNext()) {
                        tempArr = new ArrayList<String>(litr2.next());
                        friendsList.add(tempArr);
                    }
                    waitFriends = true;

                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    msg = "Error: " + ex.getMessage();
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
            }
        }.execute(null, null, null);

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
