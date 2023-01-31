package com.eng1.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.eng1.PiazzaPanic;
import com.eng1.base.BaseActor;
import com.eng1.base.BaseScreen;

public class PauseScreen extends BaseScreen {
    private final GameScreen gameScreen;
    private final PiazzaPanic game;

    public PauseScreen(GameScreen gameScreen, PiazzaPanic game){
        this.gameScreen = gameScreen;
        this.game = game;

        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/background.png" );
        background.setSize(gameScreen.width, gameScreen.height);

        Label titleLabel  = new Label("Pause", game.labelStyle[0]);
        titleLabel.setAlignment(Align.center);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.labelStyle[1].font;
        textButtonStyle.fontColor = game.labelStyle[1].fontColor;
        TextButton muteTextButton = new TextButton("Mute", textButtonStyle);

        TextButton quitTextButton = new TextButton("Quit to desktop", textButtonStyle);

        this.uiTable.pad(50);
        this.uiTable.row().height(100);
        this.uiTable.add(titleLabel).expandX();

        this.uiTable.row().height(100);
        this.uiTable.add(muteTextButton).expandX();

        this.uiTable.row().height(100);
        this.uiTable.add(quitTextButton).expandX();

        muteTextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameScreen.getMasterVolume() == 1f){
                    gameScreen.setMasterVolume(0f);
                    gameScreen.pauseBackGroundMusic();
                }else {
                    gameScreen.setMasterVolume(1f);
                    gameScreen.playBackGroundMusic();
                }
            }
        });

        quitTextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.exit(0);
            }
        });
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            this.game.setActiveScreen(this.gameScreen);
            dispose();
        }
        return true;
    }
}