package com.example.ca2;

public class Recipe {
    private String drinkName;
    private LinkyList<RecipeNode> ingredientsList;

    public Recipe(String drinkName) {
        this.drinkName = drinkName;
        this.ingredientsList = new LinkyList<>();
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public LinkyList<RecipeNode> getIngredientsList() {
        return ingredientsList;
    }


    public void addIngredient(Ingredients ingredient, int quantity) {
        RecipeNode newNode = new RecipeNode(ingredient, quantity);
        ingredientsList.add(newNode);
    }
    public boolean updateIngredient(String oldIngredientName, Ingredients newIngredient, int newQuantity) {
        for (int i = 0; i < ingredientsList.size(); i++) {
            RecipeNode current = ingredientsList.get(i);
            if (current.getIngredient().getName().equalsIgnoreCase(oldIngredientName)) {
                // Update the ingredient and quantity
                current.ingredient = newIngredient;
                current.quantity = newQuantity;
                return true;
            }
        }
        return false; // Ingredient not found
    }

    public boolean removeIngredient(String ingredientName) {
        for (int i = 0; i < ingredientsList.size(); i++) {
            RecipeNode current = ingredientsList.get(i);
            if (current.getIngredient().getName().equalsIgnoreCase(ingredientName)) {
                ingredientsList.remove(current);
                return true;
            }
        }
        return false;
    }

    public String getFormattedIngredients() {
        String result = "";
        for (int i = 0; i < ingredientsList.size(); i++) {
            RecipeNode current = ingredientsList.get(i);
            result += current.getIngredient().getName() + " - " + current.getQuantity() + " ml\n";
        }
        return result.trim();
    }

    public static class RecipeNode {
        private Ingredients ingredient;
        private int quantity;

        public RecipeNode(Ingredients ingredient, int quantity) {
            this.ingredient = ingredient;
            this.quantity = quantity;
        }

        public Ingredients getIngredient() {
            return ingredient;
        }

        public int getQuantity() {
            return quantity;
        }
    }
    public String getIngredientsListAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ingredientsList.size(); i++) {
            RecipeNode node = ingredientsList.get(i);
            sb.append(node.getIngredient().getName())
                    .append(" (")
                    .append(node.getQuantity())
                    .append(")");
            if (i < ingredientsList.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
