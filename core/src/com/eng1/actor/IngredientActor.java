package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.eng1.enums.IngredientState;
import com.eng1.enums.Item;
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

        TextureRegion[][] textureRegions = TextureRegion.split(texture, frameWidth, frameHeight);

        String image = "";

        String[] images = null;

        boolean bypass = false;

        Array<TextureRegion> textureArray = new Array<>();

        Texture texture1;

        switch (ingredient.getItem()) {
            case TOP_BUN:
                if (ingredient.getState() != IngredientState.NOT_APPLICABLE)
                    remove();
                else
                    image = "images/TopBun.png";
                break;
            case BOTTOM_BUN:
                if (ingredient.getState() != IngredientState.NOT_APPLICABLE)
                    remove();
                else
                    image = "images/BottomBun.png";
                break;
            case PATTY:
                bypass = true;
                switch (ingredient.getState()) {
                    case UNCOOKED:
                        images = new String[]{"images/Patty.png", "images/PattyHalfCooked.png", "images/PattyCooked.png", "images/PattyOvercooked.png"};
                        break;
                    case HALF_COOKED:
                        images = new String[]{"images/PattyHalfCooked.png", "images/PattyCooked.png", "images/PattyOvercooked.png"};
                        break;
                    case COOKED:
                        images = new String[]{"images/PattyCooked.png", "images/PattyOvercooked.png"};
                        break;
                    case OVERCOOKED:
                        images = new String[]{"images/PattyOvercooked.png"};
                        break;
                    default:
                        remove();
                        break;
                }
                if (images != null)
                    loadTexture(images, 80, 80);
                break;
            case CHEESE:
                if (ingredient.getState() != IngredientState.NOT_APPLICABLE)
                    remove();
                else
                    image = "images/Cheese.png";
                break;
            case LETTUCE:
                bypass = true;
                switch (ingredient.getState()) {
                    case UNCUT:
                        texture1 = new Texture(Gdx.files.internal("images/Lettuce.png"));
                        texture1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                        textureArray.add(new TextureRegion(texture1));
                    case CUT:
                        textureArray.add(textureRegions[1][1]);
                        loadAnimationFromTextureRegion(textureArray, 0.1f, 80, 80);
                        break;
                    default:
                        remove();
                }
                break;
            case TOMATO:
                bypass = true;
                switch (ingredient.getState()) {
                    case UNCUT:
                        images = new String[]{"images/Tomato2.png", "images/Tomato.png"};
                        break;
                    case CUT:
                        images = new String[]{"images/Tomato.png"};
                        break;
                    default:
                        remove();
                }
                if (images != null)
                    loadTexture(images, 80, 80);
                break;
            case ONION:
                bypass = true;
                switch (ingredient.getState()) {
                    case UNCUT:
                        images = new String[]{"images/Onion2.png", "images/Onion.png"};
                        break;
                    case CUT:
                        images = new String[]{"images/Onion.png"};
                        break;
                    default:
                        remove();
                }
                if (images != null)
                    loadTexture(images, 80, 80);
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

    public void makeOverCooked() {
        if (this.ingredient.getItem() == Item.PATTY)
            this.ingredient.setState(IngredientState.OVERCOOKED);
        else
            System.out.println("Error making " + this.ingredient.getItem().toString() + " to Overcooked");
    }

    final boolean[] pause = {true};

    class MakeReady implements Runnable {

        @Override
        public void run() {
            try {
                pause[0] = false;
                Thread.sleep(110);
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
