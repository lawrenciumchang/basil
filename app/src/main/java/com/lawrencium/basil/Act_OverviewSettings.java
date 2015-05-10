package com.lawrencium.basil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Act_OverviewSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_settings);
    }


    /**
     * This adds items to the action bar if it is present.
     * @param menu button inflates the menu options on the top right corner of the screen
     * @return returns true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview_settings, menu);
        return true;
    }

    /**
     * Handles the action bar item clicks here. The action bar will automatically handle clicks on
     * the Home/Up button so long as you specify a parent activity in AndroidManifest.xml
     * @param item Home/Up button
     * @return return needs to be true in order to return you to the previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
