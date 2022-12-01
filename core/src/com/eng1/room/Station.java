package com.eng1.room;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.screen.*;

@SuppressWarnings("unused")
public class Station extends BaseActor {

    private final StationType stationType;

    private Object storage = null;

    private BaseScreen screen = null;

    private FoodChestType foodChestType = null;

    /**
     * This is for the stations that have to store items on the page, it retains the data instead of creating a new screen
     * @param x x position
     * @param y y position
     * @param width width
     * @param height height
     * @param loci loci
     * @param stationType station type
     * @param gameScreen GameScreen
     * @param game Piazza Panic game
     * @see StationType
     * @see GameScreen
     * @see PiazzaPanic
     */
    public Station(float x, float y, int width, int height, float loci, StationType stationType, GameScreen gameScreen, PiazzaPanic game) {
        super(x, y, gameScreen.getMainStage(), loci);

        this.stationType = stationType;

        switch (stationType) {
            case CHOPPING:
                this.loadTexture("squared/blue.png", width, height);
                this.screen = new ChoppingScreen(gameScreen, game);
                break;
            case COUNTER:
                this.loadTexture("squared/purple.png", width, height);
                this.screen = new ItemTableScreen(gameScreen, game, ItemTableType.COUNTER_SCREEN);
                break;
            case GRILL:
                this.loadTexture("squared/brown.png", width, height);
                this.screen = new GrillScreen(gameScreen, game);
                break;
            case PREP:
                this.loadTexture("squared/red.png", width, height);
                this.screen = new ItemTableScreen(gameScreen, game, ItemTableType.PREP_SCREEN);
                break;
            default:
                System.out.println("Invalid use of Station with station type!");
        }
    }

    public Station(float x, float y, int width, int height, float loci, StationType stationType, Stage s) {
        super(x, y, s, loci);

        this.stationType = stationType;

        switch (stationType) {
            case BIN:
                this.loadTexture("images/bin.png", width, height);
                break;
            case SERVING:
                this.loadTexture("squared/green.png", width, height);
                break;
            default:
                System.out.println("Invalid use of Station with station type!");
        }
    }

    public Station(float x, float y, int width, int height, float loci, FoodChestType foodChestType, Stage s) {
        super(x, y, s, loci);

        this.stationType = StationType.FOOD_CHEST;
        this.foodChestType = foodChestType;

        this.loadTexture("squared/yellow.png", width, height);
    }

    public StationType getStationType() {
        return this.stationType;
    }

    public Object getStorage() {
        return this.storage;
    }

    public void setStorage(Object storage) {
        this.storage = storage;
    }

    public BaseScreen getScreen() {
        return this.screen;
    }

    public FoodChestType getFoodChestType() {
        return this.foodChestType;
    }
}
