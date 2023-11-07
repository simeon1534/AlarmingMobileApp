package com.example.alarmingmobileapp.Models;

public class MarkerModel {
    String latitude,longtitude,name;
    Integer radius;

    public MarkerModel(){}


    public MarkerModel(String latitude, String longtitude, Integer radius, String name) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.radius = radius;
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
