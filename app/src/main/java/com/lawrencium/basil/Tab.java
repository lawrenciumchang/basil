package com.lawrencium.basil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by James on 3/23/2015.
 */
public class Tab {
    private String UserOwed;
    private String UserOwing;
    private double AmountOwed;
    private String Category;
    private String Title;
    private String Date;
    private int TabId;



    //Think of ways to identify different tabs

    //May create constructor that makes a tab for only ID for Tab equals methods

    public Tab(int tabId) {
        TabId = tabId;
    }



    //Normal Constructor
    public Tab(String userOwed, String userOwing, double amountOwed, String category, String title) {
        UserOwed = userOwed;
        UserOwing = userOwing;
        AmountOwed = amountOwed;
        Category = category;
        Title = title;
        Date tempDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm:ssa");
        Date = format.format(tempDate);
    }

    //Copy Constructor
    public Tab(Tab T) {
        UserOwed = T.getUserOwed();
        UserOwing = T.getUserOwing();
        AmountOwed = T.getAmountOwed();
        Category = T.getCategory();
        Title = T.getTitle();
        TabId = T.getTabId();
        Date = T.getDate();
    }

    public String getUserOwed() {
        return UserOwed;
    }

    public void setUserOwed(String userOwed) {
        UserOwed = userOwed;
    }

    public String getUserOwing() {
        return UserOwing;
    }

    public void setUserOwing(String userOwing) {
        UserOwing = userOwing;
    }

    public double getAmountOwed() {
        return AmountOwed;
    }

    public void setAmountOwed(double amountOwed) {
        AmountOwed = amountOwed;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getTabId() {
        return TabId;
    }

    public void setTabId(int tabId) {
        TabId = tabId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tab tab = (Tab) o;

        if (TabId != tab.TabId) return false;

        return true;
    }

//    @Override
//    public String toString() {
//        DecimalFormat dec = new DecimalFormat("0.00");
//        return "Tab{" +
//                "UserOwed='" + UserOwed + '\'' +
//                ", UserOwing='" + UserOwing + '\'' +
//                ", AmountOwed=" + dec.format(AmountOwed) +
//                ", Category='" + Category + '\'' +
//                ", Title='" + Title + '\'' +
//                ", TabId=" + TabId +
//                ", Date=" + Date +
//                '}';
//    }

    public String reformatDate(String date){

        String tempString = "";
        int firstBackSlash = date.indexOf('/');
        int secondBackSlash = date.indexOf('/', firstBackSlash+1);

        tempString = date.substring(firstBackSlash+1, secondBackSlash)+"/";
        tempString += date.substring(secondBackSlash+1, secondBackSlash+3)+"/";
        tempString += date.substring(2, firstBackSlash)/*+" "*/;

//        firstBackSlash = date.indexOf(':');
//        secondBackSlash = date.indexOf(':', firstBackSlash+1);
//        tempString += date.substring(firstBackSlash-2, firstBackSlash)+":";
//        tempString += date.substring(firstBackSlash+1, secondBackSlash)+" ";
//        tempString += date.substring(secondBackSlash+3, date.length());


        return tempString;
    }

    @Override
    public String toString(){
        DecimalFormat dec = new DecimalFormat("0.00");
        String amnt = "";

        String printAmount = dec.format(AmountOwed);
        if(printAmount.startsWith("-")){
            printAmount = printAmount.substring(1);
            amnt += "-$" + printAmount;
        }
        else{
            amnt += "$" + printAmount;
        }

        return "User: " + UserOwing + "\n" +
               "Amount Owed: " + amnt + "\n" +
               "Title: " + Title + "\n" +
               "Category: " + Category + "\n" +
               "Date of Transaction: " + reformatDate(Date) + "\n";
    }

}
