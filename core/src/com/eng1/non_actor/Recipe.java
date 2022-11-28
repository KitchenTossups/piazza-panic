package com.eng1.non_actor;

import java.util.*;

@SuppressWarnings("unused")
public class Recipe {
    private final String endProductName;
    private final List<Ingredient> ingredients;

    public Recipe(String endProductName, List<Ingredient> ingredients) {
        this.endProductName = endProductName;
        this.ingredients = ingredients;
    }

    public String getEndProductName() {
        return this.endProductName;
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
            stringBuilder.append(ingredient.getItemName()).append("\n");
        return stringBuilder.toString();
    }
}
