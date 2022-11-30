package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.non_actor.Ingredient;
import com.eng1.base.BaseActor;

@SuppressWarnings("unused")
public class IngredientActor extends BaseActor {

    private final Ingredient ingredient;

    public IngredientActor(float x, float y, Stage s, Ingredient ingredient) {
        super(x, y, s);

        this.ingredient = ingredient;

        int rows = 32;
        int cols = 32;
        Texture texture = new Texture(Gdx.files.internal("spritemap/meals.png"), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        switch (ingredient.getItemName()) {
            case "0":
                this.setTexture(temp[0][2]);
                break;
            case "1":
                this.setTexture(temp[0][5]);
                break;
            default:
                break;
        }
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    @Override
    public String toString() {
        return this.ingredient.getItemName();
    }
}
