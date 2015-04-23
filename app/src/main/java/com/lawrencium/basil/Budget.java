package com.lawrencium.basil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        d = c.getTime();
        bounds[0] = format.format(d);
        for(int i=0; i<4; i++) {
            c.add(Calendar.DAY_OF_MONTH, weekRange[i]);
            d = c.getTime();
            bounds[i+1] = format.format(d);
        }

        return bounds;
    }
}
