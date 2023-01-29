package com.eng1.non_actor;

import java.util.ArrayList;
import java.util.List;

public class Food {

    private final Recipe recipe;
    private final long customerOrderTime;
    private final List<Ingredient> currentIngredients;

    public Food(Recipe recipe, long customerOrderTime) {
        this.recipe = recipe;
        this.customerOrderTime = customerOrderTime;
        this.currentIngredients = new ArrayList<>();
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void addIngredient(Ingredient ingredient) {
        this.currentIngredients.add(ingredient);
    }

    public long getCustomerOrderTime() {
        return customerOrderTime;
    }

    public List<Ingredient> getCurrentIngredients() {
        return this.currentIngredients;
    }

    public boolean ready() {
        return this.recipe.satisfied(this);
    }
}
