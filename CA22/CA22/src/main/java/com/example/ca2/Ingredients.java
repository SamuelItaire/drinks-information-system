package com.example.ca2;

public class Ingredients {
    private String name;
    private String description;
    private double abv;

    public Ingredients(String name, String description) {
        this.name = name;
        this.description = description;
        this.abv = abv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    @Override
    public String toString() {
        return name + " - " + description;}
}