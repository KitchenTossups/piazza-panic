package com.eng1.base;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.eng1.screen.GameScreen;

import java.util.Objects;

/**
 * Makes creation of a screen simpler
 * <p>
 * Original credit - <a href="https://github.com/mariorez/libgdx-maze-runman">mariorez</a>
 *
 * @author Lee Stemkoski
 * @author Liam Burnand (modified code)
 */
public abstract class BaseScreen implements Screen, InputProcessor {
    protected Stage mainStage, uiStage;
    protected Table uiTable;

    public BaseScreen() {
        this.mainStage = new Stage();
        this.uiStage = new Stage();

        this.uiTable = new Table();
        this.uiTable.setFillParent(true);
        this.uiStage.addActor(this.uiTable);
    }

    public abstract void update(float dt);

    // this is the game loop. update, then render.
    public void render(float dt) {
        // limit amount of time that can pass while window is being dragged
        dt = Math.min(dt, 1 / 30f);

        // act methods
        this.uiStage.act(dt);
        this.mainStage.act(dt);

        // defined by user
        this.update(dt);

        // render
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT + GL20.GL_DEPTH_BUFFER_BIT);

        // draw the graphics
        this.mainStage.draw();
        this.uiStage.draw();
    }

    // methods required by Screen interface
    public void resize(int width, int height) {
        this.uiStage.getViewport().update(width, height, true);
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
        this.mainStage.dispose();
        this.uiStage.dispose();
    }

    /**
     * Called when this becomes the active screen in a Game.
     * Set up InputMultiplexer here, in case screen is reactivated at a later time.
     */
    public void show() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.addProcessor(this);
        im.addProcessor(this.uiStage);
    }

    /**
     * Called when this is no longer the active screen in a Game.
     * Screen class and Stages no longer process input.
     * Other InputProcessors must be removed manually.
     */
    public void hide() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(this.uiStage);
    }

    public void updateGameScreen(GameScreen gameScreen) {
    }

    public void removeRemainingFromInventorySpace(boolean verbose) {
        BaseActor inPlace1 = (BaseActor) uiStage.hit(620, 20, true), inPlace2 = (BaseActor) uiStage.hit(620, 80, true);
        while (!Objects.equals(inPlace1.toString(), "Inventory")) {
            if (verbose) System.out.println(inPlace1);
            inPlace1.remove();
            inPlace1 = (BaseActor) uiStage.hit(620, 20, true);
        }
        while (!Objects.equals(inPlace2.toString(), "Inventory")) {
            if (verbose) System.out.println(inPlace2);
            inPlace2.remove();
            inPlace2 = (BaseActor) uiStage.hit(620, 80, true);
        }
    }

    // methods required by InputProcessor interface
    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}