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
        if (food.getCurrentIngredients().size() != this.ingredients.size())
            return false;
        for (int i = 0; i < this.ingredients.size(); i++)
            if (!this.ingredients.get(i).matches(food.getCurrentIngredients().get(i)))
                return false;
        return true;
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
            case CHEESEBURGER:
                ingredients.add(new Ingredient(Item.BOTTOM_BUN, IngredientState.NOT_APPLICABLE));
                ingredients.add(new Ingredient(Item.PATTY, IngredientState.COOKED));
                ingredients.add(new Ingredient(Item.CHEESE, IngredientState.NOT_APPLICABLE));
                ingredients.add(new Ingredient(Item.TOP_BUN, IngredientState.NOT_APPLICABLE));
                break;
//            case DOUBLE_CHEESEBURGER:
//                ingredients.add(new Ingredient(Item.BOTTOM_BUN, IngredientState.NOT_APPLICABLE));
//                ingredients.add(new Ingredient(Item.PATTY, IngredientState.COOKED));
//                ingredients.add(new Ingredient(Item.CHEESE, IngredientState.NOT_APPLICABLE));
//                ingredients.add(new Ingredient(Item.PATTY, IngredientState.COOKED));
//                ingredients.add(new Ingredient(Item.CHEESE, IngredientState.NOT_APPLICABLE));
//                ingredients.add(new Ingredient(Item.TOP_BUN, IngredientState.NOT_APPLICABLE));
//                break;
            case BURGER:
                ingredients.add(new Ingredient(Item.BOTTOM_BUN, IngredientState.NOT_APPLICABLE));
                ingredients.add(new Ingredient(Item.PATTY, IngredientState.COOKED));
                ingredients.add(new Ingredient(Item.TOP_BUN, IngredientState.NOT_APPLICABLE));
                break;
            case SALAD:
                ingredients.add(new Ingredient(Item.LETTUCE, IngredientState.CUT));
                ingredients.add(new Ingredient(Item.TOMATO, IngredientState.CUT));
                ingredients.add(new Ingredient(Item.ONION, IngredientState.CUT));
                break;
        }
        return ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "endProduct=" + endProduct.toString() +
                ", ingredients=" + ingredients.toString() +
                '}';
    }
}
