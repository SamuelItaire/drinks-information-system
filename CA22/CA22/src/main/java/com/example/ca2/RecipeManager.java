package com.example.ca2;

import java.util.Comparator;

public class RecipeManager {
    private LinkyList<Recipe> recipeList;
    private HashTable<String, Recipe> recipeHashTable;

    public RecipeManager(int hashCapacity) {
        this.recipeList = new LinkyList<>();
        this.recipeHashTable = new HashTable<>(hashCapacity);
    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
        recipeHashTable.put(recipe.getDrinkName(), recipe);
    }

    public boolean deleteRecipe(String drinkName) {
        Recipe recipe = recipeHashTable.get(drinkName);
        if (recipe != null) {
            recipeList.remove(recipe);
            recipeHashTable.remove(drinkName);
            return true;
        }
        return false;
    }

    public Recipe searchByDrinkName(String drinkName) {
        return recipeHashTable.get(drinkName);
    }

    public boolean addIngredientToRecipe(String drinkName, Ingredients ingredient, int quantity) {
        Recipe recipe = recipeHashTable.get(drinkName);
        if (recipe != null) {
            recipe.addIngredient(ingredient, quantity);
            return true;
        }
        return false;
    }
    public void reset() {
        recipeList.clear(); // Clear the list of recipes
    }

    public boolean removeIngredientFromRecipe(String drinkName, String ingredientName) {
        Recipe recipe = recipeHashTable.get(drinkName);
        if (recipe != null) {
            return recipe.removeIngredient(ingredientName);
        }
        return false;
    }

    public LinkyList<Recipe> searchByIngredient(String ingredientName) {
        LinkyList<Recipe> results = new LinkyList<>();
        for (int i = 0; i < recipeList.size(); i++) {
            Recipe recipe = recipeList.get(i);
            for (int j = 0; j < recipe.getIngredientsList().size(); j++) {
                Recipe.RecipeNode node = recipe.getIngredientsList().get(j);
                if (node.getIngredient().getName().equalsIgnoreCase(ingredientName)) {
                    results.add(recipe);
                    break;
                }
            }
        }
        return results;
    }

    public void sortByDrinkName() {
        recipeList.selectionSort(Comparator.comparing(Recipe::getDrinkName, String.CASE_INSENSITIVE_ORDER));
    }

    public LinkyList<Recipe> getAllRecipes() {
        return recipeList;
    }
}
