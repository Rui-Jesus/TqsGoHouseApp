package com.example.rui.tqsgohouseapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by rui on 06-06-2018.
 */
// Java program implementing Singleton class
// with getInstance() method
class DataHolder
{
    // static variable single_instance of type Singleton
    private static DataHolder single_instance = null;

    private ArrayList<Property> propertiesList = new ArrayList<>();
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<Property> getPropertiesList(){
        return (ArrayList<Property>) this.propertiesList.clone();
    }

    public void setPropertiesList(ArrayList<Property> properties){
        this.propertiesList = properties;
    }

    public void removeProperty(int id){
        int c = 0;
        for(Property p : propertiesList){
            if(p.getId() == id){
                propertiesList.remove(c);
                break;
            }
            c++;
        }
    }

    // private constructor restricted to this class itself
    private DataHolder() {}

    // static method to create instance of Singleton class
    public static DataHolder getInstance()
    {
        if (single_instance == null)
            single_instance = new DataHolder();

        return single_instance;
    }
}
