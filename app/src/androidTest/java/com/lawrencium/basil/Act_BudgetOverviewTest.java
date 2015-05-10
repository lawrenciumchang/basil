package com.lawrencium.basil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;

/**
 * Created by Evan on 4/14/2015.
 */
public class Act_BudgetOverviewTest extends ActivityInstrumentationTestCase2<Act_BudgetOverview> {
    private SQLiteDatabase db;

    private Act_BudgetOverview newCategoryActivity;
    private EditText newCatName;
    private EditText newCatBudget;

    public Act_BudgetOverviewTest() {
        super(Act_BudgetOverview.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        newCategoryActivity = getActivity();
    }

    @UiThreadTest
    public void testCategorySQLRemove() {
        int lastId;
        db = newCategoryActivity.mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT
        };
        String sortOrder = FeedReaderContract.FeedEntry._ID + " DESC";
        String limit = "1";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                limit
        );
        if(c.moveToFirst()) {
            lastId = Integer.parseInt( c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)) );
        }
        else
            fail("Table is empty");


    }

    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
