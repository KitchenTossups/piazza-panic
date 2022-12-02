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
            case TEST_0:
                this.setTexture(this.textureRegions[0][2]);
                this.index = "0:2";
                break;
            case TEST_1:
                this.setTexture(this.textureRegions[0][5]);
                this.index = "0:5";
                break;
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
