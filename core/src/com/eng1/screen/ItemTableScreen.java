package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.eng1.enums.ItemTableType;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;

public class ItemTableScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;
    private final ItemTableType itemTableType;
    private final Object[] items;

    public ItemTableScreen(GameScreen gameScreen, PiazzaPanic game, ItemTableType itemTableType) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        new Table(this.mainStage);
        this.gameScreen = gameScreen;
        this.game = game;
        this.itemTableType = itemTableType;
        this.items = new Object[3];
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
        }
    }
}
