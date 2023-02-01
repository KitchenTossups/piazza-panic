package com.eng1.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.non_actor.*;

import java.util.*;

public class FoodChestScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;

    public FoodChestScreen(FoodChestType foodChestType, GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("background/background.png");
        background.setSize(gameScreen.width, gameScreen.height);
        this.gameScreen = gameScreen;
        this.game = game;
        String image = "";
        Inventory inventory = new Inventory(590, 0, 100, 100, 1, this.uiStage);
        List<IngredientActor> ingredientActorList = new ArrayList<>();
        switch (foodChestType) {
            case DRY_FOOD:
                image = "squared/yellow.png";
                ingredientActorList.add(new IngredientActor(600, 200, uiStage, new Ingredient(Item.BOTTOM_BUN, IngredientState.NOT_APPLICABLE)));
                ingredientActorList.add(new IngredientActor(600, 500, uiStage, new Ingredient(Item.TOP_BUN, IngredientState.NOT_APPLICABLE)));
                break;
            case FRESH_FOOD:
                image = "squared/grey.png";
                ingredientActorList.add(new IngredientActor(386, 200, uiStage, new Ingredient(Item.CHEESE, IngredientState.NOT_APPLICABLE)));
                ingredientActorList.add(new IngredientActor(813, 200, uiStage, new Ingredient(Item.LETTUCE, IngredientState.UNCUT)));
                ingredientActorList.add(new IngredientActor(386, 500, uiStage, new Ingredient(Item.TOMATO, IngredientState.UNCUT)));
                ingredientActorList.add(new IngredientActor(813, 500, uiStage, new Ingredient(Item.ONION, IngredientState.UNCUT)));
                break;
            case FROZEN_FOOD:
                image = "squared/blue.png";
                ingredientActorList.add(new IngredientActor(600, 200, uiStage, new Ingredient(Item.PATTY, IngredientState.UNCOOKED)));
                break;
        }
        new Shelf(0, 100, 1280, 100, image, this.mainStage);
        new Shelf(0, 400, 1280, 100, image, this.mainStage);
        this.uiTable.pad(10);
        this.uiTable.add(new Label("Press \"Q\" to exit this screen", this.game.labelStyle[1])).expand().align(Align.topRight);
        this.uiTable.row();
        DragAndDrop dragAndDrop = new DragAndDrop();
        final boolean[] accepted = {false};
        for (IngredientActor ingredientActor : ingredientActorList) {
            dragAndDrop.addSource(new DragAndDrop.Source(ingredientActor) {
                final float x = ingredientActor.getX(), y = ingredientActor.getY();

                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(ingredientActor);
                    accepted[0] = false;
                    return payload;
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                    if (!accepted[0]) {
                        payload.getDragActor().setPosition(this.x, this.y);
                        gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                    }
                }
            });
        }
        dragAndDrop.addTarget(new DragAndDrop.Target(inventory) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                getActor().setColor(Color.GREEN);
                return true;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                getActor().setColor(Color.WHITE);
                accepted[0] = false;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if (gameScreen.chefs[gameScreen.getChefSelector()].getInventoryItem() == null) {
                    float x1 = inventory.getX();
                    float y1 = inventory.getY();
                    source.getActor().setPosition(x1 + 10, y1 + 10);
                    accepted[0] = true;
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((IngredientActor) source.getActor()).getIngredient());
                } else {
                    accepted[0] = false;
                }
            }
        });
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play(gameScreen.getMasterVolume());
            dispose();
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    static class Shelf extends BaseActor {
        public Shelf(float x, float y, float width, float height, String image, Stage s) {
            super(x, y, s);

            this.loadTexture(image, width, height);
        }
    }
}
