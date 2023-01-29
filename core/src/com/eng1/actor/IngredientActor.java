package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.eng1.enums.IngredientState;
import com.eng1.non_actor.Ingredient;
import com.eng1.base.BaseActor;

import java.util.Arrays;

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

        TextureRegion[][] textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

        String image = "";

        String[] images = {};

        boolean bypass = false;

        Array<TextureRegion> textureArray = new Array<>();

        Texture texture1;

        switch (ingredient.getItem()) {
            case TOP_BUN:
                image = "images/TopBun.png";
                break;
            case BOTTOM_BUN:
                image = "images/BottomBun.png";
                break;
            case PATTY:
                bypass = true;
                images = new String[]{"images/Patty.png", "images/PattyHalfCooked.png", "images/PattyCooked.png"};
                loadTexture(images, 80, 80);
                break;
            case CHEESE:
                image = "images/Cheese.png";
                break;
            case LETTUCE:
                bypass = true;
                texture1 = new Texture(Gdx.files.internal("images/Lettuce.png"));
                texture1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                textureArray.add(new TextureRegion(texture1));
                textureArray.add(textureRegions[1][1]);
                loadAnimationFromTextureRegion(textureArray, 1, 80, 80);
                break;
            case TOMATO:
                bypass = true;
                images = new String[]{"images/Tomato2.png", "images/Tomato.png"};
                loadTexture(images, 80, 80);
//                texture1 = new Texture(Gdx.files.internal("images/Tomato.png"));
//                texture1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//                textureArray.add(new TextureRegion(texture1));
//                textureArray.add(textureRegions[7][12]);
//                loadAnimationFromTextureRegion(textureArray, 1, 80, 80);
                break;
            case ONION:
                bypass = true;
                images = new String[]{"images/Onion2.png", "images/Onion.png"};
                loadTexture(images, 80, 80);
//                texture1 = new Texture(Gdx.files.internal("images/Onion.png"));
//                texture1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//                textureArray.add(new TextureRegion(texture1));
//                textureArray.add(textureRegions[7][10]);
//                loadAnimationFromTextureRegion(textureArray, 1, 80, 80);
                break;
            default:
                break;
        }
        if (!bypass)
            this.loadTexture(image, 80, 80);
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public void makeReady() {
        System.out.print("Make ready " + ingredient.toString() + " from " + ingredient.getState() + " to ");
        switch (this.ingredient.getState()) {
            case UNCUT:
                System.out.println(IngredientState.CUT);
                this.ingredient.setState(IngredientState.CUT);
                new Thread(new MakeReady()).start();
                break;
            case UNCOOKED:
                System.out.println(IngredientState.HALF_COOKED);
                this.ingredient.setState(IngredientState.HALF_COOKED);
                new Thread(new MakeReady()).start();
                break;
            case HALF_COOKED:
                System.out.println(IngredientState.COOKED);
                this.ingredient.setState(IngredientState.COOKED);
                new Thread(new MakeReady()).start();
                break;
            case UNPREPARED:
                System.out.println(IngredientState.PREPARED);
                this.ingredient.setState(IngredientState.PREPARED);
                break;
            default:
                System.out.println("Error making ready " + this.ingredient.getState());
                break;
        }
    }

    final boolean[] pause = {true};

    class MakeReady implements Runnable {

        @Override
        public void run() {
            try {
                pause[0] = false;
                Thread.sleep(1100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pause[0] = true;
        }
    }

    @Override
    public String toString() {
        return this.ingredient.getItem().toString();
    }

    public void act(float deltaTime) {
        super.act(deltaTime);
        setAnimationPaused(pause[0]);
    }
}
