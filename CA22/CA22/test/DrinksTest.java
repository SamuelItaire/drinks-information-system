package com.example.ca2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DrinksTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String drinkName = "Mojito";
        String origin = "Cuba";
        String description = "A refreshing cocktail with lime and mint";
        String ingredients = "Lime, Mint, Rum, Soda Water, Sugar";
        double abv = 13.5;

        // Act
        Drinks drink = new Drinks(drinkName, origin, description, ingredients, abv);

        // Assert
        assertEquals(drinkName, drink.getDrinkName());
        assertEquals(origin, drink.getOrigin());
        assertEquals(description, drink.getDescription());
        assertEquals(ingredients, drink.getIngredients());
        assertEquals(abv, drink.getAbv(), 0.01);
    }

    @Test
    void testSetters() {
        // Arrange
        Drinks drink = new Drinks("Old Fashioned", "USA", "A whiskey cocktail", "Whiskey, Sugar, Bitters, Orange Peel", 40.0);

        // Act
        drink.setName("Martini");
        drink.setOrigin("Italy");
        drink.setDescription("A classic gin and vermouth cocktail");
        drink.setIngredients("Gin, Vermouth, Olive");
        drink.setAbv(15.0);

        // Assert
        assertEquals("Martini", drink.getDrinkName());
        assertEquals("Italy", drink.getOrigin());
        assertEquals("A classic gin and vermouth cocktail", drink.getDescription());
        assertEquals("Gin, Vermouth, Olive", drink.getIngredients());
        assertEquals(15.0, drink.getAbv(), 0.01);
    }

    @Test
    void testRecipeAssociation() {
        // Arrange
        Recipe recipe = new Recipe("Martini Recipe"); // Pass only one String argument
        Drinks drink = new Drinks("Martini", "Italy", "A classic gin and vermouth cocktail", "Gin, Vermouth, Olive", 15.0);

        // Act
        drink.setRecipe(recipe);

        // Assert
        assertEquals(recipe, drink.getRecipe());
    }

    @Test
    void testToString() {
        // Arrange
        String drinkName = "Margarita";
        String origin = "Mexico";
        String description = "A tangy tequila-based cocktail";
        String ingredients = "Tequila, Lime Juice, Triple Sec, Salt";
        double abv = 12.5;
        Drinks drink = new Drinks(drinkName, origin, description, ingredients, abv);

        // Act
        String result = drink.toString();

        // Assert
        String expected = "Margarita (Mexico) - A tangy tequila-based cocktail (ABV: 12.5%)\nIngredients: Tequila, Lime Juice, Triple Sec, Salt";
        assertEquals(expected, result);
    }
}
