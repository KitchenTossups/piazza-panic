package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.eng1.PiazzaPanic;
import com.eng1.base.*;

public class ChoppingScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;

    public ChoppingScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        this.gameScreen = gameScreen;
        this.game = game;
        System.out.println("Chopping Screen");
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
        }
    }
}
