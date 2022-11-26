package com.eng1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.eng1.Objects.*;

public class GameScreen implements Screen {

    final PiazzaPanic game;

    final Mode mode;

//    Texture wallTextureHorizontal, wallTextureVertical, counterTexture, chef1Texture, chef2Texture, chef3Texture;
    OrthographicCamera camera;
    Rectangle serverCounter, burgerStation, saladStation, recipeStation;
    Array<Counter> counters;
    Chef[] chefs;
    Array<Rectangle> customers;

    private boolean tabPressed;
    int width, height, chefSelector;

    public GameScreen(PiazzaPanic game, int width, int height, Mode mode) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.tabPressed = false;
        this.chefSelector = 0;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);

        this.counters = new Array<>();

        this.counters.add(new Counter(0f, height - 40f, (int) (width / 5f * 4f), 40, this.game.stage)); // Top
        this.counters.add(new Counter(0f, 0f, (int) (width / 5f * 4f), 40, this.game.stage));   // Bottom
        this.counters.add(new Counter(0f, 0f, 40, height, this.game.stage));    // Left
        this.counters.add(new Counter((width / 5f * 4f), 0f, 40, height, this.game.stage));    // Right
        this.counters.add(new Counter(width / 5f, (height / 3f) - 40f, (int) (width / 5f * 2f), 80, this.game.stage));  // Counter 1
        this.counters.add(new Counter(width / 5f, (height / 3f * 2f) - 40f, (int) (width / 5f * 2f), 80, this.game.stage)); // Counter 2

        this.chefs = new Chef[2];
        if (mode == Mode.ASSESSMENT_2) this.chefs = new Chef[3];

        this.chefs[0] = new Chef(60, 60, this.game.stage, 1);
        this.chefs[1] = new Chef(60, 100, this.game.stage, 2);
        if (mode == Mode.ASSESSMENT_2) this.chefs[2] = new Chef(60, 140, this.game.stage, 3);

//        this.chef1Texture = new Texture(Gdx.files.internal("chef1.png"));
//        this.chef2Texture = new Texture(Gdx.files.internal("chef2.png"));
//        this.chef3Texture = new Texture(Gdx.files.internal("chef3.png"));

        /*Pixmap pixmapHorizontal = new Pixmap(width / 5 * 4, 40, Pixmap.Format.RGBA8888);
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
        generator.dispose();*/

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
//        this.game.batch.draw(this.wallTextureHorizontal, this.wallTop.x, this.wallTop.y);
//        this.game.batch.draw(this.wallTextureHorizontal, this.wallBottom.x, this.wallBottom.y);
//        this.game.batch.draw(this.wallTextureVertical, this.wallLeft.x, this.wallLeft.y);
//        this.game.batch.draw(this.wallTextureVertical, this.wallRight.x, this.wallRight.y);
//        this.game.batch.draw(this.counterTexture, this.counter1.x, this.counter1.y);
//        this.game.batch.draw(this.counterTexture, this.counter2.x, this.counter2.y);
//        this.game.batch.draw(this.chef1Texture, this.chef1.x, this.chef1.y);
//        this.game.batch.draw(this.chef2Texture, this.chef2.x, this.chef2.y);
//        if (chefs == 3)
//            this.game.batch.draw(this.chef3Texture, this.chef3.x, this.chef3.y);
//        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.stage.act();
        this.game.stage.draw();
        this.game.batch.end();

        try {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() + 200 * Gdx.graphics.getDeltaTime());
                checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() - 200 * Gdx.graphics.getDeltaTime());
                checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() + 200 * Gdx.graphics.getDeltaTime());
                checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() - 200 * Gdx.graphics.getDeltaTime());
                checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.TAB) && !Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                if (!this.tabPressed)
                    switch (mode) {
                        case ASSESSMENT_1 -> {
                            this.chefSelector++;
                            if (this.chefSelector == 2) this.chefSelector = 0;
                        }
                        case ASSESSMENT_2 -> {
                            this.chefSelector++;
                            if (this.chefSelector == 3) this.chefSelector = 0;
                        }
                    }
                tabPressed = true;
            } else
                tabPressed = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCollision(float oldX, float oldY) {
        for (Counter counter : this.counters)
            if (counter.getBounds().overlaps(this.chefs[this.chefSelector].getBounds())) {
                this.chefs[this.chefSelector].setX(oldX);
                this.chefs[this.chefSelector].setY(oldY);
            }
        switch (this.chefSelector) {
            case 0 -> {
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBounds().overlaps(this.chefs[2].getBounds())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBounds().overlaps(this.chefs[1].getBounds())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
            }
            case 1 -> {
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBounds().overlaps(this.chefs[2].getBounds())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBounds().overlaps(this.chefs[0].getBounds())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
            }
            case 2 -> {
                if (this.chefs[this.chefSelector].getBounds().overlaps(this.chefs[0].getBounds())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                if (this.chefs[this.chefSelector].getBounds().overlaps(this.chefs[1].getBounds())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
            }
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
