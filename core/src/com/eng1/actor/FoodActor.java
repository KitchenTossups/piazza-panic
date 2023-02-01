package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;
import com.eng1.enums.Product;
import com.eng1.non_actor.Food;
import com.eng1.non_actor.Ingredient;

import java.util.*;

public class FoodActor extends BaseActor {

    private final Food food;
    private final List<IngredientActor> ingredientActors = new ArrayList<>();
    private int step = 0;
    private final boolean verbose;
    private final BaseActor plate;

    public FoodActor(float x, float y, Stage s, Food f, boolean verbose) {
        super(x, y, s);
        this.food = f;
        this.verbose = verbose;

        int rows = 32;
        int cols = 32;
        Texture texture = new Texture(Gdx.files.internal("spritemap/meals.png"), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

        this.plate = new BaseActor(x, y, s);

        if (Objects.requireNonNull(this.food.getRecipe().getEndProduct()) == Product.SALAD) {
            this.plate.setTexture(textureRegions[0][2], 100, 100);
        } else {
            this.plate.setTexture(textureRegions[0][5], 100, 100);
        }

        float spacing = 15;

//        if (this.food.getRecipe().getEndProduct() == Product.SALAD)
//            spacing = 25;

        for (Ingredient ingredient : this.food.getRecipe().getIngredientsRaw()) {
            if (this.verbose)
                System.out.println("Creating FoodActor " + this.food + " with Ingredient " + ingredient.toString());
            IngredientActor ingredientActor = new IngredientActor(x + 10, y + spacing, s, ingredient);
            ingredientActor.setVisible(false);
            ingredientActors.add(ingredientActor);
//            if (this.food.getRecipe().getEndProduct() == Product.SALAD)
//                spacing += 5;
//            else
                spacing += 15;
        }

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1, 0, 0, 0));
        pixmap.fillRectangle(0, 0, 100, 100);
        this.setTexture(new Texture(pixmap));
        pixmap.dispose();
        this.setSize(100, 100);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void moveAll(float x, float y) {
        this.setPosition(x, y);
        this.plate.setPosition(x, y);
        int space = 15;
        for (IngredientActor ingredientActor : this.ingredientActors) {
            ingredientActor.setPosition(x + 10, y + space);
            space += 15;
        }
    }

    public Food getFood() {
        return this.food;
    }

    public void changeStage(Stage stage) {
        this.plate.remove();
        stage.addActor(this.plate);
        for (IngredientActor ingredientActor : ingredientActors) {
            ingredientActor.remove();
            stage.addActor(ingredientActor);
        }
        this.remove();
        stage.addActor(this);
    }

    public boolean addNextItem(Ingredient ingredient) {
        if (this.verbose) System.out.println(this.ingredientActors.toString());
        if (this.verbose) System.out.println("Add next item " + ingredient);
        if (ingredient == null) {
            if (this.verbose) System.out.println("Add next item null");
            return false;
        }
        if (ingredientActors.get(step).getIngredient().notMatches(ingredient)) {
            if (this.verbose)
                System.out.println("Add next item not expected, given " + ingredient + " - " + ingredient.getState() + ", expected " + ingredientActors.get(step).getIngredient() + " - " + ingredientActors.get(step).getIngredient().getState());
            return false;
        }
        if (step == ingredientActors.size()) {
            if (this.verbose) System.out.println("Add next item max");
            return false;
        }
        step++;
        this.food.addIngredient(ingredient);
        return true;
    }

    public int getStep() {
        if (step == ingredientActors.size()) return -1;
        return step;
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if (step != 0 && !ingredientActors.get(step - 1).isVisible()) {
            if (this.verbose) System.out.println("FoodActor act");
            for (int i = 0; i < step; i++)
                ingredientActors.get(i).setVisible(true);
        }
    }

    @Override
    public String toString() {
        return "FoodActor{" +
                "food=" + food +
                ", ingredientActors=" + ingredientActors.toString() +
                ", step=" + step +
                '}';
    }
}
