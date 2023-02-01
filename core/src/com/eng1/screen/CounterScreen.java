package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.non_actor.*;

import java.util.Objects;

public class CounterScreen extends BaseScreen {

    private GameScreen gameScreen;
    private final PiazzaPanic game;
    private final BaseActor[] items;
    private final DragAndDrop dragAndDrop;
    BaseActor inventoryItem = null;
    private final Inventory inventory;
    final TableSpace[] tableSpaces = new TableSpace[3];
    private final boolean[] accepted = {false, false, false, false};

    public CounterScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("background/Floor1.png");
        background.setSize(gameScreen.width, gameScreen.height);
        new Table(this.mainStage);
        this.inventory = new Inventory(600, 0, 105, 110, 0, uiStage);
        tableSpaces[0] = new TableSpace(300, 170, TableSpaceType.BLANK, uiStage);
        tableSpaces[1] = new TableSpace(600, 170, TableSpaceType.BLANK, uiStage);
        tableSpaces[2] = new TableSpace(890, 170, TableSpaceType.BLANK, uiStage);
        this.gameScreen = gameScreen;
        this.game = game;
        this.items = new BaseActor[3];
        this.uiTable.pad(10);
        this.uiTable.add(new Label("Press \"Q\" to exit this screen", this.game.labelStyle[1])).expand().align(Align.topRight);
        this.uiTable.row();
        this.dragAndDrop = new DragAndDrop();
        this.dragAndDrop.addTarget(new DragAndDrop.Target(inventory) {
            final float x = inventory.getX(), y = inventory.getY();

            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if (gameScreen.chefs[gameScreen.getChefSelector()].getInventoryItem() == null) {
                    getActor().setColor(Color.GREEN);
                    return true;
                }
                return false;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                getActor().setColor(Color.WHITE);
                accepted[0] = false;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                source.getActor().setPosition(this.x + 10, this.y + 10);
                accepted[0] = true;
                BaseActor object = (BaseActor) payload.getDragActor();
                if (object instanceof IngredientActor)
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((IngredientActor) object).getIngredient());
                else if (object instanceof FoodActor)
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((FoodActor) object).getFood());
                for (int i = 0; i < 3; i++) {
                    BaseActor actor = items[i];
                    if (game.isVerbose()) System.out.println(actor);
                    if (game.isVerbose()) System.out.println("Counter searching");
                    if (actor == source.getActor()) {
                        if (game.isVerbose()) System.out.println("Counter found " + i);
                        items[i].remove();
                        items[i] = null;
                    }
                }
            }
        });
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            this.dragAndDrop.addTarget(new DragAndDrop.Target(tableSpaces[finalI]) {
                final float x = tableSpaces[finalI].getX(), y = tableSpaces[finalI].getY();

                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    if (items[finalI] == null) {
                        if (game.isVerbose()) System.out.println("Counter drag allow null " + finalI);
                        getActor().setColor(Color.GREEN);
                        return true;
                    }
                    if (game.isVerbose()) System.out.println("Counter drag fail end " + finalI);
                    getActor().setColor(Color.RED);
                    return false;
                }

                @Override
                public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                    getActor().setColor(Color.WHITE);
                    accepted[0] = false;
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    source.getActor().setPosition(this.x + 20, this.y + 10);
                    accepted[0] = true;
                    items[finalI] = inventoryItem;
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                    inventoryItem = null;
                    if (game.isVerbose()) System.out.println("Counter remove " + finalI);
                }
            });
        }
    }

    @Override
    public void update(float dt) {
        Object object = gameScreen.chefs[gameScreen.getChefSelector()].getInventoryItem();
        BaseActor actor = null;
        if (object != null && inventoryItem == null) {
            if (object instanceof Ingredient) {
                IngredientActor ingredientActor = new IngredientActor(inventory.getX() + 10, inventory.getY() + 10, uiStage, (Ingredient) object);
                inventoryItem = ingredientActor;
                actor = ingredientActor;
            } else if (object instanceof Food) {
                FoodActor foodActor = new FoodActor(inventory.getX() + 10, inventory.getY() + 10, uiStage, (Food) object, game.isVerbose());
                inventoryItem = foodActor;
                actor = foodActor;
            }
            BaseActor finalActor = actor;
            this.dragAndDrop.addSource(new DragAndDrop.Source(Objects.requireNonNull(finalActor)) {
                final float x = finalActor.getX(), y = finalActor.getY();

                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    boolean spareSpace = false;
                    for (BaseActor space : items) {
                        if (game.isVerbose()) System.out.println("Counter payload loop");
                        if (space == null || (space == finalActor && inventoryItem == null)) {
                            if (game.isVerbose()) System.out.println("Counter payload space");
                            spareSpace = true;
                            break;
                        }
                    }
                    if (!spareSpace)
                        return null;
                    if (game.isVerbose()) System.out.println("Counter payload after");
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(finalActor);
                    if (game.isVerbose()) System.out.println("Counter payload!");
                    return payload;
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                    if (!accepted[0]) {
                        payload.getDragActor().setPosition(this.x, this.y);
                    }
                }
            });
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            super.removeRemainingFromInventorySpace(game.isVerbose());
            try {
                assert actor != null;
                actor.remove();
            } catch (Exception ignored) {

            }
            try {
                inventoryItem.remove();
            } catch (Exception ignored) {

            }
            inventoryItem = null;
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play(gameScreen.getMasterVolume());
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    @Override
    public void updateGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
