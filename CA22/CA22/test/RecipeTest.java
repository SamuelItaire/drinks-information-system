package com.example.ca2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe("Mojito");
    }

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String expectedDrinkName = "Mojito";

        // Act & Assert
        assertEquals(expectedDrinkName, recipe.getDrinkName(), "Drink name should match the one provided in the constructor.");
        assertEquals(0, recipe.getIngredientsList().size(), "Ingredients list should initially be empty."); // Fixed here
    }

    @Test
    void testSetDrinkName() {
        // Arrange
        String newDrinkName = "Daiquiri";

        // Act
        recipe.setDrinkName(newDrinkName);

        // Assert
        assertEquals(newDrinkName, recipe.getDrinkName(), "Drink name should be updated.");
    }

    @Test
    void testAddIngredient() {
        // Arrange
        Ingredients ingredient = new Ingredients("Rum", "Alcoholic spirit");
        int quantity = 50;

        // Act
        recipe.addIngredient(ingredient, quantity);

        // Assert
        assertEquals(1, recipe.getIngredientsList().size(), "Ingredients list size should increase by 1.");
        Recipe.RecipeNode node = recipe.getIngredientsList().get(0);
        assertEquals(ingredient, node.getIngredient(), "Ingredient should match the one added.");
        assertEquals(quantity, node.getQuantity(), "Quantity should match the one provided.");
    }

    @Test
    void testUpdateIngredient() {
        // Arrange
        Ingredients ingredient1 = new Ingredients("Rum", "Alcoholic spirit");
        Ingredients ingredient2 = new Ingredients("Vodka", "Clear alcoholic spirit");
        int quantity1 = 50;
        int quantity2 = 60;
        recipe.addIngredient(ingredient1, quantity1);

        // Act
        boolean isUpdated = recipe.updateIngredient("Rum", ingredient2, quantity2);

        // Assert
        assertTrue(isUpdated, "Ingredient should be updated successfully.");
        Recipe.RecipeNode node = recipe.getIngredientsList().get(0);
        assertEquals(ingredient2, node.getIngredient(), "Ingredient should be updated to the new ingredient.");
        assertEquals(quantity2, node.getQuantity(), "Quantity should be updated to the new quantity.");
    }

    @Test
    void testUpdateIngredientNotFound() {
        // Arrange
        Ingredients ingredient = new Ingredients("Rum", "Alcoholic spirit");
        int quantity = 50;
        recipe.addIngredient(ingredient, quantity);

        // Act
        boolean isUpdated = recipe.updateIngredient("Gin", new Ingredients("Gin", "Alcoholic spirit"), 30);

        // Assert
        assertFalse(isUpdated, "Updating a non-existent ingredient should return false.");
    }

    @Test
    void testRemoveIngredient() {
        // Arrange
        Ingredients ingredient = new Ingredients("Rum", "Alcoholic spirit");
        int quantity = 50;
        recipe.addIngredient(ingredient, quantity);

        // Act
        boolean isRemoved = recipe.removeIngredient("Rum");

        // Assert
        assertTrue(isRemoved, "Ingredient should be removed successfully.");
        assertEquals(0, recipe.getIngredientsList().size(), "Ingredients list should be empty after removal."); // Fixed here
    }

    @Test
    void testRemoveIngredientNotFound() {
        // Arrange
        Ingredients ingredient = new Ingredients("Rum", "Alcoholic spirit");
        int quantity = 50;
        recipe.addIngredient(ingredient, quantity);

        // Act
        boolean isRemoved = recipe.removeIngredient("Gin");

        // Assert
        assertFalse(isRemoved, "Removing a non-existent ingredient should return false.");
    }

    @Test
    void testGetFormattedIngredients() {
        // Arrange
        recipe.addIngredient(new Ingredients("Rum", "Alcoholic spirit"), 50);
        recipe.addIngredient(new Ingredients("Lime Juice", "Citrus juice"), 20);

        String expected = "Rum - 50 ml\nLime Juice - 20 ml";

        // Act
        String actual = recipe.getFormattedIngredients();

        // Assert
        assertEquals(expected, actual, "Formatted ingredients string should match the expected format.");
    }

    @Test
    void testGetIngredientsListAsString() {
        // Arrange
        recipe.addIngredient(new Ingredients("Rum", "Alcoholic spirit"), 50);
        recipe.addIngredient(new Ingredients("Lime Juice", "Citrus juice"), 20);

        String expected = "Rum (50), Lime Juice (20)";

        // Act
        String actual = recipe.getIngredientsListAsString();

        // Assert
        assertEquals(expected, actual, "Ingredients list as string should match the expected format.");
    }
}
