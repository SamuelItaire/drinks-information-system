package com.example.ca2;

import java.util.Comparator;

public class IngredientManager {
    private LinkyList<Ingredients> ingredientList;
    private HashTable<String, Ingredients> ingredientHashTable;

    public IngredientManager(int hashCapacity) {
        this.ingredientList = new LinkyList<>();
        this.ingredientHashTable = new HashTable<>(hashCapacity);
    }

    public void addIngredient(Ingredients ingredient) {
        ingredientList.add(ingredient);
        ingredientHashTable.put(ingredient.getName(), ingredient);
    }

    public void updateIngredient(Ingredients oldIngredient, Ingredients newIngredient) {
        deleteIngredient(oldIngredient);
        addIngredient(newIngredient);
    }

    public void deleteIngredient(Ingredients ingredient) {
        ingredientList.remove(ingredient);
        ingredientHashTable.remove(ingredient.getName());
    }

    public Ingredients searchByName(String name) {
        return ingredientHashTable.get(name);
    }

    public LinkyList<Ingredients> searchByNameKeyword(String query) {
        LinkyList<Ingredients> results = new LinkyList<>();
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(ingredientList.get(i));
            }
        }
        return results;
    }

    public LinkyList<Ingredients> searchByDescription(String query) {
        LinkyList<Ingredients> results = new LinkyList<>();
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getDescription().toLowerCase().contains(query.toLowerCase())) {
                results.add(ingredientList.get(i));
            }
        }
        return results;
    }
    public void reset() {
        ingredientList.clear(); // Clear the list of ingredients
    }

    public void sortByName() {
        ingredientList.selectionSort(Comparator.comparing(Ingredients::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public LinkyList<Ingredients> getAllIngredients() {
        return ingredientList;
    }
}
