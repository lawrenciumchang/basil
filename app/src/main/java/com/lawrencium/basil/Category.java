package com.lawrencium.basil;

/**
 * Created by Evan on 3/23/2015.
 */
class Category {
    private final String name;
    private final double budgetValue;

    public Category(String name, double budget){
        this.name = name;
        this.budgetValue = budget;
    }

    public String getName(){
        return name;
    }
}
