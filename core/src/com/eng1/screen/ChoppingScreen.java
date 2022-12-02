package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.eng1.PiazzaPanic;
import com.eng1.actor.*;
import com.eng1.base.*;

public class ChoppingScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;
    private IngredientActor ingredientActor;
    private final BaseActor knife;

    public ChoppingScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage), choppingBoard = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        choppingBoard.loadTexture("images/chopping_board.png");
        choppingBoard.setX((gameScreen.width / 2f) - (choppingBoard.getWidth() / 2f));
        this.knife = new BaseActor(0, -100, this.mainStage);
        this.knife.loadTexture("images/knife.png");
        this.knife.setX((gameScreen.width / 2f) - (this.knife.getWidth() / 3f));
        this.gameScreen = gameScreen;
        this.game = game;
        this.ingredientActor = null;
    }

    @Override
    public void update(float dt) {
        if (this.ingredientActor != null) {
            if (this.knife.getY() == 200) {
                MoveToAction action = new MoveToAction();
                action.setX(this.knife.getX());
                action.setY(500);
                action.setDuration(0.5f);
                this.knife.addAction(action);
            }
            if (this.knife.getY() == 500) {
                MoveToAction action = new MoveToAction();
                action.setX(this.knife.getX());
                action.setY(200);
                action.setDuration(0.5f);
                this.knife.addAction(action);
            }
        } else if (this.knife.getY() != -100f)
            this.knife.setY(-100f);

        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
        }
    }
}
