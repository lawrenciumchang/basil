package com.lawrencium.basil;

import java.util.ArrayList;

/**
 * Created by James on 3/24/2015.
 */
public class TabVault {
    private String User;
    private ArrayList<Tab> Tabs = new ArrayList<Tab>();
    private volatile static TabVault ourInstance;
    private IouRequestTab TempIou = new IouRequestTab();
    private TabVault() {
    }

    public static TabVault getInstance() {
        if (ourInstance == null) {
            synchronized (TabVault.class) {
                if (ourInstance == null) {
                    ourInstance = new TabVault();
                }
            }
        }
        return ourInstance;
    }



    public String getUser() {
        return User;
    }



    public ArrayList<Tab> getTabs() {
        return Tabs;
    }

    public boolean addTab(Tab T){
        if(Tabs.add(T))
            return true;
        else
            return false;
    }

    public boolean removeTab(int tabId){
        for(Tab T : Tabs) {
            if (T.getTabId()== tabId) {
                if (Tabs.remove(T))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public boolean removeTab(Tab T){
        for(Tab tempTab : Tabs) {
            if (tempTab.equals(T)) {
                if (Tabs.remove(T))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public boolean containsTab(Tab T){
        if(Tabs.contains(T))
            return true;
        else
            return false;


    }

    public boolean containsTab(int tabId){
        for(Tab T : Tabs) {
            if (T.getTabId() == tabId) {
                return true;
            }
        }
        return false;
    }

    public IouRequestTab getTempIou() {
        return TempIou;
    }
}






