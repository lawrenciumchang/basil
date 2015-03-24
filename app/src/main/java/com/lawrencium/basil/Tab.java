package com.lawrencium.basil;

/**
 * Created by James on 3/23/2015.
 */
public class Tab {
    private String UserOwed;
    private String UserOwing;
    private double AmountOwed;
    private String Category;
    private String Title;
    private int TabId;
    private double tmep;


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
    }

    //Copy Constructor
    public Tab(Tab T) {
        UserOwed = T.getUserOwed();
        UserOwing = T.getUserOwing();
        AmountOwed = T.getAmountOwed();
        Category = T.getCategory();
        Title = T.getTitle();
        TabId = T.getTabId();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tab tab = (Tab) o;

        if (TabId != tab.TabId) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "UserOwed='" + UserOwed + '\'' +
                ", UserOwing='" + UserOwing + '\'' +
                ", AmountOwed=" + AmountOwed +
                ", Category='" + Category + '\'' +
                ", Title='" + Title + '\'' +
                ", TabId=" + TabId +
                '}';
    }
}
