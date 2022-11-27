package com.eng1.non_actor;

import java.util.HashSet;
import java.util.List;

public record Recipe(String endProductName, List<Ingredient> ingredients) {

    public boolean satisfied(Food food) {
        return new HashSet<>(food.getCurrentIngredients()).containsAll(this.ingredients);
    }

    public String getIngredients() {
        StringBuilder stringBuilder = new StringBuilder("Ingredients:\n");
        for (Ingredient ingredient : this.ingredients)
            stringBuilder.append(ingredient.itemName()).append("\n");
        return stringBuilder.toString();
    }
}
