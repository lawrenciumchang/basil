package com.lawrencium.basil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lawrencium.basil.FeedReaderContract.FeedEntry;
/**
 * Created by James on 4/2/2015.
 */





public class TabsDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_TABS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_USEROWED + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_USEROWING + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CATEGORIES + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TABID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_DATE + TEXT_TYPE +

                    // Any other options for the CREATE command
                    " )";

    private static final String SQL_DELETE_TABS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_TABS;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public TabsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
