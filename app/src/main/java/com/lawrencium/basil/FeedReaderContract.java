package com.lawrencium.basil;

import android.provider.BaseColumns;

/**
 * Created by Evan on 3/24/2015.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME_CATEGORIES = "categories";
        public static final String TABLE_NAME_TABS = "tabs";
        public static final String TABLE_NAME_TRANSACTIONS = "transactions";
        public static final String TABLE_NAME_FRIENDS = "friends";
        public static final String COLUMN_NULL_HACK = "null";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_USEROWED = "userowed";
        public static final String COLUMN_NAME_USEROWING = "userowing";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CATEGORIES = "categories";
        public static final String COLUMN_NAME_TABID = "tabid";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_FRIEND = "friend";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_TRANSACTIONID = "transactionid";

    }
}