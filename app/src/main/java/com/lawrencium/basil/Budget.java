package com.lawrencium.basil;

import java.util.ArrayList;

/**
 * Created by Evan on 3/23/2015.
 */
public class Budget {
    private volatile static Budget singleton;
    //FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper();

    private Budget() {
    }
    public static Budget getInstance() {
        if (singleton == null) {
            synchronized (Budget.class) {
                if (singleton == null) {
                    singleton = new Budget();
                }
            }
        }
        return singleton;
    }


}
