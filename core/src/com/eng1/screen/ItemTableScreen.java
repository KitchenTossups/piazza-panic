package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.eng1.enums.ItemTableType;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.TableSpaceType;

public class ItemTableScreen extends BaseScreen {

    private GameScreen gameScreen;
    private final PiazzaPanic game;
    private final ItemTableType itemTableType;
    private final Object[] items;

    public ItemTableScreen(GameScreen gameScreen, PiazzaPanic game, ItemTableType itemTableType) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);
        new Table(this.mainStage);
        Inventory inventory = new Inventory(uiStage);
        TableSpace[] tableSpaces = new TableSpace[3];
        tableSpaces[0] = new TableSpace(300, 150, TableSpaceType.OUTLINE, mainStage);
        tableSpaces[1] = new TableSpace(590, 150, TableSpaceType.OUTLINE, mainStage);
        tableSpaces[2] = new TableSpace(880, 150, TableSpaceType.OUTLINE, mainStage);
        this.gameScreen = gameScreen;
        this.game = game;
        this.itemTableType = itemTableType;
        this.items = new Object[3];
        this.uiTable.pad(10);
        this.uiTable.add(new Label("Press \"Q\" to exit this screen", this.game.labelStyle[1])).expand().align(Align.topRight);
        this.uiTable.row();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Gdx.audio.newSound(Gdx.files.internal("sounds/CloseStation.mp3")).play();
            dispose();
            this.game.setActiveScreen(this.gameScreen);
        }
    }

    @Override
    public void updateGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
