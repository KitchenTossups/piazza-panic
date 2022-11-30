package com.eng1.non_actor;

import com.eng1.enums.IngredientState;

public class Ingredient {
    private final String itemName;

    private IngredientState state;

    public Ingredient(String itemName, IngredientState state) {
        this.itemName = itemName;
        this.state = state;
    }

    public String getItemName() {
        return this.itemName;
    }

    public IngredientState getState() {
        return this.state;
    }

    public void setState(IngredientState state) {
        this.state = state;
    }
}
