package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.eng1.PiazzaPanic;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.IngredientState;
import com.eng1.enums.TableSpaceType;
import com.eng1.non_actor.Ingredient;

public class ChoppingScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;
    private IngredientActor ingredientActor;
    private final BaseActor knife;
    protected final boolean[] accepted = {false, true};

    public ChoppingScreen(GameScreen gameScreen, PiazzaPanic game) {
        this.gameScreen = gameScreen;
        this.game = game;
        BaseActor background = new BaseActor(0, 0, this.mainStage), choppingBoard = new BaseActor(0, 100, this.mainStage);
        background.loadTexture("background/background.png");
        background.setSize(gameScreen.width, gameScreen.height);
        choppingBoard.loadTexture("images/chopping_board.png");
        choppingBoard.setX((gameScreen.width / 2f) - (choppingBoard.getWidth() / 2f));
        this.uiTable.pad(10);
        this.uiTable.add(new Label("Press \"Q\" to exit this screen when the chopping has completed", this.game.labelStyle[1])).expand().align(Align.topRight);
        this.uiTable.row();
        Inventory inventory = new Inventory(uiStage);
        TableSpace tableSpace = new TableSpace(590, 300, TableSpaceType.OUTLINE, uiStage);
        this.ingredientActor = null;
        Ingredient ingredient = (Ingredient) this.gameScreen.chefs[this.gameScreen.getChefSelector()].getInventoryItem();
        this.ingredientActor = new IngredientActor(inventory.getX() + 10, inventory.getY() + 10, this.uiStage, ingredient);
        this.knife = new BaseActor(0, -100, this.uiStage);
        this.knife.loadTexture("images/knife.png");
        this.knife.setX((gameScreen.width / 2f) - (this.knife.getWidth() / 3f));
        this.game.setActiveScreen(gameScreen);
        DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.addSource(new DragAndDrop.Source(ingredientActor) {
            final float x = ingredientActor.getX(), y = ingredientActor.getY();

            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(ingredientActor);
                accepted[0] = false;
                if (accepted[1])
                    return payload;
                else return null;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (!accepted[0]) {
                    payload.getDragActor().setPosition(this.x, this.y);
                }
            }
        });
        dragAndDrop.addTarget(new DragAndDrop.Target(tableSpace) {
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
                if (ingredientActor.getIngredient().getState() == IngredientState.UNCUT) {
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                    float x1 = tableSpace.getX();
                    float y1 = tableSpace.getY();
                    source.getActor().setPosition(x1 + 10, y1 + 10);
                    accepted[0] = true;
                    accepted[1] = false;
                    new Thread(new StopKnife()).start();
                } else accepted[0] = false;
            }
        });
        dragAndDrop.addTarget(new DragAndDrop.Target(inventory) {
            final float x = inventory.getX(), y = inventory.getY();

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
                source.getActor().setPosition(this.x + 10, this.y + 10);
                accepted[0] = true;
                gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((IngredientActor) source.getActor()).getIngredient());
            }
        });
    }

    class StopKnife implements Runnable {

        @Override
        public void run() {
            knife.setY(200);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            knife.clearActions();
            knife.setY(-100f);
            accepted[1] = true;
//            new IngredientActor()
            ingredientActor.makeReady();
        }
    }

    @Override
    public void update(float dt) {
        if (this.ingredientActor != null) {
            if (this.knife.getY() == 200) {
                MoveToAction action = new MoveToAction();
                action.setX(this.knife.getX());
                action.setY(500);
                action.setDuration(0.5f);
                this.knife.addAction(action);
            }
            if (this.knife.getY() == 500) {
                MoveToAction action = new MoveToAction();
                action.setX(this.knife.getX());
                action.setY(200);
                action.setDuration(0.5f);
                this.knife.addAction(action);
            }
        } else if (this.knife.getY() != -100f)
            this.knife.setY(-100f);

        if (Gdx.input.isKeyPressed(Input.Keys.Q) && ingredientActor.getY() == 10) {
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play();
            dispose();
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    public IngredientActor getIngredientActor() {
        return this.ingredientActor;
    }
}
