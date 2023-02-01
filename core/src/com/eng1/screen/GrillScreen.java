package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
import com.eng1.enums.Mode;
import com.eng1.enums.TableSpaceType;
import com.eng1.non_actor.Ingredient;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class GrillScreen extends BaseScreen {

    private GameScreen gameScreen;
    private final PiazzaPanic game;
    private final IngredientActor[] items;
    final boolean[] accepted = {false, false, false, false}, unlock = {true, true, true}, flipped = {false, false, false}, needFlip = {false, false, false}, finished = {false, false, false};
    final long[] timePlaced = new long[3], flipTime = new long[3];
    final DragAndDrop dragAndDrop;
    final Inventory inventory;
    IngredientActor inventoryItem = null;
    final Label[] labels = new Label[3];
    final TableSpace[] tableSpaces = new TableSpace[3];
    final Steam[] steams = new Steam[3];

    public GrillScreen(GameScreen gameScreen, PiazzaPanic game) {
        Arrays.fill(timePlaced, -1);
        Arrays.fill(flipTime, -1);
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
        Arrays.fill(this.items, null);
        tableSpaces[0] = new TableSpace(300, 200, TableSpaceType.OUTLINE, uiStage);
        tableSpaces[1] = new TableSpace(590, 200, TableSpaceType.OUTLINE, uiStage);
        tableSpaces[2] = new TableSpace(880, 200, TableSpaceType.OUTLINE, uiStage);
        steams[0] = new Steam(286, 280, mainStage);
        steams[1] = new Steam(576, 280, mainStage);
        steams[2] = new Steam(866, 280, mainStage);
        steams[0].setVisible(false);
        steams[1].setVisible(false);
        steams[2].setVisible(false);
        this.inventory = new Inventory(590, 0, 100, 100, 1, uiStage);
        this.dragAndDrop = new DragAndDrop();
        this.dragAndDrop.addTarget(new DragAndDrop.Target(this.inventory) {
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
                    if (game.isVerbose()) System.out.println(source.getActor().getX());
                    accepted[0] = true;
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((IngredientActor) payload.getDragActor()).getIngredient());
                    if (game.isVerbose()) System.out.println("Accepted Inventory");
                    for (int i = 0; i < 3; i++) {
                        IngredientActor actor = items[i];
                        if (game.isVerbose()) System.out.println(actor);
                        if (game.isVerbose()) System.out.println("Searching Inventory");
                        if (actor == source.getActor()) {
                            if (game.isVerbose()) System.out.println("Inventory found " + i);
                            gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((IngredientActor) payload.getDragActor()).getIngredient());
                            if (game.isVerbose()) System.out.println(gameScreen.chefs[gameScreen.getChefSelector()].getInventoryItem().toString());
//                            items[i].remove();
                            items[i] = null;
                            steams[i].setVisible(false);
                            timePlaced[i] = -1;
                            flipped[i] = false;
                            needFlip[i] = false;
                            flipTime[i] = -1;
                            finished[i] = false;
//                            try {
//                                inventoryItem.remove();
//                            } catch (Exception ignored) {
//
//                            }
//                            inventoryItem = null;
                            if (game.isVerbose()) System.out.println("Remove Inventory");
                        }
                    }
                } else accepted[0] = false;
            }
        });
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            this.dragAndDrop.addTarget(new DragAndDrop.Target(tableSpaces[finalI]) {
                final float x = tableSpaces[finalI].getX(), y = tableSpaces[finalI].getY();

                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    if (!unlock[finalI]) {
                        if (game.isVerbose()) System.out.println("Grill drag fail unlock " + finalI);
                        return false;
                    }
                    if (items[finalI] == null) {
                        if (game.isVerbose()) System.out.println("Grill drag allow null " + finalI);
                        getActor().setColor(Color.GREEN);
                        return true;
                    }
                    if (((IngredientActor) source.getActor()).getIngredient().getState() == IngredientState.UNCOOKED) {
                        if (game.isVerbose()) System.out.println("Grill drag allow uncooked " + finalI);
                        getActor().setColor(Color.GREEN);
                        return true;
                    }
                    if (game.isVerbose()) System.out.println("Grill drag fail end " + finalI);
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
                    if (((IngredientActor) source.getActor()).getIngredient().getState() == IngredientState.UNCOOKED) {
                        source.getActor().setPosition(this.x + 10, this.y + 10);
                        accepted[0] = true;
                        items[finalI] = inventoryItem;
                        steams[finalI].setVisible(true);
                        unlock[finalI] = false;
                        gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                        inventoryItem = null;
                        if (game.isVerbose()) System.out.println("Grill remove " + finalI);
                        timePlaced[finalI] = new Date().getTime();
                    } else accepted[0] = false;
                }
            });
        }
        for (int i = 0; i < 3; i++) {
            labels[i] = new Label(null, game.labelStyle[1]);
            labels[i].setAlignment(Align.center);
            labels[i].setPosition(225 + (290 * i), 350);
            labels[i].setSize(250, 100);

            this.uiStage.addActor(labels[i]);
            int finalI = i;
            labels[i].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (game.isVerbose()) System.out.println("Grill touched " + finalI);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (game.isVerbose()) System.out.println("Grill start up " + finalI + " " + items[finalI].getIngredient().getState());
                    if (items[finalI].getIngredient().getState() == IngredientState.UNCOOKED) {
                        needFlip[finalI] = false;
                        flipped[finalI] = true;
                        flipTime[finalI] = new Date().getTime();
                        items[finalI].makeReady();
                        if (game.isVerbose()) System.out.println("Grill invisible " + finalI);
                        labels[finalI].setVisible(false);
                        labels[finalI].setText(null);
                    }
                }
            });
        }
        new Thread(new CheckTimes()).start();
    }

    class CheckTimes implements Runnable {

        @Override
        public void run() {
            while (true) {
                boolean allOff = true;
                for (int i = 0; i < 3; i++) {
                    if (labels[i].isVisible())
                        allOff = false;
                    if (timePlaced[i] != -1) {
                        long timeElapsed = new Date().getTime() - timePlaced[i];
                        if (timeElapsed >= 30000) {
                            if (!flipped[i] && !needFlip[i]) {
                                needFlip[i] = true;
                                if (game.isVerbose()) System.out.println(i + " needs flipping!");
                                labels[i].setText("Flip now!\nPress here to flip");
                                if (game.isVerbose()) System.out.println("Grill thread visible " + i);
                                labels[i].setVisible(true);
                            }
                        }
                    } else if (labels[i].isVisible()) {
                        labels[i].setVisible(false);
                        if (game.isVerbose()) System.out.println("Grill thread invisible " + i);
                    }
                    if (flipTime[i] != -1) {
                        long timeElapsed = new Date().getTime() - flipTime[i];
                        if (timeElapsed >= 30000) {
                            if (!finished[i]) {
                                finished[i] = true;
                                unlock[i] = true;
                                if (game.isVerbose()) System.out.println(i + " has finished!");
                                labels[i].setText("Finished!\nRemove now!");
                                labels[i].setVisible(true);
                                if (game.isVerbose()) System.out.println("Grill thread visible " + i);
                                items[i].makeReady();
                            }
                        }
                        if (timeElapsed >= 60000 && gameScreen.mode == Mode.ASSESSMENT_2) {
                            if (items[i].getIngredient().getState() == IngredientState.COOKED) {
                                items[i].getIngredient().setState(IngredientState.OVERCOOKED);
                                if (game.isVerbose()) System.out.println(i + " is overcooked!");
                                labels[i].setText("Overcooked!\nRemove now!");
                                if (game.isVerbose()) System.out.println("Grill thread visible " + i);
                                labels[i].setVisible(true);
                            }
                        }
                    }
                }
                gameScreen.grillLabel.setVisible(!allOff);
            }
        }
    }

    @Override
    public void update(float dt) {
        Ingredient ingredient = (Ingredient) gameScreen.chefs[gameScreen.getChefSelector()].getInventoryItem();
        IngredientActor ingredientActor = null;
        if (ingredient != null && inventoryItem == null && ingredient.getState() == IngredientState.UNCOOKED) {
            ingredientActor = new IngredientActor(inventory.getX() + 10, inventory.getY() + 10, super.uiStage, ingredient);
            inventoryItem = ingredientActor;
            IngredientActor finalIngredientActor = ingredientActor;
            this.dragAndDrop.addSource(new DragAndDrop.Source(ingredientActor) {
                final float x = finalIngredientActor.getX(), y = finalIngredientActor.getY();

                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    boolean spareSpace = false;
                    for (IngredientActor space : items) {
                        if (game.isVerbose()) System.out.println("Payload loop");
                        if (space == null || (space == finalIngredientActor && inventoryItem == null)) {
                            if (game.isVerbose()) System.out.println("Payload space");
                            spareSpace = true;
                            break;
                        }
                    }
                    if (!spareSpace)
                        return null;
                    if (game.isVerbose()) System.out.println("Payload after");
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(finalIngredientActor);
                    for (int i = 0; i < 3; i++) {
                        IngredientActor actor = items[i];
                        if (game.isVerbose()) System.out.println("Payload loop 2");
                        if (actor == finalIngredientActor) {
                            if (game.isVerbose()) System.out.println("Payload found");
                            if (unlock[i])
                                return payload;
                            return null;
                        }
                    }
                    if (game.isVerbose()) System.out.println("Payload!");
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
                assert ingredientActor != null;
                ingredientActor.remove();
            } catch (Exception ignored) {

            }
            try {
                inventoryItem.remove();
            } catch (Exception ignored) {

            }
            inventoryItem = null;
            if (game.isVerbose()) System.out.println(Arrays.toString(timePlaced));
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play(gameScreen.getMasterVolume());
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    @Override
    public void updateGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
