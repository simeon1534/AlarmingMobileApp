package com.example.alarmingmobileapp.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "markers")
public class MarkerModel {
    @PrimaryKey(autoGenerate = true)
    private int key;

    @ColumnInfo(name="latitude")
    private double latitude;

    @ColumnInfo(name="longtitude")
    private double longtitude;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name ="radius")
    private int radius;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
