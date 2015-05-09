package com.lawrencium.basil;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Evan on 3/23/2015.
 */
public class Budget {

    private Budget() {
    }

    /**
     * calculateWeek - calculates which week the given date is in
     * @param day
     * @param daysMax
     * @return
     * @throws java.lang.IllegalArgumentException If the date or number of days in the month is invalid
     */
    public static int calculateWeek(int day, int daysMax) {
        final int[] feb = {7, 14, 21, 28};
        final int[] leapFeb = {8, 15, 26, 29};
        final int[] small = {8, 16, 23, 30};
        final int[] big = {8, 16, 24, 31};
        int[] weekRange = big;

        if(day > daysMax)
            throw new IllegalArgumentException("The date ["+day+"] cannot be greater than the number of days in the month");
        switch(daysMax) {
            case 28: weekRange = feb; break;
            case 29: weekRange = leapFeb; break;
            case 30: weekRange = small; break;
            case 31: weekRange = big; break;
            default: throw new IllegalArgumentException(daysMax + " is not a valid number of days in a month");
        }
        for(int i=0; i<4; i++) {
            if(day <= weekRange[i])
                return i;
        }
        return 0;
    }
    public static String[] calculateBounds(Date d) {
        final int[] feb = {7, 7, 7, 7};
        final int[] leapFeb = {8, 7, 7, 7};
        final int[] small = {8, 8, 7, 7};
        final int[] big = {8, 8, 8, 7};
        int[] weekRange = big;

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        String[] bounds = new String[5];
        Calendar c = Calendar.getInstance();
        int daysThisMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        switch(daysThisMonth) {
            case 28: weekRange = feb; break;
            case 29: weekRange = leapFeb; break;
            case 30: weekRange = small; break;
            case 31: weekRange = big; break;
            default: throw new IllegalArgumentException(daysThisMonth + " is not a valid number of days in a month");
        }

        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        d = c.getTime();
        bounds[0] = format.format(d);
        System.out.println(bounds[0]);
        for(int i=0; i<4; i++) {
            c.add(Calendar.DAY_OF_MONTH, weekRange[i]);
            d = c.getTime();
            bounds[i+1] = format.format(d);
            System.out.println(bounds[i+1]);
        }

        return bounds;
    }

    public static long newTransaction(SQLiteDatabase db, String name, String value, String category) {
        Date tempDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd k:mm:ss");
        String date = dateFormat.format(tempDate);
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, category);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, value);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, date);
        long newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
                FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                values);
        return newRowId;
    }

    public static int[] calculateBoundsDate() {
        final int[] feb = {7, 7, 7, 7};
        final int[] leapFeb = {8, 7, 7, 7};
        final int[] small = {8, 8, 7, 7};
        final int[] big = {8, 8, 8, 7};
        int[] weekRange = big;


        /*array of date bounds
        0) 1st day of the quarter
        1) 1st day of the second quarter
        2) 1st day of the third quarter
        3) 1st day of the fourth quarter
        4) last day of the month
        5) current month
        */
        int[] bounds = new int[6];
        Calendar c = Calendar.getInstance();
        bounds[5] = c.get(Calendar.MONTH);
        int daysThisMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        switch(daysThisMonth) {
            case 28: weekRange = feb; break;
            case 29: weekRange = leapFeb; break;
            case 30: weekRange = small; break;
            case 31: weekRange = big; break;
            default: throw new IllegalArgumentException(daysThisMonth + " is not a valid number of days in a month");
        }

        bounds[0] = 1;
        System.out.println(bounds[0]);
        for(int i=0; i<4; i++) {
            bounds[i+1] = bounds[i] + weekRange[i];
            System.out.println(bounds[i+1]);
        }

        return bounds;
    }
}
