package com.example.ca2;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
    private HashTable<String, Drinks> drinksTable;
    private HashTable<String, Ingredients> ingredientsTable;
    private HashTable<String, Recipe> recipeTable;

    public Driver() {
        this.drinksTable = new HashTable<>(50); // Initialize hash table for drinks
        this.ingredientsTable = new HashTable<>(50); // Initialize hash table for ingredients
        this.recipeTable = new HashTable<>(50); // Initialize hash table for recipes
    }

    // Getters and Setters
    public HashTable<String, Drinks> getDrinksTable() {
        return drinksTable;
    }

    public HashTable<String, Ingredients> getIngredientsTable() {
        return ingredientsTable;
    }

    public HashTable<String, Recipe> getRecipeTable() {
        return recipeTable;
    }

    public void setDrinksTable(HashTable<String, Drinks> drinksTable) {
        this.drinksTable = drinksTable;
    }

    public void setIngredientsTable(HashTable<String, Ingredients> ingredientsTable) {
        this.ingredientsTable = ingredientsTable;
    }

    public void setRecipeTable(HashTable<String, Recipe> recipeTable) {
        this.recipeTable = recipeTable;
    }

    // Drinks Management
    public void addDrink(Drinks drink) {
        drinksTable.put(drink.getDrinkName(), drink);
    }

    public void removeDrink(Drinks drink) {
        recipeTable.traverse((key, recipe) -> {
            if (recipe.getDrinkName().equals(drink.getDrinkName())) {
                removeRecipe(recipe);
            }
        });
        drinksTable.remove(drink.getDrinkName());
    }

    public void updateDrink(Drinks oldDrink, Drinks newDrink) {
        removeDrink(oldDrink);
        addDrink(newDrink);
    }

    public Drinks searchDrink(String name) {
        return drinksTable.get(name);
    }

    // Ingredients Management
    public void addIngredient(Ingredients ingredient) {
        ingredientsTable.put(ingredient.getName(), ingredient);
    }

    public void removeIngredient(Ingredients ingredient) {
        // Step 1: Iterate over the recipeTable to find and update recipes containing the ingredient
        recipeTable.traverse((drinkName, recipe) -> {
            LinkyList<Recipe.RecipeNode> ingredientsList = recipe.getIngredientsList();

            for (int i = 0; i < ingredientsList.size(); i++) {
                Recipe.RecipeNode node = ingredientsList.get(i);
                if (node.getIngredient().getName().equalsIgnoreCase(ingredient.getName())) {
                    recipe.removeIngredient(ingredient.getName());
                }
            }
        });

        // Step 2: Remove the ingredient from the ingredientsTable
        if (ingredientsTable.containsKey(ingredient.getName())) {
            ingredientsTable.remove(ingredient.getName());
            System.out.println("Ingredient '" + ingredient.getName() + "' removed successfully.");
        } else {
            System.out.println("Ingredient '" + ingredient.getName() + "' not found in the ingredients table.");
        }
    }



    public void updateIngredient(Ingredients oldIngredient, Ingredients newIngredient) {
        removeIngredient(oldIngredient);
        addIngredient(newIngredient);
    }

    public Ingredients searchIngredient(String name) {
        return ingredientsTable.get(name);
    }

    // Recipe Management
    public void addRecipe(Recipe recipe) {
        recipeTable.put(recipe.getDrinkName(), recipe);
    }

    public void removeRecipe(Recipe recipe) {
        recipeTable.remove(recipe.getDrinkName());
    }

    public void updateRecipe(Recipe oldRecipe, Recipe newRecipe) {
        removeRecipe(oldRecipe);
        addRecipe(newRecipe);
    }

    public Recipe searchRecipe(String drinkName) {
        return recipeTable.get(drinkName);
    }

    // Save and Load Data
    public void save(String filename) {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("Driver", Driver.class);
        xstream.alias("Drinks", Drinks.class);
        xstream.alias("Ingredients", Ingredients.class);
        xstream.alias("Recipe", Recipe.class);
        xstream.alias("HashTable", HashTable.class);

        try (FileWriter writer = new FileWriter(filename)) {
            xstream.toXML(this, writer);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void load(String filename) {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("Driver", Driver.class);
        xstream.alias("Drinks", Drinks.class);
        xstream.alias("Ingredients", Ingredients.class);
        xstream.alias("Recipe", Recipe.class);
        xstream.alias("HashTable", HashTable.class);

        try (FileReader reader = new FileReader(filename)) {
            Driver loadedData = (Driver) xstream.fromXML(reader);
            this.drinksTable = loadedData.getDrinksTable();
            this.ingredientsTable = loadedData.getIngredientsTable();
            this.recipeTable = loadedData.getRecipeTable();
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    // Reset All Data
    public void reset() {
        drinksTable = new HashTable<>(50);
        ingredientsTable = new HashTable<>(50);
        recipeTable = new HashTable<>(50);
        System.out.println("All data has been reset successfully.");
    }
}
