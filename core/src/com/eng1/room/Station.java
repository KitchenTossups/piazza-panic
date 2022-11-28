package com.eng1.room;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.StationType;
import com.eng1.base.BaseActor;

public class Station extends BaseActor {

    private final StationType stationType;

    private Object storage;

    public Station(float x, float y, int width, int height, float loci, Stage s, StationType stationType) {
        super(x, y, s, loci);

        switch (stationType) {
            case BIN -> this.loadTexture("bin.png", width, height);
            case SERVING -> this.loadTexture("green.png", width, height);
            case FOOD_CHEST -> this.loadTexture("yellow.png", width, height);
            case CHOPPING -> this.loadTexture("blue.png", width, height);
            case COUNTER -> this.loadTexture("purple.png", width, height);
            case PREP -> this.loadTexture("red.png", width, height);
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
