package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.eng1.enums.ItemTableType;
import com.eng1.PiazzaPanic;
import com.eng1.actor.Table;
import com.eng1.base.*;

public class ItemTableScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;
    private final ItemTableType itemTableType;

    public ItemTableScreen(GameScreen gameScreen, PiazzaPanic game, ItemTableType itemTableType) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "assets/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        new Table(this.mainStage);
        this.gameScreen = gameScreen;
        this.game = game;
        this.itemTableType = itemTableType;
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
        }
    }
}
