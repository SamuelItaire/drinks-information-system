package com.example.ca2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IngredientsTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String expectedName = "Sugar";
        String expectedDescription = "Sweetener";

        // Act
        Ingredients ingredient = new Ingredients(expectedName, expectedDescription);

        // Assert
        assertEquals(expectedName, ingredient.getName(), "Name should match the one provided in the constructor.");
        assertEquals(expectedDescription, ingredient.getDescription(), "Description should match the one provided in the constructor.");
        assertEquals(0.0, ingredient.getAbv(), "ABV should default to 0.0.");
    }

    @Test
    void testSetName() {
        // Arrange
        Ingredients ingredient = new Ingredients("Sugar", "Sweetener");
        String newName = "Salt";

        // Act
        ingredient.setName(newName);

        // Assert
        assertEquals(newName, ingredient.getName(), "Name should be updated.");
    }

    @Test
    void testSetDescription() {
        // Arrange
        Ingredients ingredient = new Ingredients("Sugar", "Sweetener");
        String newDescription = "Preservative";

        // Act
        ingredient.setDescription(newDescription);

        // Assert
        assertEquals(newDescription, ingredient.getDescription(), "Description should be updated.");
    }

    @Test
    void testSetAbv() {
        // Arrange
        Ingredients ingredient = new Ingredients("Sugar", "Sweetener");
        double newAbv = 12.5;

        // Act
        ingredient.setAbv(newAbv);

        // Assert
        assertEquals(newAbv, ingredient.getAbv(), "ABV should be updated.");
    }

    @Test
    void testToString() {
        // Arrange
        Ingredients ingredient = new Ingredients("Sugar", "Sweetener");
        String expectedString = "Sugar - Sweetener";

        // Act
        String actualString = ingredient.toString();

        // Assert
        assertEquals(expectedString, actualString, "toString() output should match the expected format.");
    }
}
