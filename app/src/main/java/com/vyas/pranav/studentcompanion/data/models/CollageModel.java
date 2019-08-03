package com.vyas.pranav.studentcompanion.data.models;

public class CollageModel {
    private String name, city;

    public CollageModel() {
    }

    public CollageModel(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
