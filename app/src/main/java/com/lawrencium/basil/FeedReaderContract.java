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
        public static final String COLUMN_NULL_HACK = "null";
        //public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";

    }
}