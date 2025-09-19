package com.example.ca2;

public class Drinks {
    private String DrinkName;
    private String origin;
    private String description;
    private String ingredients; // Legacy field, not used for cascading.
    private double abv;
    private Recipe recipe; // Associated recipe

    public Drinks(String DrinkName, String origin, String description, String ingredients, double abv) {
        this.DrinkName = DrinkName;
        this.origin = origin;
        this.description = description;
        this.ingredients = ingredients;
        this.abv = abv;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDrinkName() {
        return DrinkName;
    }

    public void setName(String name) {
        this.DrinkName = DrinkName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return DrinkName + " (" + origin + ") - " + description + " (ABV: " + abv + "%)" + "\nIngredients: " + ingredients;
    }

}