package com.lawrencium.basil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;

/**
 * Created by Evan on 4/14/2015.
 */
public class Act_NewCategoryTest extends ActivityInstrumentationTestCase2<Act_NewCategory> {
    private SQLiteDatabase db;

    private Act_NewCategory newCategoryActivity;
    private EditText newCatName;
    private EditText newCatBudget;

    public Act_NewCategoryTest() {
        super(Act_NewCategory.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        newCategoryActivity = getActivity();
        newCatName = (EditText) newCategoryActivity.findViewById(R.id.inputName);
        newCatBudget = (EditText) newCategoryActivity.findViewById(R.id.inputBudget);
    }

    @UiThreadTest
    public void testNewCategorySQLInsert() {
        newCatName.setText("Test Category");
        newCatBudget.setText("123.45");
        newCategoryActivity.createCategory(newCategoryActivity.findViewById(R.id.btn_submitCategory));
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
            assertEquals("Test Category", c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)) );
            assertEquals("123.45", c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT)));
        }
        else
            fail("Table is empty");
    }

    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
