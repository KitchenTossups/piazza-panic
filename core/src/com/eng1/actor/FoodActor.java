package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;
import com.eng1.enums.Product;
import com.eng1.non_actor.Food;

import java.util.Objects;

public class FoodActor extends BaseActor {

    private final Food food;
    private final TextureRegion[][] textureRegions;
    private Group group;

    public FoodActor(float x, float y, Stage s, Food f) {
        super(x, y, s);

        this.food = f;

        int rows = 32;
        int cols = 32;
        Texture texture = new Texture(Gdx.files.internal("spritemap/meals.png"), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        this.textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

        if (Objects.requireNonNull(this.food.getRecipe().getEndProduct()) == Product.SALAD) {
            this.setTexture(this.textureRegions[0][2]);
        } else {
            this.setTexture(this.textureRegions[0][5]);
        }
    }

    public Food getFood() {
        return this.food;
    }
}
