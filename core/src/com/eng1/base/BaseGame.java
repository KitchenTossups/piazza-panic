package com.eng1.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public abstract class BaseGame extends Game {
    /**
     * Stores reference to game; used when calling setActiveScreen method.
     */
    private final BaseGame game;

    public LabelStyle[] labelStyle;

    /**
     * Called when game is initialized; stores global reference to game object.
     */
    public BaseGame() {
        this.game = this;
    }

    /**
     * Called when game is initialized,
     * after Gdx.input and other objects have been initialized.
     */
    public void create() {
        // prepare for multiple classes/stages/actors to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);

        // parameters for generating a custom bitmap font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.size = 36;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = TextureFilter.Linear;
        fontParameters.magFilter = TextureFilter.Linear;

        BitmapFont customFont1 = fontGenerator.generateFont(fontParameters);

        fontParameters.size = 24;

        BitmapFont customFont2 = fontGenerator.generateFont(fontParameters);

        this.labelStyle = new LabelStyle[2];

        this.labelStyle[0] = new LabelStyle();
        this.labelStyle[0].font = customFont1;

        this.labelStyle[1] = new LabelStyle();
        this.labelStyle[1].font = customFont2;
    }

    /**
     * Used to switch screens while game is running.
     */
    public void setActiveScreen(BaseScreen s) {
        this.game.setScreen(s);
    }
}
