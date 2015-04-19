package com.lawrencium.basil;

/**
 * Created by James on 3/23/2015.
 */
public class IouRequestTab {

    private Tab createdTab;
    private int tabID = 0;
    //may need to think of a way to handle IOUs vs YOMs without changing constructor order
    private volatile static IouRequestTab uniqueInstance;

    private IouRequestTab(){}

    public static IouRequestTab getInstance(){
        if(uniqueInstance == null){
            synchronized (IouRequestTab.class){
                if(uniqueInstance == null)
                    uniqueInstance = new IouRequestTab();
            }
        }
        return uniqueInstance;

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
