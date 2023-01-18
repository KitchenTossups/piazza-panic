package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.non_actor.Ingredient;
import com.eng1.base.BaseActor;

import java.util.Arrays;

@SuppressWarnings("unused")
public class IngredientActor extends BaseActor {

    private final Ingredient ingredient;
    private final TextureRegion[][] textureRegions;
    private String index;

    public IngredientActor(float x, float y, Stage s, Ingredient ingredient) {
        super(x, y, s);

        this.ingredient = ingredient;

        int rows = 32;
        int cols = 32;
        Texture texture = new Texture(Gdx.files.internal("spritemap/meals.png"), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        this.textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

        switch (ingredient.getItem()) {
            case TOPBUN:
                this.loadTexture("images/TopBun.png");
            case BOTTOMBUN:
                this.loadTexture("images/BottomBun.png");
            case PATTY:
                this.loadTexture("images/Patty.png");
            case CHEESE:
                this.loadTexture("images/Cheese.png");
            case LETTUCE:
                this.loadTexture("images/Lettuce.png");
            case TOMATO:
                this.loadTexture("images/Tomato.png");
            case ONION:
                this.loadTexture("images/Onion.png");
            default:
                break;
        }
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public void makeReady() {
        System.out.println(Arrays.deepToString(this.textureRegions));
        System.out.println(this.index);
    }

    @Override
    public String toString() {
        return this.ingredient.getItem().toString();
    }
}
