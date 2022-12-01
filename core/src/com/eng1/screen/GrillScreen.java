package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.eng1.PiazzaPanic;
import com.eng1.base.*;

public class GrillScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;
    private final Object[] items;

    public GrillScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage),
                grill = new BaseActor(0, 0, this.mainStage),
                hood = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/stainless.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        grill.loadTexture("images/grill.jpg");
        grill.setSize(gameScreen.width, gameScreen.width / grill.getWidth() * grill.getHeight());
        hood.loadTexture("images/hood.png");
        hood.setSize(gameScreen.width, gameScreen.width / hood.getWidth() * hood.getHeight());
        hood.setY(gameScreen.height - hood.getHeight());
        this.gameScreen = gameScreen;
        this.game = game;
        this.items = new Object[3];
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
        }
    }
}
