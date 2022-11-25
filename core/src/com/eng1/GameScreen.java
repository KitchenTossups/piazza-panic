package com.eng1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final PiazzaPanic game;

    Texture wallTextureHorizontal, wallTextureVertical, counterTexture;
    OrthographicCamera camera;
    Rectangle wallTop, wallLeft, wallBottom, wallRight, counter1, counter2, serverCounter, burgerStation, saladStation, recipeStation, chef1, chef2, chef3;
    Array<Rectangle> customers;
    int width, height;

    public GameScreen(PiazzaPanic game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);

        this.wallTop = new Rectangle();
        this.wallTop.x = 0;
        this.wallTop.y = height - 40;

        this.wallTop.width = width / 5f * 4f;
        this.wallTop.height = 40;

        this.wallBottom = new Rectangle();
        this.wallBottom.x = 0;
        this.wallBottom.y = 0;

        this.wallBottom.width = width / 5f * 4f;
        this.wallBottom.height = 40;

        this.wallLeft = new Rectangle();
        this.wallLeft.x = 0;
        this.wallLeft.y = 40;

        this.wallLeft.width = 40;
        this.wallLeft.height = height - (2 * 40);

        this.wallRight = new Rectangle();
        this.wallRight.x = width / 5f * 4f - 40;
        this.wallRight.y = 40;

        this.wallRight.width = 40;
        this.wallRight.height = height - (2 * 40);

        this.counter1 = new Rectangle();
        this.counter1.x = width / 5f;
        this.counter1.y = height / 3f - 40;

        this.counter1.width = width / 5f * 2f;
        this.counter1.height = 80;

        this.counter2 = new Rectangle();
        this.counter2.x = width / 5f;
        this.counter2.y = height / 3f * 2f - 40;

        this.counter2.width = width / 5f * 2f;
        this.counter2.height = 80;

        Pixmap pixmapHorizontal = new Pixmap(width / 5 * 4, 40, Pixmap.Format.RGBA8888);
        pixmapHorizontal.setColor(new Color(0, 0, 1, 1));
        pixmapHorizontal.fillRectangle(0, 0, width / 5 * 4, 40);
        this.wallTextureHorizontal = new Texture(pixmapHorizontal);
        pixmapHorizontal.dispose();

        Pixmap pixmapVertical = new Pixmap(40, height, Pixmap.Format.RGBA8888);
        pixmapVertical.setColor(new Color(0, 0, 1, 1));
        pixmapVertical.fillRectangle(0, 0, 40, height);
        this.wallTextureVertical = new Texture(pixmapVertical);
        pixmapVertical.dispose();

        Pixmap pixmapCounter = new Pixmap(width / 5 * 3, 80, Pixmap.Format.RGBA8888);
        pixmapCounter.setColor(new Color(0, 0, 1, 1));
        pixmapCounter.fillRectangle(0, 0, width / 5 * 2, 80);
        this.counterTexture = new Texture(pixmapCounter);
        pixmapCounter.dispose();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SacredCzars-mLE0G.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        Gdx.input.setInputProcessor(this.game.stage);
        /*Skin skin = new Skin();

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        skin.add("button", textButtonStyle);
        skin.add("default", windowStyle);*/
        /*this.dialog = new Dialog("End Game", skin)
        {
            protected void result(Object object)
            {
                System.out.println("Exit: " + object);
                Timer.schedule(new Timer.Task()
                {

                    @Override
                    public void run()
                    {
                        GameScreen.this.dialog.show(GameScreen.this.game.stage);
                    }
                }, 1);
            }
        };

        this.dialog.button("Exit", true, textButtonStyle);
        this.dialog.button("Cancel", false, textButtonStyle);
        this.dialog.key(Input.Keys.ENTER, true);

        Timer.schedule(new Timer.Task()
        {

            @Override
            public void run()
            {
                GameScreen.this.dialog.show(GameScreen.this.game.stage);
            }
        }, 1);*/

//        OrthographicCamera cameraGamePlay = new OrthographicCamera(160, 144);
//        screenViewport = new ScreenViewport(cameraGamePlay);
//        screenViewport.setScreenBounds(100, 100, 160, 144);


//        .getViewport().apply(); //readjusts the draw bounds for the UI stage to the set resolution for that stage's viewport's camera
//        uiStage.act();
//        uiStage.draw();

//        screenViewport.apply(); //readjusts the draw bounds for the gameArea
// gameArea renders (or game stage's act/draw methods if you decide to use a stage)
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        this.camera.update();
        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.batch.begin();
        this.game.batch.draw(this.wallTextureHorizontal, this.wallTop.x, this.wallTop.y);
        this.game.batch.draw(this.wallTextureHorizontal, this.wallBottom.x, this.wallBottom.y);
        this.game.batch.draw(this.wallTextureVertical, this.wallLeft.x, this.wallLeft.y);
        this.game.batch.draw(this.wallTextureVertical, this.wallRight.x, this.wallRight.y);
        this.game.batch.draw(this.counterTexture, this.counter1.x, this.counter1.y);
        this.game.batch.draw(this.counterTexture, this.counter2.x, this.counter2.y);
//        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.stage.act();
        this.game.stage.draw();
//        game.batch.draw(screenViewport, 100, 100);
        this.game.batch.end();
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
