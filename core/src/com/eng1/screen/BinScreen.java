package com.eng1.screen;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.eng1.*;
import com.eng1.base.*;

public class BinScreen extends BaseScreen {

    public BinScreen(GameScreen gameScreen, PiazzaPanic game) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "assets/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        Label titleLabel = new Label("This is the bin!", game.labelStyle[0]), contents = new Label(String.format("You are currently holding: %s\nAre you sure you want to bin this?", gameScreen.chefs[gameScreen.getChefSelector()].getInventoryItem()), game.labelStyle[1]), bin = new Label("BIN!", game.labelStyle[1]), cancel = new Label("CANCEL!", game.labelStyle[1]);
        titleLabel.setAlignment(Align.center);
        contents.setAlignment(Align.center);
        bin.setAlignment(Align.center);
        cancel.setAlignment(Align.center);
        this.uiTable.pad(50);
        this.uiTable.add(titleLabel).expandX();
        this.uiTable.row();
        this.uiTable.add(contents).expandX();
        this.uiTable.row();
        this.uiTable.add(bin).expand();
        this.uiTable.row();
        this.uiTable.add(cancel).expand();
        bin.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.chefs[gameScreen.getChefSelector()].setInventoryItem(null);
                gameScreen.increaseBinnedItems();
                game.setActiveScreen(gameScreen);
            }
        });
        cancel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setActiveScreen(gameScreen);
            }
        });
    }

    @Override
    public void update(float dt) {

    }
}
