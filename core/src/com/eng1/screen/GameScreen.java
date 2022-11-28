package com.eng1.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.non_actor.*;
import com.eng1.room.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameScreen extends BaseScreen {

    final PiazzaPanic game;
    final Mode mode;
    final OrthographicCamera camera;
    final Array<Station> stations; // servingCounter, burgerStation, saladStation, recipeStation;
    final Array<Counter> counters;
    final Chef[] chefs;
    final Array<Customer> customers;
    final Array<IngredientActor> ingredientActors;
    private boolean tabPressed;
    final int width, height;
    private int chefSelector;

    public GameScreen(PiazzaPanic game, int width, int height, Mode mode, float loci) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.tabPressed = false;
        this.chefSelector = 0;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);

        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("assets/background.png");
        background.setSize(this.width, this.height);

        this.counters = new Array<>();

        this.counters.add(new Counter(0f, height - 80f, (int) (width / 5f * 4f), 80, this.mainStage)); // Top
        this.counters.add(new Counter(0f, 0f, (int) (width / 5f * 4f), 80, this.mainStage));   // Bottom
        this.counters.add(new Counter(0f, 0f, 80, height, this.mainStage));    // Left
        this.counters.add(new Counter((width / 5f * 4f), 0f, 80, height, this.mainStage));    // Right
        this.counters.add(new Counter(width / 5f + 40f, (height / 3f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage));  // Counter 1
        this.counters.add(new Counter(width / 5f + 40f, (height / 3f * 2f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage)); // Counter 2

        this.stations = new Array<>();

        this.stations.add(new Station(0, 200f, 80, 80, loci, this.mainStage, StationType.PREP));
        this.stations.add(new Station(0, 400f, 80, 80, loci, this.mainStage, StationType.PREP));
        this.stations.add(new Station(0, 0, 80, 80, loci, this.mainStage, StationType.BIN));
        this.stations.add(new Station(0, 640f, 80, 80, loci, this.mainStage, StationType.FOOD_CHEST));
        this.stations.add(new Station(200f, 0, 80, 80, loci, this.mainStage, StationType.CHOPPING));
        this.stations.add(new Station(width / 5f + 40f, (height / 3f * 2f) - 40f, 80, 80, loci, this.mainStage, StationType.COUNTER));
        this.stations.add(new Station((width / 5f * 4f), (height / 3f) - 40f, 80, 280, loci, this.mainStage, StationType.SERVING));

        this.customers = new Array<>();

        List<Ingredient> temp = new ArrayList<>();

        temp.add(new Ingredient("TestI1"));
        temp.add(new Ingredient("TestI2"));
        temp.add(new Ingredient("TestI3"));
        temp.add(new Ingredient("TestI4"));

        this.customers.add(new Customer(1300, 100, this.uiStage, new Recipe("TestR", temp)));

        for (Customer customer : this.customers) {
            MoveToAction action = new MoveToAction();
            action.setX(1150);
            action.setY(100);
            action.setDuration(3);
            customer.addAction(action);
            customer.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        if (customer.getOrderPlaced() <= new Date().getTime())
                            GameScreen.this.game.setActiveScreen(new OrderScreen(customer, GameScreen.this.game.labelStyle, GameScreen.this, GameScreen.this.game));
                    } catch (Exception ignored) {

                    }
                }
            });
        }

        if (mode == Mode.ASSESSMENT_1)
            this.chefs = new Chef[2];
        else
            this.chefs = new Chef[3];

        this.chefs[0] = new Chef(90, 90, this.mainStage, 1);
        this.chefs[1] = new Chef(90, 130, this.mainStage, 2);
        if (mode == Mode.ASSESSMENT_2) this.chefs[2] = new Chef(60, 140, this.mainStage, 3);

        this.ingredientActors = new Array<>();

        this.ingredientActors.add(new IngredientActor(200f, 300f, this.mainStage, new Ingredient("0")));
        this.ingredientActors.add(new IngredientActor(200f, 350f, this.mainStage, new Ingredient("1")));

        Label oldestOrder = new Label("Click here for the oldest order!", this.game.labelStyle[1]);
        this.uiTable.pad(10);
        this.uiTable.add(oldestOrder).expand().align(Align.topRight);

        oldestOrder.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setActiveScreen(new OrderScreen(GameScreen.this.customers.get(0), GameScreen.this.game.labelStyle, GameScreen.this, GameScreen.this.game, GameScreen.this.width, GameScreen.this.height));
            }
        });
    }

    private long movementTimer = 0;

    public void update(float dt) {
        if (this.customers.get(0).getX() == 1150 && this.customers.get(0).getY() == 100)
            this.movementTimer++;
        if (this.movementTimer == 5 * 60) {
            MoveToAction action = new MoveToAction();
            action.setX(1150);
            action.setY(550);
            action.setDuration(3);
            this.customers.get(0).addAction(action);
        }
        try {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() + 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() - 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() + 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() - 200 * Gdx.graphics.getDeltaTime());
                this.checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.TAB) && !Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                if (!this.tabPressed)
                    switch (this.mode) {
                        case ASSESSMENT_1 -> {
                            this.chefSelector++;
                            if (this.chefSelector == 2) this.chefSelector = 0;
                        }
                        case ASSESSMENT_2 -> {
                            this.chefSelector++;
                            if (this.chefSelector == 3) this.chefSelector = 0;
                        }
                    }
                this.tabPressed = true;
            } else
                this.tabPressed = false;
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                this.stationProximity();
//                this.game.setActiveScreen(new MainMenuScreen(new PiazzaPanic(this.width, this.height, this.mode), this.width, this.height, this.mode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCollision(float oldX, float oldY) {
        for (Counter counter : this.counters)
            if (counter.getBoundaryRectangle().overlaps(this.chefs[this.chefSelector].getBoundaryRectangle())) {
                this.chefs[this.chefSelector].setX(oldX);
                this.chefs[this.chefSelector].setY(oldY);
            }
        switch (this.chefSelector) {
            case 0 -> {
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[2].getBoundaryRectangle())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[1].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
            }
            case 1 -> {
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[2].getBoundaryRectangle())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[0].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
            }
            case 2 -> {
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[0].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[1].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
            }
        }
    }

    private void stationProximity() {
        for (Station station : this.stations) {
            if (station.getLociRectangle().overlaps(this.chefs[this.chefSelector].getBoundaryRectangle()))
                System.out.println(station.getStationType().toString());
        }
    }
}
