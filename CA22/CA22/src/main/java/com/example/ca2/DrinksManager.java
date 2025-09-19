package com.example.ca2;

import java.util.Comparator;

public class DrinksManager {
    private LinkyList<Drinks> drinksList;
    private HashTable<String, Drinks> drinksHashTable;

    public DrinksManager(int hashCapacity) {
        this.drinksList = new LinkyList<>();
        this.drinksHashTable = new HashTable<>(hashCapacity);
    }

    // Add a new drink
    public void addDrink(Drinks drink) {
        drinksList.add(drink); // Add to linked list
        drinksHashTable.put(drink.getDrinkName(), drink); // Add to hash table with name as key
    }

    // Update an existing drink
    public void updateDrink(Drinks oldDrink, Drinks newDrink) {
        deleteDrink(oldDrink); // Remove the old drink
        addDrink(newDrink); // Add the updated drink
    }

    // Delete a drink
    public void deleteDrink(Drinks drink) {
        drinksList.remove(drink); // Remove from linked list
        drinksHashTable.remove(drink.getDrinkName()); // Remove from hash table using name as key
    }

    // Search for drinks by name (partial match)
    public LinkyList<Drinks> searchByName(String query) {
        LinkyList<Drinks> results = new LinkyList<>();
        for (int i = 0; i < drinksList.size(); i++) {
            if (drinksList.get(i).getDrinkName().toLowerCase().contains(query.toLowerCase())) {
                results.add(drinksList.get(i));
            }
        }
        return results;
    }

    // Search for drinks by a specific ingredient
    public LinkyList<Drinks> searchByIngredient(String ingredient) {
        LinkyList<Drinks> results = new LinkyList<>();
        for (int i = 0; i < drinksList.size(); i++) {
            if (drinksList.get(i).getIngredients().toLowerCase().contains(ingredient.toLowerCase())) {
                results.add(drinksList.get(i));
            }
        }
        return results;
    }

    // Search for drinks by ABV (equal to or lower than the specified value)
    public LinkyList<Drinks> searchByAbv(double maxAbv) {
        LinkyList<Drinks> results = new LinkyList<>();
        for (int i = 0; i < drinksList.size(); i++) {
            Drinks drink = drinksList.get(i);
            if (drink.getAbv() <= maxAbv) {
                results.add(drink);
            }
        }
        return results;
    }

    public void reset() {
        drinksList.clear(); // Clear the list of drinks
    }

    // Sort drinks by name
    public void sortByName() {
        drinksList.selectionSort(Comparator.comparing(Drinks::getDrinkName));
    }

    // Sort drinks by ABV
    public void sortByAbv() {
        drinksList.selectionSort(Comparator.comparingDouble(Drinks::getAbv));
    }

    // Get all drinks
    public LinkyList<Drinks> getAllDrinks() {
        return drinksList;
    }

    // Display all drinks (for CLI debugging)
    public void displayAllDrinks() {
        drinksList.display();
    }
}
