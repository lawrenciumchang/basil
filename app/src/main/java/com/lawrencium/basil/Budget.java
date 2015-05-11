package com.lawrencium.basil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Evan on 3/23/2015.
 */
class Budget {

    private Budget() {
    }

    /**
     * calculateWeek - calculates which week the given date is in.
     * @param day current day.
     * @param daysMax last day of the current month.
     * @return returns which quarter the current day is in. 0 is the 1st quarter -> 3 is the 4th quarter
     * @throws java.lang.IllegalArgumentException If the date or number of days in the month is invalid
     */
    public static int calculateWeek(int day, int daysMax) {
        final int[] feb = {7, 14, 21, 28};
        final int[] leapFeb = {8, 15, 26, 29};
        final int[] small = {8, 16, 23, 30};
        final int[] big = {8, 16, 24, 31};
        int[] weekRange;

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

    /**
     * This method calculates the bounds for each quarter and stores it into an array called bounds.
     * The value in each index represents the start of each quarter. This date stored in the array
     * is formatted in yyyy/mm/dd k:mm:ss.
     * @param d This is the current day.
     * @return Returns the bounds array.
     */
    public static String[] calculateBounds(Date d) {
        final int[] feb = {7, 7, 7, 7};
        final int[] leapFeb = {8, 7, 7, 7};
        final int[] small = {8, 8, 7, 7};
        final int[] big = {8, 8, 8, 7};
        int[] weekRange;

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

    /**
     * Puts a newly made transaction into the database
     * @param db database being pulled from.
     * @param name item purchased.
     * @param value cost of item.
     * @param category category the item.
     * @return newRowId, helps with table lookup.
     */
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
    public static long newTransaction(SQLiteDatabase db, String name, String value, String category, String date) {
        // Date format: "yyyy/MM/dd k:mm:ss"
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

    public static long newCategory(SQLiteDatabase db, String inputName, String inputBudget) {
        ContentValues values = new ContentValues();
        String newName = inputName;
        String newBudget = inputBudget;

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, newName);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, newBudget);
        long newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES,
                FeedReaderContract.FeedEntry.COLUMN_NULL_HACK,
                values);
        return newRowId;
    }

    /**
     * This method returns a simpler date version of calculateBounds. The bounds array here only has
     * day in index 0-3, index 4 contains the last day of the month, and index 5 has the current
     * month
     * @return bounds array
     */
    public static int[] calculateBoundsDate() {
        final int[] feb = {7, 7, 7, 7};
        final int[] leapFeb = {8, 7, 7, 7};
        final int[] small = {8, 8, 7, 7};
        final int[] big = {8, 8, 8, 7};
        int[] weekRange;

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

    /**
     * Gets a specific transaction and puts it in a formatted transaction row.
     * @param context
     * @param title item purchased.
     * @param value cost of item.
     * @param date date purchased.
     * @return
     */
    public static LinearLayout getTransactionRow(Context context, String title, String value, String date) {
        LinearLayout transactionRow = new LinearLayout(context);
        transactionRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        transactionRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView nextTransaction = new TextView(context);
        nextTransaction.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        transactionRow.addView(nextTransaction);

        TextView nextTransactionValue = new TextView(context);
        nextTransactionValue.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        nextTransactionValue.setGravity(Gravity.RIGHT);
        transactionRow.addView(nextTransactionValue);

        String valueStr = valueFormat(value);

        String[] splitDate = date.split("[ :/]");
        String text = splitDate[1]+"/"+splitDate[2] + " " +
                title;
        nextTransaction.setText(text);
        nextTransaction.setTextSize(15);

        nextTransactionValue.setText("$" + valueStr);
        nextTransactionValue.setTextSize(15);

        return transactionRow;
    }

    /**
     * Currency formatting for the cost of item purchased.
     * @param value cost of item.
     * @return formatted cost.
     */
    public static String valueFormat(String value){
        BigDecimal bdValue = new BigDecimal(value);
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMinimumIntegerDigits(1);
        String valueStr = df.format(bdValue);
        return valueStr;
    }

    /**
     * Gets transaction data from the database.
     * @param db datebase where we're pulling data from.
     * @param filter dates of purchased items
     * @return table of transactions
     */
    public static Cursor getTransactions(SQLiteDatabase db, String filter){
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        };
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " DESC";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME_TRANSACTIONS,
                projection,
                filter,
                null, null, null,
                sortOrder
        );
        return c;
    }
}
