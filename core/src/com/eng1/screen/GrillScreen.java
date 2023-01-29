package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.eng1.PiazzaPanic;
import com.eng1.actor.IngredientActor;
import com.eng1.actor.Inventory;
import com.eng1.actor.Steam;
import com.eng1.actor.TableSpace;
import com.eng1.base.*;
import com.eng1.enums.IngredientState;
import com.eng1.enums.TableSpaceType;

public class GrillScreen extends BaseScreen {

    private GameScreen gameScreen;
    private final PiazzaPanic game;
    private final IngredientActor[] items;
    final boolean[] accepted = {false, false, false, false}, unlock = {true, true, true}, flipped = {false, false, false}, needFlip = {false, false, false};
    final long[] timePlaced = new long[3], flipTime = new long[3];

    public GrillScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage),
                stainless = new BaseActor(0, 100, this.mainStage),
                grill = new BaseActor(0, 100, this.mainStage),
                hood = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("background/background.png");
        stainless.loadTexture("background/stainless.png");
        stainless.setSize(gameScreen.width, gameScreen.height - 100);
        grill.loadTexture("images/grill.jpg");
        grill.setSize(gameScreen.width, gameScreen.width / grill.getWidth() * grill.getHeight());
        hood.loadTexture("images/hood.png");
        hood.setSize(gameScreen.width, gameScreen.width / hood.getWidth() * hood.getHeight());
        hood.setY(gameScreen.height - hood.getHeight());
        this.gameScreen = gameScreen;
        this.game = game;
        this.uiTable.pad(10);
        this.uiTable.add(new Label("Press \"Q\" to exit this screen", this.game.labelStyle[1])).expand().align(Align.topRight);
        this.uiTable.row();
        this.items = new IngredientActor[3];
        TableSpace[] tableSpaces = new TableSpace[3];
        tableSpaces[0] = new TableSpace(300, 200, TableSpaceType.OUTLINE, mainStage);
        tableSpaces[1] = new TableSpace(590, 200, TableSpaceType.OUTLINE, mainStage);
        tableSpaces[2] = new TableSpace(880, 200, TableSpaceType.OUTLINE, mainStage);
        Steam[] steams = new Steam[3];
        steams[0] = new Steam(286, 280, mainStage);
        steams[1] = new Steam(576, 280, mainStage);
        steams[2] = new Steam(866, 280, mainStage);
        steams[0].setVisible(false);
        steams[1].setVisible(false);
        steams[2].setVisible(false);
        Inventory inventory = new Inventory(uiStage);
        DragAndDrop dragAndDrop = new DragAndDrop();
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
                if (((IngredientActor) payload.getDragActor()).getIngredient().getState() == IngredientState.COOKED || ((IngredientActor) payload.getDragActor()).getIngredient().getState() == IngredientState.OVERCOOKED) {
                    source.getActor().setPosition(this.x + 10, this.y + 10);
                    accepted[0] = true;
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(payload.getDragActor());
                } else accepted[0] = false;
            }
        });
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            dragAndDrop.addTarget(new DragAndDrop.Target(tableSpaces[finalI]) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    return false;
                }

                @Override
                public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                    getActor().setColor(Color.WHITE);
                    accepted[finalI + 1] = false;
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                }
            });
        }
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play();
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    @Override
    public void updateGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
