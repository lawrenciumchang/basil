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
    //may need to think of a way to handle IOUs vs YOMs without changing constructor order
    public IouRequestTab() {
        tabID = 0;
    }

    public void createTab(String userOwed, String userOwing, double amountOwed, String category, String title) {
        createdTab = new Tab(userOwed, userOwing, amountOwed, category, title);
        createdTab.setTabId(tabID);
        tabID++;
    }

    public Tab getCreatedTab() {
        return createdTab;
    }
}
