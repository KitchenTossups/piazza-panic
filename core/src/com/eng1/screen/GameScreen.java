package com.eng1.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.*;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.non_actor.*;
import com.eng1.room.*;

import java.util.*;

@SuppressWarnings("unused")
public class GameScreen extends BaseScreen {

    final PiazzaPanic game;
    final Mode mode;
    final OrthographicCamera camera;
    final List<Station> stations;
    final List<Counter> counters;
    final Chef[] chefs;
    final List<Customer> customers;
    final List<IngredientActor> ingredientActors;
    final Label messageLabel;
    private boolean tabPressed;
    final int width, height;
    private int chefSelector = 0, binnedItems = 0;
    private long messageTimer = -1, movementTimer = 0;

    public GameScreen(PiazzaPanic game, int width, int height, Mode mode, float loci) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.messageLabel = new Label(null, this.game.labelStyle[1]);
        this.messageLabel.setAlignment(Align.center);
        this.tabPressed = false;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);

        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("background/background.png");
        background.setSize(this.width, this.height);

        this.counters = new ArrayList<>();

        this.counters.add(new Counter(0f, height - 80f, (int) (width / 5f * 4f), 80, this.mainStage)); // Top
        this.counters.add(new Counter(0f, 0f, (int) (width / 5f * 4f), 80, this.mainStage));   // Bottom
        this.counters.add(new Counter(0f, 0f, 80, height, this.mainStage));    // Left
        this.counters.add(new Counter((width / 5f * 4f), 0f, 80, height, this.mainStage));    // Right
//        this.counters.add(new Counter(width / 5f + 40f, (height / 3f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage));  // Counter 1
//        this.counters.add(new Counter(width / 5f + 40f, (height / 3f * 2f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage)); // Counter 2
        this.counters.add(new Counter(width / 5f + 40f, (height / 2f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage));

        this.customers = new ArrayList<>();

        this.customers.add(new Customer(1300, 100, this.uiStage, new Recipe(Product.BURGER)));

        for (Customer customer : this.customers) {
            MoveToAction action = new MoveToAction();
            action.setX(1150);
            action.setY(100);
            action.setDuration(3);
            customer.addAction(action);
            customer.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        if (customer.getOrderPlaced() <= new Date().getTime())
                            GameScreen.this.game.setActiveScreen(new OrderScreen(customer, GameScreen.this.game.labelStyle, GameScreen.this, GameScreen.this.game));
                    } catch (Exception ignored) {

                    }
                }
            });
        }

        if (mode == Mode.ASSESSMENT_1)
            this.chefs = new Chef[2];
        else
            this.chefs = new Chef[3];

        this.chefs[0] = new Chef(90, 90, this.mainStage, 1);
        this.chefs[1] = new Chef(90, 130, this.mainStage, 2);
        if (mode == Mode.ASSESSMENT_2) this.chefs[2] = new Chef(90, 170, this.mainStage, 3);

        this.ingredientActors = new ArrayList<>();

//        this.ingredientActors.add(new IngredientActor(200f, 300f, this.mainStage, new Ingredient(Item.TEST_4, IngredientState.UNCOOKED)));
//        this.ingredientActors.add(new IngredientActor(200f, 350f, this.mainStage, new Ingredient(Item.TEST_5, IngredientState.UNCOOKED)));

        Label oldestOrder = new Label("Click here for the oldest order!", this.game.labelStyle[1]);
        this.uiTable.pad(10);
        this.uiTable.add(oldestOrder).expandX().align(Align.topRight);
        this.uiTable.row();
        this.uiTable.add(this.messageLabel).expand().align(Align.center);

        oldestOrder.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if (GameScreen.this.customers.get(0).getOrderPlaced() <= new Date().getTime())
                        GameScreen.this.game.setActiveScreen(new OrderScreen(GameScreen.this.customers.get(0), GameScreen.this.game.labelStyle, GameScreen.this, GameScreen.this.game));
                } catch (Exception ignored) {

                }
            }
        });

        this.stations = new ArrayList<>();

        this.stations.add(new Station(width / 5f + 40f, height - 80f, 80, 80, loci, StationType.PREP, this, this.game));
        this.stations.add(new Station((width / 5f + 40f) + 136f + 80f, height - 80f, 80, 80, loci, StationType.PREP, this, this.game));
        this.stations.add(new Station((width / 5f + 40f) + 2f * (136f + 80f), height - 80f, 80, 80, loci, StationType.PREP, this, this.game));
        this.stations.add(new Station(0, 0, 80, 80, loci, StationType.BIN, this.mainStage));
        this.stations.add(new Station(width / 5f + 40f, 0, 80, 80, loci, FoodChestType.DRY_FOOD, this.mainStage));
        this.stations.add(new Station((width / 5f + 40f) + 136f + 80f, 0, 80, 80, loci, FoodChestType.FRESH_FOOD, this.mainStage));
        this.stations.add(new Station((width / 5f + 40f) + 2f * (136f + 80f), 0, 80, 80, loci, FoodChestType.FROZEN_FOOD, this.mainStage));
        this.stations.add(new Station(0, height / 2f - 40f, 80, 80, loci, StationType.CHOPPING, this, this.game));
//        this.stations.add(new Station(width / 5f + 40f, (height / 3f * 2f) - 40f, 80, 80, loci, StationType.COUNTER, this, this.game));
//        this.stations.add(new Station((width / 5f + 40f) + 136f + 80f, (height / 3f * 2f) - 40f, 80, 80, loci, StationType.COUNTER, this, this.game));
//        this.stations.add(new Station((width / 5f + 40f) + 2f * (136f + 80f), (height / 3f * 2f) - 40f, 80, 80, loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station(width / 5f + 40f, (height / 2f) - 40f, 80, 80, loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station((width / 5f + 40f) + 136f + 80f, (height / 2f) - 40f, 80, 80, loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station((width / 5f + 40f) + 2f * (136f + 80f), (height / 2f) - 40f, 80, 80, loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station((width / 5f * 4f), (height / 3f) - 40f, 80, 280, loci, StationType.SERVING, this.mainStage));
        this.stations.add(new Station(0, height - 80f, 80, 80, loci, StationType.GRILL, this, this.game));
    }

    public void update(float dt) {
        this.timerCheck();
        if (this.customers.get(0).getX() == 1150 && this.customers.get(0).getY() == 100)
            this.movementTimer++;
        if (this.movementTimer == 5 * 60) {
            this.movementTimer = 0;
            MoveToAction action = new MoveToAction();
            action.setX(1150);
            action.setY(550);
            action.setDuration(3);
            this.customers.get(0).addAction(action);
        }
        try {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() + 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() - 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() + 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() - 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.TAB) && !Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                if (!this.tabPressed)
                    switch (this.mode) {
                        case ASSESSMENT_1:
                            this.chefSelector++;
                            if (this.chefSelector == 2) this.chefSelector = 0;
                            break;
                        case ASSESSMENT_2:
                            this.chefSelector++;
                            if (this.chefSelector == 3) this.chefSelector = 0;
                            break;
                    }
                this.tabPressed = true;
            } else
                this.tabPressed = false;
            if (Gdx.input.isKeyPressed(Input.Keys.E))
                this.stationProximity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCollision(float oldX, float oldY) {
        if (this.chefs[this.chefSelector].getX() + this.chefs[this.chefSelector].getWidth() > this.width || this.chefs[this.chefSelector].getX() < 0)
            this.chefs[this.chefSelector].setX(oldX);
        if (this.chefs[this.chefSelector].getY() + this.chefs[this.chefSelector].getHeight() > this.height || this.chefs[this.chefSelector].getY() < 0)
            this.chefs[this.chefSelector].setY(oldY);
        for (Counter counter : this.counters)
            if (counter.getBoundaryRectangle().overlaps(this.chefs[this.chefSelector].getBoundaryRectangle())) {
                this.chefs[this.chefSelector].setX(oldX);
                this.chefs[this.chefSelector].setY(oldY);
            }
        switch (this.chefSelector) {
            case 0:
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[2].getBoundaryRectangle())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[1].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                break;
            case 1:
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[2].getBoundaryRectangle())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[0].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                break;
            case 2:
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[0].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[1].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                break;
        }
    }

    private void stationProximity() {
        for (Station station : this.stations) {
            if (station.getLociRectangle().overlaps(this.chefs[this.chefSelector].getBoundaryRectangle()))
                switch (station.getStationType()) {
                    case BIN:
                        if (this.chefs[this.chefSelector].getInventoryItem() == null) {
                            this.messageLabel.setText("This chef has nothing in their inventory!\nYou can't bin emptiness!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        } else
                            this.game.setActiveScreen(new BinScreen(this, this.game));
                        break;
                    case CHOPPING:
                        if (this.chefs[this.chefSelector].getInventoryItem() == null) {
                            if (((ChoppingScreen) station.getScreen()).getIngredientActor() == null) {
                                this.messageLabel.setText("This chef has nothing in their inventory!\nYou aren't allowed to chop yourself!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            } else
                                this.game.setActiveScreen(station.getScreen());
                        } else if (this.chefs[this.chefSelector].getInventoryItem() instanceof Ingredient) {
                            if (((Ingredient) this.chefs[this.chefSelector].getInventoryItem()).getState() == IngredientState.UNCUT) {
                                this.game.setActiveScreen(station.getScreen());
                            } else if (((Ingredient) this.chefs[this.chefSelector].getInventoryItem()).getState() == IngredientState.CUT) {
                                this.messageLabel.setText("This chef has cut ingredients in their inventory!\nYou can't cut an item twice!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            } else {
                                this.messageLabel.setText("This chef has nothing in their inventor that can be cut!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            }
                        } else {
                            this.messageLabel.setText("This chef has no ingredient in their inventory!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        }
                        break;
                    case COUNTER:
                        System.out.println(StationType.COUNTER);
                        this.game.setActiveScreen(station.getScreen());
                        break;
                    case FOOD_CHEST:
                        if (this.chefs[this.chefSelector].getInventoryItem() != null) {
                            this.messageLabel.setText("This chef has something in their ingredient!\nYou have no inventory space available!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        } else {
                            this.game.setActiveScreen(new FoodChestScreen(this, this.game));
                        }
                        break;
                    case GRILL:
                        System.out.println(StationType.GRILL);
                        this.game.setActiveScreen(station.getScreen());
                    case PREP:
                        System.out.println(StationType.PREP);
                        this.game.setActiveScreen(station.getScreen());
                        break;
                    case SERVING:
                        if (this.chefs[this.chefSelector].getInventoryItem() instanceof FoodActor)
                            if (((FoodActor) (this.chefs[this.chefSelector].getInventoryItem())).getFood().ready()) {
                                System.out.println("SERVED!");
                                this.messageLabel.setText(String.format("Dinner is served!\nYou have served %s!", ((FoodActor) this.chefs[this.chefSelector].getInventoryItem()).getFood().getRecipe().getEndProduct()));
                                this.messageTimer = new Date().getTime() + 5000L;
                            } else {
                                this.messageLabel.setText("This chef has nothing able to be served in their inventory!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            }
                        else {
                            this.messageLabel.setText("This chef has nothing able to be served in their inventory!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        }
                        break;
                    default:
                        System.out.println("Invalid station type: " + station.getStationType());
                        break;
                }
        }
    }

    /**
     * This checks if a message has been on the screen for a set amount of time and removes it
     */
    private void timerCheck() {
        if (this.messageTimer != -1)
            if (new Date().getTime() >= this.messageTimer) {
                this.messageTimer = -1;
                this.messageLabel.setText(null);
            }
    }

    /**
     * Get the selected chef number
     * @return int chefSelector
     */
    public int getChefSelector() {
        return this.chefSelector;
    }

    /**
     * Get the number of binned items
     * @return int binnedItems
     */
    public int getBinnedItems() {
        return this.binnedItems;
    }

    /**
     * Increase the number of binned items
     */
    public void increaseBinnedItems() {
        this.binnedItems++;
    }

    /**
     * Get the main stage
     * @return Stage mainStage
     */
    public Stage getMainStage() {
        return this.mainStage;
    }
}
