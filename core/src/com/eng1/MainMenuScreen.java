package com.eng1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final PiazzaPanic game;
    int width, height;
    boolean extraChef;
    OrthographicCamera camera;

    public MainMenuScreen(PiazzaPanic game, int width, int height, boolean extraChef) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.extraChef = extraChef;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        this.camera.update();
        this.game.batch.setProjectionMatrix(this.camera.combined);

        this.game.batch.begin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SacredCzars-mLE0G.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
        this.game.font = font;
        this.game.font.draw(this.game.batch, "Welcome to Kitchen Tossup's Piazza Panic!!! ", 100, 150);
        this.game.font.draw(this.game.batch, "Touch anywhere to begin!", 100, 100);
        this.game.batch.end();

        if (Gdx.input.isTouched()) {
            this.game.setScreen(new GameScreen(this.game, this.width, this.height, this.extraChef));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
