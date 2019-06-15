package com.surya.bakingapp.models;

public class IngredientsModel {

    String name;

    public IngredientsModel(String name, String quantity, String mesure) {
        this.name = name;
        this.quantity = quantity;
        this.mesure = mesure;
    }

    String quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMesure() {
        return mesure;
    }

    public void setMesure(String mesure) {
        this.mesure = mesure;
    }

    String mesure;

}
