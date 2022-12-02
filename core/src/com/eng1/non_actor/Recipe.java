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
            case TEST_0:
                ingredients.add(new Ingredient(Item.TEST_0, IngredientState.UNPREPARED));
                ingredients.add(new Ingredient(Item.TEST_1, IngredientState.UNCOOKED));
                ingredients.add(new Ingredient(Item.TEST_2, IngredientState.UNCUT));
                ingredients.add(new Ingredient(Item.TEST_3, IngredientState.UNPREPARED));
                break;
            case TEST_1:
                ingredients.add(new Ingredient(Item.TEST_0, IngredientState.UNPREPARED));
                ingredients.add(new Ingredient(Item.TEST_3, IngredientState.UNPREPARED));
                ingredients.add(new Ingredient(Item.TEST_4, IngredientState.UNCOOKED));
                ingredients.add(new Ingredient(Item.TEST_5, IngredientState.UNCUT));
                break;
            case TEST_2:
                ingredients.add(new Ingredient(Item.TEST_3, IngredientState.UNPREPARED));
                ingredients.add(new Ingredient(Item.TEST_4, IngredientState.UNCOOKED));
                break;
            case TEST_3:
                ingredients.add(new Ingredient(Item.TEST_0, IngredientState.UNPREPARED));
                ingredients.add(new Ingredient(Item.TEST_5, IngredientState.UNCUT));
                break;
        }
        return ingredients;
    }
}
