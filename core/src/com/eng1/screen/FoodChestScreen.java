package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.eng1.PiazzaPanic;
import com.eng1.base.BaseActor;
import com.eng1.base.BaseScreen;

public class FoodChestScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;

    public FoodChestScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("assets/background.png");
        background.setSize(gameScreen.width, gameScreen.height);
        this.gameScreen = gameScreen;
        this.game = game;
        System.out.println("Food Chest Screen");
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
        }
    }
}
