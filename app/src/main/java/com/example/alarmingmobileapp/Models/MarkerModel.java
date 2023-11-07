package com.example.alarmingmobileapp.Models;

public class MarkerModel {
    static String latitude;
    static String longtitude;
    static String name;
    static Integer radius;

    public MarkerModel(){}


    public MarkerModel(String latitude, String longtitude, Integer radius, String name) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.radius = radius;
        this.name = name;
    }

    public static String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public static String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public static Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
