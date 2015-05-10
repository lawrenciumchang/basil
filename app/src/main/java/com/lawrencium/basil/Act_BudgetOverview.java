package com.lawrencium.basil;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class Act_BudgetOverview extends Activity implements Frag_GraphButton.OnFragmentInteractionListener {
    final SQLiteDbHelper mDbHelper = new SQLiteDbHelper(this);
    private final FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_overview);
        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Action bar not found, no action necessary
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE;
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                projection,
                null, null, null, null,
                sortOrder
        );
        if(c.moveToFirst()) {
            do { //adds category buttons created by user onto this page in a sorted order
                Frag_GraphButton fragment = Frag_GraphButton.newInstance(
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE)));
                fragmentTransaction.add(R.id.categoryLayout, fragment, c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
            } while (c.moveToNext());
        }
        fragmentTransaction.commit();
        db.close();
    }

    /**
     * refreshes categories to resort and add a newly created category
     */
    protected void onResume() {
        super.onResume();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE
        };
        String sortOrder = FeedReaderContract.FeedEntry._ID + " DESC";
        String limit = "1";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                projection,
                null, null, null, null,
                sortOrder,
                limit
        );
        if(c.moveToFirst()) {
            if(fragmentManager.findFragmentByTag( c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)) ) == null) {
                Frag_GraphButton fragment = Frag_GraphButton.newInstance(
                    c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)),
                    c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                    c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE)));
                fragmentTransaction.add(R.id.categoryLayout, fragment, c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
            }
        }
        fragmentTransaction.commit();
    }

    /**
     * Goes to the category selected.
     */
    void gotoNewCategory(){
        Intent intent = new Intent(this, Act_NewCategory.class);
        startActivity(intent);
    }

    /**
     * This adds items to the action bar if it is present.
     * @param menu button inflates the menu options on the top right corner of the screen
     * @return returns true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_overview, menu);
        return true;
    }

    /**
     * For the back button on the top left button
     * @param item back arrow used for parent functionality
     * @return return needs to be true in order to return you to the previous page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        switch (item.getItemId())
        {
            case android.R.id.home:

                finish();
                break;
            case R.id.action_new_category:
                gotoNewCategory();
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This method does the official deletion of a category. This includes the deletion of the
     * graph and the data in the database.
     * @param fragment
     */
    public void removeCategory(Frag_GraphButton fragment) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES, FeedReaderContract.FeedEntry._ID+"="+fragment.getCatId(), null);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}
