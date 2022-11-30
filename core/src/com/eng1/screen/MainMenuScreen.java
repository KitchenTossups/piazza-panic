package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.eng1.*;
import com.eng1.base.*;
import com.eng1.enums.Mode;

public class MainMenuScreen extends BaseScreen {

    private final PiazzaPanic game;
    private final int width, height;
    private final Mode mode;
    private final float loci;

    public MainMenuScreen(PiazzaPanic game, int width, int height, Mode mode, float loci) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.loci = loci;

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/main_menu.png" );
        background.setSize(this.width, this.height);
    }
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(new GameScreen(this.game, this.width, this.height, this.mode, this.loci));
            this.dispose();
        }
    }
}
