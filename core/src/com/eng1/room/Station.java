package com.eng1.room;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.screen.*;

@SuppressWarnings("unused")
public class Station extends BaseActor {

    private final StationType stationType;

    private Object storage;

    public Station(float x, float y, int width, int height, float loci, Stage s, StationType stationType) {
        super(x, y, s, loci);

        switch (stationType) {
            case BIN:
                this.loadTexture("bin.png", width, height);
                break;
            case SERVING:
                this.loadTexture("green.png", width, height);
                break;
            case FOOD_CHEST:
                this.loadTexture("yellow.png", width, height);
                break;
            case CHOPPING:
                this.loadTexture("blue.png", width, height);
                break;
            case COUNTER:
                this.loadTexture("purple.png", width, height);
                this.screen = new ItemTableScreen(gameScreen, game, ItemTableType.COUNTER_SCREEN);
                break;
            case PREP:
                this.loadTexture("red.png", width, height);
                this.screen = new ItemTableScreen(gameScreen, game, ItemTableType.PREP_SCREEN);
                break;
            default:
                System.out.println("Invalid use of Station with station type!");
        }

        this.stationType = stationType;
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
}
