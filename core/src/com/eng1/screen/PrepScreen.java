package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.non_actor.Food;
import com.eng1.non_actor.Ingredient;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class PrepScreen extends BaseScreen {

    private GameScreen gameScreen;
    private final PiazzaPanic game;
    private final FoodActor[] items;
    private final DragAndDrop dragAndDrop;
    BaseActor inventoryItem = null;
    private final Inventory inventory;
    final TableSpace[] tableSpaces = new TableSpace[3];
    private final Label addNextLabel;
    private final Label[] placeLabel = new Label[3];
    private final boolean[] accepted = {false, false, false, false};

    public PrepScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("background/background.png");
        background.setSize(gameScreen.width, gameScreen.height);
        new Table(this.mainStage);
        this.inventory = new Inventory(uiStage);
        this.tableSpaces[0] = new TableSpace(300, 150, TableSpaceType.OUTLINE_OPEN_TOP, uiStage);
        this.tableSpaces[1] = new TableSpace(590, 150, TableSpaceType.OUTLINE_OPEN_TOP, uiStage);
        this.tableSpaces[2] = new TableSpace(880, 150, TableSpaceType.OUTLINE_OPEN_TOP, uiStage);
        this.gameScreen = gameScreen;
        this.game = game;
        this.items = new FoodActor[3];
        Arrays.fill(this.items, null);
        this.uiTable.pad(10);
        this.uiTable.add(new Label("Press \"Q\" to exit this screen", this.game.labelStyle[1])).expand().align(Align.topRight);
        this.uiTable.row();
        for (int i = 0; i < 3; i++) {
            placeLabel[i] = new Label(null, game.labelStyle[1]);
            placeLabel[i].setAlignment(Align.center);
            placeLabel[i].setPosition(225 + (290 * i), i == 1 ? 500 : 400);
            placeLabel[i].setSize(250, 100);
            placeLabel[i].setVisible(false);

            this.uiStage.addActor(placeLabel[i]);
        }
        this.dragAndDrop = new DragAndDrop();
        this.dragAndDrop.addTarget(new DragAndDrop.Target(this.inventory) {
            final float x = inventory.getX(), y = inventory.getY();

            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if (game.isVerbose()) System.out.println("Prep inventory drag init");
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
                source.getActor().setPosition(this.x, this.y);
                accepted[0] = true;
                BaseActor object = (BaseActor) payload.getDragActor();
                if (object instanceof IngredientActor)
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((IngredientActor) object).getIngredient());
                else if (object instanceof FoodActor)
                    gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(((FoodActor) object).getFood());
                for (int i = 0; i < 3; i++) {
                    FoodActor actor = items[i];
                    if (game.isVerbose()) System.out.println(actor);
                    if (game.isVerbose()) System.out.println("Prep searching");
                    if (actor == source.getActor()) {
                        if (game.isVerbose()) System.out.println("Prep found " + i);
                        items[i].remove();
                        items[i] = null;
                        placeLabel[i].setVisible(false);
                    }
                }
                ((FoodActor) source.getActor()).moveAll(inventory.getX(), inventory.getY());
            }
        });
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            if (game.isVerbose()) System.out.println("Prep drag init " + i);
            this.dragAndDrop.addTarget(new DragAndDrop.Target(tableSpaces[finalI]) {
                final float x = tableSpaces[finalI].getX(), y = tableSpaces[finalI].getY();

                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    if (game.isVerbose()) System.out.println("Prep drag");
                    if (items[finalI] == null) {
                        if (source.getActor() instanceof FoodActor) {
                            if (game.isVerbose()) System.out.println("Prep drag allow null " + finalI);
                            getActor().setColor(Color.GREEN);
                            return true;
                        }
                    } else if (source.getActor() instanceof IngredientActor) {
                        if (items[finalI].getStep() != -1) {
                            if (game.isVerbose()) System.out.println("Prep drag allow not null " + finalI);
                            getActor().setColor(Color.GREEN);
                            return true;
                        }
                    }
                    if (game.isVerbose()) System.out.println("Prep drag fail end " + finalI);
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
                    if (game.isVerbose()) System.out.println(source.getActor());
                    if (source.getActor() instanceof IngredientActor) {
                        if (items[finalI].getStep() != -1) {
                            source.getActor().setPosition(this.x + 10, this.y + 10);
                            boolean accept = items[finalI].addNextItem(((IngredientActor) source.getActor()).getIngredient());
                            if (!accept) {
                                accepted[0] = false;
                            } else {
                                accepted[0] = true;
                                inventoryItem.remove();
                                inventoryItem = null;
                                gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                                if (items[finalI].getStep() == -1) {
                                    placeLabel[finalI].setText(String.format("%s\nComplete!", items[finalI].getFood().getRecipe().getEndProduct().toString()));
                                    items[finalI].changeStage(uiStage);
                                    dragAndDrop.addSource(new DragAndDrop.Source(items[finalI]) {
                                        final float x = items[finalI].getX(), y = items[finalI].getY();

                                        @Override
                                        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                                            if (game.isVerbose()) System.out.println("Add FoodActor drag start");
                                            if (inventoryItem != null) {
                                                if (game.isVerbose()) System.out.println("Add FoodActor drag start fail inventory");
                                                return null;
                                            }
                                            if (game.isVerbose()) System.out.println("Add FoodActor payload after");
                                            DragAndDrop.Payload payload = new DragAndDrop.Payload();
                                            payload.setDragActor(items[finalI]);
                                            if (game.isVerbose()) System.out.println("Add FoodActor payload!");
                                            return payload;
                                        }

                                        @Override
                                        public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                                            if (!accepted[0]) {
                                                payload.getDragActor().setPosition(this.x, this.y);
                                            }
                                        }
                                    });
                                } else
                                    placeLabel[finalI].setText(String.format("%s\nNext ingredient: %s", items[finalI].getFood().getRecipe().getEndProduct().toString(), items[finalI].getFood().getRecipe().getIngredientsRaw().get(items[finalI].getStep())));
                                placeLabel[finalI].setVisible(true);
                                if (game.isVerbose()) System.out.println("Prep remove " + finalI);
                            }
                        } else
                            accepted[0] = false;
                    } else if (source.getActor() instanceof FoodActor) {
                        source.getActor().setPosition(this.x, this.y);
                        accepted[0] = true;
                        items[finalI] = (FoodActor) source.getActor();
                        items[finalI].changeStage(mainStage);
                        inventoryItem.remove();
                        inventoryItem = null;
                        gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                    } else
                        accepted[0] = false;
                }
            });
        }
        addNextLabel = new Label("Get next order", game.labelStyle[1]);
        addNextLabel.setPosition(10, gameScreen.height - 46);
        addNextLabel.setAlignment(Align.center);
        this.uiStage.addActor(addNextLabel);
        addNextLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.isVerbose()) System.out.println("Prep add touched");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (gameScreen.getLastFoodActorCustomerTime() == -1) {
                    if (game.isVerbose()) System.out.println("Prep add -1");
                    Customer customer = gameScreen.customers.get(0);
                    if (new Date().getTime() >= customer.getOrderPlaced()) {
                        for (int j = 0; j < 3; j++) {
                            if (items[j] == null) {
                                if (game.isVerbose()) System.out.println("Prep add found null");
                                items[j] = new FoodActor(tableSpaces[j].getX(), tableSpaces[j].getY(), mainStage, new Food(customer.getOrder(), customer.getOrderPlaced()), game.isVerbose());
                                if (game.isVerbose()) System.out.println(items[j]);
                                placeLabel[j].setText(String.format("%s\nNext ingredient: %s", customer.getOrder().getEndProduct().toString(), items[j].getFood().getRecipe().getIngredientsRaw().get(items[j].getStep())));
                                placeLabel[j].setVisible(true);
                                gameScreen.setLastFoodActorCustomerTime(customer.getOrderPlaced());
                                break;
                            }
                        }
                    } else if (game.isVerbose()) System.out.println("Prep add time");
                } else {
                    if (game.isVerbose()) System.out.println("Prep add not -1");
                    for (int i = 0; i < gameScreen.customers.size(); i++) {
                        if (gameScreen.customers.get(i).getOrderPlaced() == gameScreen.getLastFoodActorCustomerTime()) {
                            i++;
                            try {
                                Customer customer = gameScreen.customers.get(i);
                                if (new Date().getTime() >= customer.getOrderPlaced()) {
                                    for (int j = 0; j < 3; j++) {
                                        if (items[j] == null) {
                                            if (game.isVerbose()) System.out.println("Prep add found null");
                                            items[j] = new FoodActor(tableSpaces[j].getX(), tableSpaces[j].getY(), mainStage, new Food(customer.getOrder(), customer.getOrderPlaced()), game.isVerbose());
                                            placeLabel[j].setText(String.format("%s\nNext ingredient: %s", customer.getOrder().getEndProduct().toString(), items[j].getFood().getRecipe().getIngredientsRaw().get(items[j].getStep())));
                                            placeLabel[j].setVisible(true);
                                            gameScreen.setLastFoodActorCustomerTime(customer.getOrderPlaced());
                                            break;
                                        }
                                    }
                                } else if (game.isVerbose()) System.out.println("Prep add time");
                            } catch (Exception ignored) {
                                if (game.isVerbose()) System.out.println("Prep add end");
                                addNextLabel.setVisible(false);
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void update(float dt) {
        boolean allFilled = true;
        for (FoodActor foodActor : items)
            if (foodActor == null) {
                allFilled = false;
                break;
            }
        boolean thereIsNext = true;
        if (gameScreen.getLastFoodActorCustomerTime() != -1) {
            for (int i = 0; i < gameScreen.customers.size(); i++) {
                if (gameScreen.customers.get(i).getOrderPlaced() == gameScreen.getLastFoodActorCustomerTime()) {
                    i++;
                    try {
                        Customer customer = gameScreen.customers.get(i);
                        if (customer == null || new Date().getTime() < customer.getOrderPlaced())
                            thereIsNext = false;
                    } catch (Exception ignored) {
                        thereIsNext = false;
                    }
                }
            }
        }
        addNextLabel.setVisible(!allFilled && thereIsNext);
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
                        if (game.isVerbose()) System.out.println("Prep payload loop");
                        if (space == null || (space == finalActor && inventoryItem == null)) {
                            if (game.isVerbose()) System.out.println("Prep payload space");
                            spareSpace = true;
                            break;
                        }
                    }
                    if (!spareSpace && finalActor instanceof FoodActor)
                        return null;
                    if (game.isVerbose()) System.out.println("Prep payload after");
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(finalActor);
                    if (game.isVerbose()) System.out.println("Prep payload!");
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
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play();
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    @Override
    public void updateGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
