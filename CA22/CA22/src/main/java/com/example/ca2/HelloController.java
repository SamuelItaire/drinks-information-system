package com.example.ca2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.Comparator;

public class HelloController {

    // ==== Shared Fields ====
    @FXML
    private TextField DrinkNameField, descriptionField, originField, IngredientsField, abvField, searchField, chosenIngredient;
    @FXML
    private TextField nameIngredientField, descriptionIngredientField, searchIngredientField, searchRecipeField;
    @FXML
    private TextArea detailsBox, detailsIngredientBox, detailsRecipeBox;
    @FXML
    private ListView<String> drinksList;
    @FXML
    private ListView<String> ingredientList;
    @FXML
    private ListView<String> recipesListView;
    @FXML
    private ListView<String> selectedIngredientsList;

    // Managers for Drinks, Ingredients, and Recipes
    private DrinksManager drinksManager = new DrinksManager(10);
    private IngredientManager ingredientManager = new IngredientManager(10);
    private RecipeManager recipeManager = new RecipeManager(10);

    // Data files for persistence
    private String DRINKS_DATA_FILE = "drinks_data.txt";
    private String INGREDIENTS_DATA_FILE = "ingredients_data.txt";
    private String RECIPES_DATA_FILE = "recipes_data.txt";

    // ==== Scene Navigation ====
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            String resourcePath = "/com/example/ca2/" + fxmlFile;
            System.out.println("Loading FXML: " + resourcePath); // Debugging output
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to load the requested scene: " + fxmlFile + "\n" + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // Debugging error stack trace
        }
    }

    @FXML
    public void navigateToDrinks(ActionEvent event) {
        switchScene(event, "Drink.fxml");
    }

    @FXML
    public void navigateToIngredients(ActionEvent event) {
        switchScene(event, "Ingredients.fxml");
    }

    @FXML
    public void navigateToRecipes(ActionEvent event) {
        switchScene(event, "Recipe.fxml");
    }

    @FXML
    public void navigateToMainScreen(ActionEvent event) {
        switchScene(event, "hello-view.fxml");
    }

    // ==== Shared Methods ====
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        DrinkNameField.clear();
        originField.clear();
        IngredientsField.clear();
        abvField.clear();
        descriptionField.clear();
        searchField.clear();
        nameIngredientField.clear();
        descriptionIngredientField.clear();
        searchIngredientField.clear();
    }

    @FXML
    public void initialize() {
        // Load data from files
        loadDrinks();
        loadIngredients();
        loadRecipes();

        // Add listeners for selection changes
        if (drinksList != null) {
            drinksList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Drinks selectedDrink = drinksManager.searchByName(newValue).get(0);
                    if (selectedDrink != null) {
                        detailsBox.setText(selectedDrink.toString());
                    }
                }
            });
        }

        if (ingredientList != null) {
            ingredientList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Ingredients selectedIngredient = ingredientManager.searchByName(newValue);
                    if (selectedIngredient != null) {
                        detailsIngredientBox.setText(selectedIngredient.toString());
                    }
                }
            });
        }

        if (recipesListView != null) {
            recipesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Recipe selectedRecipe = recipeManager.searchByDrinkName(newValue);
                    if (selectedRecipe != null) {
                        detailsRecipeBox.setText("Recipe for: " + selectedRecipe.getDrinkName() + "\n" +
                                selectedRecipe.getIngredientsListAsString());
                    }
                }
            });
        }

        // Populate the left-hand side ListView with drinks in the Recipes tab
        updateDrinksListInRecipesTab();
    }
    // ==== Drinks Methods ====
    public void loadDrinks() {
        File file = new File(DRINKS_DATA_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 5) {
                        String name = parts[0];
                        String origin = parts[1];
                        String description = parts[2];
                        String ingredients = parts[3];
                        double abv = Double.parseDouble(parts[4]);
                        drinksManager.addDrink(new Drinks(name, origin, description, ingredients, abv));
                    }
                }
                updateDrinksListView();
                updateRecipesListView(); // Ensure the recipeListView is updated
            } catch (IOException | NumberFormatException e) {
                showAlert("Error", "Failed to load drinks from file.", Alert.AlertType.ERROR);
            }
        }
    }

    public void saveDrinks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DRINKS_DATA_FILE))) {
            LinkyList<Drinks> allDrinks = drinksManager.getAllDrinks();
            for (int i = 0; i < allDrinks.size(); i++) {
                Drinks drink = allDrinks.get(i);
                String line = String.format("%s;%s;%s;%s;%f",
                        drink.getDrinkName(),
                        drink.getOrigin(),
                        drink.getDescription(),
                        drink.getIngredients(),
                        drink.getAbv());
                writer.write(line);
                writer.newLine();
            }
            showAlert("Success", "Drinks have been successfully saved.", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Error", "Failed to save drinks to file.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onAddDrink() {
        try {
            String name = DrinkNameField.getText();
            String origin = originField.getText();
            String description = descriptionField.getText();
            String ingredients = IngredientsField.getText();
            double abv = Double.parseDouble(abvField.getText());
            drinksManager.addDrink(new Drinks(name, origin, description, ingredients, abv));
            clearFields();
            updateDrinksListView();
            updateRecipesListView();
            updateDrinksListInRecipesTab(); // Update the drinks list in the Recipes tab
            showAlert("Success", "Drink has been added.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid ABV.", Alert.AlertType.ERROR);
        }
    }

    public void onUpdateDrink() {
        String selectedDrink = drinksList.getSelectionModel().getSelectedItem();
        if (selectedDrink != null) {
            Drinks oldDrink = drinksManager.searchByName(selectedDrink).get(0);
            if (oldDrink != null) {
                try {
                    Drinks updatedDrink = new Drinks(
                            DrinkNameField.getText(),
                            originField.getText(),
                            descriptionField.getText(),
                            IngredientsField.getText(),
                            Double.parseDouble(abvField.getText())
                    );
                    drinksManager.updateDrink(oldDrink, updatedDrink);
                    updateDrinksListView();
                    updateRecipesListView();
                    updateDrinksListInRecipesTab(); // Update the drinks list in the Recipes tab
                    showAlert("Success", "Drink has been updated.", Alert.AlertType.INFORMATION);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid ABV.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Error", "Please select a drink to update.", Alert.AlertType.INFORMATION);
        }
    }

    public void onDeleteDrink() {
        String selectedDrink = drinksList.getSelectionModel().getSelectedItem();
        if (selectedDrink != null) {
            Drinks drink = drinksManager.searchByName(selectedDrink).get(0);
            if (drink != null) {
                drinksManager.deleteDrink(drink);
                updateDrinksListView();
                updateDrinksListInRecipesTab(); // Update the drinks list in the Recipes tab
                showAlert("Success", "Drink has been deleted.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Drink not found.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Error", "Please select a drink to delete.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void onSearchDrinkByName() {
        // Use the searchRecipeField text field instead of searchField
        String query = searchRecipeField.getText().trim();
        if (!query.isEmpty()) {
            LinkyList<Drinks> results = drinksManager.searchByName(query);
            updateDrinksListView(results);
            showAlert("Search Results", results.size() + " drink(s) found with name matching \"" + query + "\".", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Please enter a name to search.", Alert.AlertType.INFORMATION);
        }
    }

    public void onSearchDrinkByIngredient() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            LinkyList<Drinks> results = drinksManager.searchByIngredient(query);
            updateDrinksListView(results);
            showAlert("Search Results", results.size() + " drink(s) found containing \"" + query + "\" as an ingredient.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Please enter an ingredient to search.", Alert.AlertType.INFORMATION);
        }
    }



    public void onSearchDrinkByAbv() {
        try {
            double maxAbv = Double.parseDouble(searchField.getText().trim());
            LinkyList<Drinks> results = drinksManager.searchByAbv(maxAbv);
            results.selectionSort(new Comparator<Drinks>() {
                @Override
                public int compare(Drinks drink1, Drinks drink2) {
                    return Double.compare(drink2.getAbv(), drink1.getAbv());
                }});
            updateDrinksListView(results);
            showAlert("Search Complete", results.size() + " drink(s) found with ABV <= " + maxAbv + "%.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid ABV to search.", Alert.AlertType.INFORMATION);
        }
    }

    private void updateDrinksListView() {
        updateDrinksListView(drinksManager.getAllDrinks());
    }

    private void updateDrinksListView(LinkyList<Drinks> drinks) {
        if (drinksList != null) {
            drinksList.getItems().clear();
            for (int i = 0; i < drinks.size(); i++) {
                drinksList.getItems().add(drinks.get(i).getDrinkName());
            }
        }
    }

    // ==== Ingredients Methods ====
    public void loadIngredients() {
        File file = new File(INGREDIENTS_DATA_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String name = parts[0];
                        String description = parts[1];
                        double abv = Double.parseDouble(parts[2]);
                        ingredientManager.addIngredient(new Ingredients(name, description));
                    }
                }
                updateIngredientsListView();
            } catch (IOException | NumberFormatException e) {
                showAlert("Error", "Failed to load ingredients from file.", Alert.AlertType.ERROR);
            }
        }
    }

    public void saveIngredients() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INGREDIENTS_DATA_FILE))) {
            LinkyList<Ingredients> allIngredients = ingredientManager.getAllIngredients();
            for (int i = 0; i < allIngredients.size(); i++) {
                Ingredients ingredient = allIngredients.get(i);
                writer.write(String.format("%s;%s;%f%n", ingredient.getName(), ingredient.getDescription(), ingredient.getAbv()));
            }
            showAlert("Success", "Ingredients have been successfully saved.", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Error", "Failed to save ingredients to file.", Alert.AlertType.ERROR);
        }
    }
    
    public void onAddIngredient() {
        try {
            // Use the correct fields for ingredients
            String name = nameIngredientField.getText().trim();
            String description = descriptionIngredientField.getText().trim();

            // Validate that the name and description fields are not empty
            if (name.isEmpty() || description.isEmpty()) {
                showAlert("Error", "Please enter a valid name and description.", Alert.AlertType.ERROR);
                return;
            }

            // Add the ingredient if all validations pass
            ingredientManager.addIngredient(new Ingredients(name, description)); // Remove ABV parameter
            clearFields();
            updateIngredientsListView(); // Ensure this is called
            showAlert("Success", "Ingredient has been added.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "An error occurred while adding the ingredient.", Alert.AlertType.ERROR);
            e.printStackTrace(); // Debugging: Print the stack trace
        }
    }

    public void onUpdateIngredient() {
        String selectedIngredient = ingredientList.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            Ingredients oldIngredient = ingredientManager.searchByName(selectedIngredient);
            if (oldIngredient != null) {
                try {
                    Ingredients updatedIngredient = new Ingredients(
                            nameIngredientField.getText(),
                            descriptionIngredientField.getText()
                    );
                    ingredientManager.updateIngredient(oldIngredient, updatedIngredient);
                    updateIngredientsListView();
                    showAlert("Success", "Ingredient has been updated.", Alert.AlertType.INFORMATION);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid ABV.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Error", "Please select an ingredient to update.", Alert.AlertType.INFORMATION);
        }
    }

    public void onDeleteIngredient() {
        String selectedIngredient = ingredientList.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            Ingredients ingredient = ingredientManager.searchByName(selectedIngredient);
            if (ingredient != null) {
                ingredientManager.deleteIngredient(ingredient);
                updateIngredientsListView();
                showAlert("Success", "Ingredient has been deleted.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Ingredient not found.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Error", "Please select an ingredient to delete.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void onSearchIngredientByName() {
        String query = searchIngredientField.getText().trim();
        if (!query.isEmpty()) {
            LinkyList<Ingredients> results = ingredientManager.searchByNameKeyword(query);
            updateIngredientsListView(results);
            showAlert("Search Results", results.size() + " ingredient(s) found with name containing \"" + query + "\".", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Please enter a name to search.", Alert.AlertType.INFORMATION);
        }
    }

    public void onSearchIngredientByDescription() {
        String query = searchIngredientField.getText().trim();
        if (!query.isEmpty()) {
            LinkyList<Ingredients> results = ingredientManager.searchByDescription(query);
            updateIngredientsListView(results);
            showAlert("Search Results", results.size() + " ingredient(s) found with description containing \"" + query + "\".", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Please enter a description to search.", Alert.AlertType.INFORMATION);
        }
    }

    private void updateIngredientsListView() {
        updateIngredientsListView(ingredientManager.getAllIngredients());
    }

    private void updateIngredientsListView(LinkyList<Ingredients> ingredients) {
        if (ingredientList != null) {
            ingredientList.getItems().clear(); // Clear the list before updating
            for (int i = 0; i < ingredients.size(); i++) {
                ingredientList.getItems().add(ingredients.get(i).getName()); // Add ingredient names
            }
        }
    }

    // ==== Recipes Methods ====
    public void loadRecipes() {
        File file = new File(RECIPES_DATA_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 1) {
                        Recipe recipe = new Recipe(parts[0]); // Create recipe with drink name
                        for (int i = 1; i < parts.length; i++) {
                            String[] ingredientData = parts[i].split(",");
                            if (ingredientData.length == 3) {
                                Ingredients ingredient = new Ingredients(ingredientData[0], ingredientData[1]);
                                recipe.addIngredient(ingredient, Integer.parseInt(ingredientData[3]));
                            }
                        }
                        recipeManager.addRecipe(recipe);
                    }
                }
                updateRecipesListView();
            } catch (IOException | NumberFormatException e) {
                showAlert("Error", "Failed to load recipes from file.", Alert.AlertType.ERROR);
            }
        }
    }

    public void onSaveRecipes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECIPES_DATA_FILE))) {
            LinkyList<Recipe> allRecipes = recipeManager.getAllRecipes();
            for (int i = 0; i < allRecipes.size(); i++) {
                Recipe recipe = allRecipes.get(i);
                StringBuilder line = new StringBuilder(recipe.getDrinkName());
                for (int j = 0; j < recipe.getIngredientsList().size(); j++) {
                    Recipe.RecipeNode node = recipe.getIngredientsList().get(j);
                    line.append(";").append(node.getIngredient().getName())
                            .append(",").append(node.getIngredient().getDescription())
                            .append(",").append(node.getIngredient().getAbv())
                            .append(",").append(node.getQuantity());
                }
                writer.write(line.toString());
                writer.newLine();
            }
            showAlert("Success", "Recipes have been successfully saved.", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Error", "Failed to save recipes to file.", Alert.AlertType.ERROR);
        }
    }

    private void updateDrinksListInRecipesTab() {
        if (recipesListView != null) {
            // Clear the list before updating
            recipesListView.getItems().clear();

            // Add all drinks to the list
            LinkyList<Drinks> allDrinks = drinksManager.getAllDrinks();
            for (int i = 0; i < allDrinks.size(); i++) {
                recipesListView.getItems().add(allDrinks.get(i).getDrinkName());
            }
        }
    }

    private void updateRecipesListView() {
        if (recipesListView != null) {
            recipesListView.getItems().clear(); // Clear the list before updating
            LinkyList<Drinks> allDrinks = drinksManager.getAllDrinks();
            for (int i = 0; i < allDrinks.size(); i++) {
                recipesListView.getItems().add(allDrinks.get(i).getDrinkName()); // Add drink names
            }
        }
    }

    @FXML
    private void onHandleReset(ActionEvent event) {
        // Confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Reset Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to reset?");
        confirmationAlert.setContentText("This will delete all data in memory and clear all saved data files. This action cannot be undone.");

        // Wait for user response
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Reset all managers
            drinksManager.reset(); // Clear all drinks
            ingredientManager.reset(); // Clear all ingredients
            recipeManager.reset(); // Clear all recipes

            // Clear data files
            clearDataFile(DRINKS_DATA_FILE);
            clearDataFile(INGREDIENTS_DATA_FILE);
            clearDataFile(RECIPES_DATA_FILE);

            // Clear UI components
            updateDrinksListView(); // Refresh drinks list
            updateIngredientsListView(); // Refresh ingredients list
            updateRecipesListView(); // Refresh recipes list
            clearFields(); // Clear input fields

            // Show success message
            showAlert("Success", "All in-memory data and data files have been cleared successfully.", Alert.AlertType.INFORMATION);
        }
    }

    // Helper method to clear a data file
    private void clearDataFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(""); // Clear the file content
            } catch (IOException e) {
                showAlert("Error", "Failed to clear the data file: " + filePath, Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    public void updateRecipesListView(LinkyList<Recipe> recipes) {
        if (recipesListView != null) {
            recipesListView.getItems().clear();
            for (int i = 0; i < recipes.size(); i++) {
                recipesListView.getItems().add(recipes.get(i).getDrinkName());
            }
        }
    }

    @FXML
    private void onAddIngredientToRecipe() {
        String selectedDrinkName = recipesListView.getSelectionModel().getSelectedItem();
        String ingredientName = chosenIngredient.getText().trim();

        if (selectedDrinkName == null || ingredientName.isEmpty()) {
            showAlert("Error", "Please select a drink and enter an ingredient name.", Alert.AlertType.ERROR);
            return;
        }

        Ingredients ingredient = ingredientManager.searchByName(ingredientName);
        if (ingredient == null) {
            showAlert("Error", "Ingredient not found in the ingredients list.", Alert.AlertType.ERROR);
            return;
        }

        Recipe recipe = recipeManager.searchByDrinkName(selectedDrinkName);
        if (recipe == null) {
            recipe = new Recipe(selectedDrinkName);
            recipeManager.addRecipe(recipe);
        }

        recipe.addIngredient(ingredient, 1); // Default quantity: 1
        updateRecipesListView();
        showAlert("Success", "Ingredient added to the recipe.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onUpdateIngredientInRecipe() {
        String selectedDrinkName = recipesListView.getSelectionModel().getSelectedItem();
        String ingredientName = chosenIngredient.getText().trim();

        if (selectedDrinkName == null || ingredientName.isEmpty()) {
            showAlert("Error", "Please select a drink and enter an ingredient name.", Alert.AlertType.ERROR);
            return;
        }

        Recipe recipe = recipeManager.searchByDrinkName(selectedDrinkName);
        if (recipe == null) {
            showAlert("Error", "Recipe not found.", Alert.AlertType.ERROR);
            return;
        }

        Ingredients ingredient = ingredientManager.searchByName(ingredientName);
        if (ingredient == null) {
            showAlert("Error", "Ingredient not found in the ingredients list.", Alert.AlertType.ERROR);
            return;
        }

        // Update the ingredient in the recipe
        recipe.updateIngredient(ingredientName, ingredient, 1); // Pass ingredientName as the first argument
        updateRecipesListView();
        showAlert("Success", "Ingredient updated in the recipe.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onDeleteIngredientFromRecipe() {
        String selectedDrinkName = recipesListView.getSelectionModel().getSelectedItem();
        String ingredientName = chosenIngredient.getText().trim();

        if (selectedDrinkName == null || ingredientName.isEmpty()) {
            showAlert("Error", "Please select a drink and enter an ingredient name.", Alert.AlertType.ERROR);
            return;
        }

        Recipe recipe = recipeManager.searchByDrinkName(selectedDrinkName);
        if (recipe == null) {
            showAlert("Error", "Recipe not found.", Alert.AlertType.ERROR);
            return;
        }

        // Delete the ingredient from the recipe using the ingredient name
        boolean removed = recipe.removeIngredient(ingredientName);
        if (removed) {
            updateRecipesListView();
            showAlert("Success", "Ingredient deleted from the recipe.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Ingredient not found in the recipe.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void onSortDrinks(ActionEvent event) {
        // Sort the drinks list alphabetically by drink name
        drinksManager.getAllDrinks().selectionSort(new Comparator<Drinks>() {
            @Override
            public int compare(Drinks drink1, Drinks drink2) {
                return drink1.getDrinkName().compareToIgnoreCase(drink2.getDrinkName());
            }
        });

        // Update the ListView to reflect the sorted order
        updateDrinksListView();
    }
    @FXML
    private void onSortIngredients(ActionEvent event) {
        // Sort the ingredients list alphabetically by ingredient name
        ingredientManager.getAllIngredients().selectionSort(new Comparator<Ingredients>() {
            @Override
            public int compare(Ingredients ingredient1, Ingredients ingredient2) {
                return ingredient1.getName().compareToIgnoreCase(ingredient2.getName());
            }
        });

        // Update the ListView to reflect the sorted order
        updateIngredientsListView();
    }
    @FXML
    private void onSortRecipes(ActionEvent event) {
        // Sort the recipes list alphabetically by drink name
        recipeManager.getAllRecipes().selectionSort(new Comparator<Recipe>() {
            @Override
            public int compare(Recipe recipe1, Recipe recipe2) {
                return recipe1.getDrinkName().compareToIgnoreCase(recipe2.getDrinkName());
            }
        });

        // Update the ListView to reflect the sorted order
        updateRecipesListView();
    }
}