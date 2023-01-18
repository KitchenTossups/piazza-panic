package com.eng1.non_actor;

import com.eng1.enums.*;

import java.util.*;

@SuppressWarnings("unused")
public class Recipe {
    private final Product endProduct;
    private final List<Ingredient> ingredients;

    public Recipe(Product endProduct) {
        this.endProduct = endProduct;
        this.ingredients = this.generateIngredientList();
    }

    public Product getEndProduct() {
        return this.endProduct;
    }

    public List<Ingredient> getIngredientsRaw() {
        return this.ingredients;
    }

    public boolean satisfied(Food food) {
        return new HashSet<>(food.getCurrentIngredients()).containsAll(this.ingredients);
    }

    public String getIngredients() {
        StringBuilder stringBuilder = new StringBuilder("Ingredients:\n");
        for (Ingredient ingredient : this.ingredients)
            stringBuilder.append(ingredient.getItem().toString()).append("\n");
        return stringBuilder.toString();
    }

    private List<Ingredient> generateIngredientList() {
        List<Ingredient> ingredients = new ArrayList<>();
        switch (this.endProduct) {
            case BURGER:
                ingredients.add(new Ingredient(Item.BOTTOMBUN, IngredientState.PREPARED));
                ingredients.add(new Ingredient(Item.PATTY, IngredientState.COOKED));
                ingredients.add(new Ingredient(Item.CHEESE, IngredientState.UNPREPARED));
                ingredients.add(new Ingredient(Item.TOPBUN, IngredientState.PREPARED));
                break;
            case SALAD:
                ingredients.add(new Ingredient(Item.LETTUCE, IngredientState.PREPARED));
                ingredients.add(new Ingredient(Item.TOMATO, IngredientState.PREPARED));
                ingredients.add(new Ingredient(Item.ONION, IngredientState.PREPARED));
                break;
        }
        return ingredients;
    }
}
