package com.surya.bakingapp.models;

public class ItemModel {

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String name;

}
