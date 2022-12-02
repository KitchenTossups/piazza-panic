package com.eng1.non_actor;

import com.eng1.enums.*;

@SuppressWarnings("unused")
public class Ingredient {

    private final Item item;
    private IngredientState state;

    public Ingredient(Item item, IngredientState state) {
        this.item = item;
        this.state = state;
    }

    public Item getItem() {
        return this.item;
    }

    public IngredientState getState() {
        return this.state;
    }

    public void setState(IngredientState state) {
        this.state = state;
    }
}
