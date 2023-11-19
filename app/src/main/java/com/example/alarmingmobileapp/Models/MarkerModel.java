package com.example.alarmingmobileapp.Models;

public class MarkerModel {

    Double latitude;
    Double longtitude;
    String name;
    Integer radius;

    public MarkerModel(){}

    public MarkerModel(Double latitude, Double longtitude, String name, Integer radius) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
        this.radius = radius;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
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
