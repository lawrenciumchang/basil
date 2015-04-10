package com.lawrencium.basil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lawrencium.basil.FeedReaderContract.FeedEntry;

/**
 * Created by Evan on 3/24/2015.
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_CATEGORIES + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + //COMMA_SEP +
                    // Any other options for the CREATE command
                    " )";

    private static final String SQL_DELETE_CATEGORIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_CATEGORIES;
    private static final String SQL_CREATE_TRANSACTIONS =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_TRANSACTIONS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + //COMMA_SEP +
                    // Any other options for the CREATE command
                    " )";

    private static final String SQL_DELETE_TRANSACTIONS =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_TRANSACTIONS;
    // If you change the database schema, you must increment the database version.

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIES);
        db.execSQL(SQL_CREATE_TRANSACTIONS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CATEGORIES);
        db.execSQL(SQL_DELETE_TRANSACTIONS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}