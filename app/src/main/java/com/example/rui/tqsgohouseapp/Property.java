package com.example.rui.tqsgohouseapp;

import android.location.Location;
import android.support.design.internal.ParcelableSparseArray;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by rui on 04-06-2018.
 */

public class Property implements Serializable{

    private String name;
    private String owner;
    private String address;
    private int nRooms;

    private int id;

    private double Lat;
    private double Lng;


    public Property(String name, String owner, String address, int nRooms, double Lat, double Lng){
        this.name = name;
        this.owner = owner;
        this.address = address;
        this.nRooms = nRooms;
        this.Lng = Lng;
        this.Lat = Lat;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return "Address: " + address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getnRooms() {
        return nRooms;
    }

    public void setnRooms(int nRooms) {
        this.nRooms = nRooms;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String toString(){
        return "Name: " + name + "\t"
                + "Address:" + address + "\n";
    }

}
