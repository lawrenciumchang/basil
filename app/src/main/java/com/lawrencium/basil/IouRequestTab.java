package com.lawrencium.basil;

/**
 * Created by James on 3/23/2015.
 */
public class IouRequestTab {
    private String UserOwed;
    private String UserOwing;
    private double AmountOwed;
    private String Category;
    private String Title;
    private Tab createdTab;
    private int tabID;

    public IouRequestTab(String userOwed, String userOwing, double amountOwed, String category, String title) {
        UserOwed = userOwed;
        UserOwing = userOwing;
        AmountOwed = amountOwed;
        Category = category;
        Title = title;
    }

    public void createTab() {
        createdTab = new Tab(UserOwed, UserOwing, AmountOwed, Category, Title);
        createdTab.setTabId(tabID);
        tabID++;
    }

    public Tab getCreatedTab() {
        return createdTab;
    }
}
