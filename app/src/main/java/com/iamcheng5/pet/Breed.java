package com.iamcheng5.pet;

public class Breed {
    private int id;
    private String name;
    private String[] feature;
    private String url;

    public Breed(int id, String name, String[] feature, String url) {
        this.id = id;
        this.name = name;
        this.feature = feature;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFeature() {
        return feature;
    }

    public void setFeature(String[] feature) {
        this.feature = feature;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
