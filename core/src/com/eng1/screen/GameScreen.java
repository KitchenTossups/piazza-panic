package com.eng1.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.*;
import com.eng1.*;
import com.eng1.actor.*;
import com.eng1.base.*;
import com.eng1.enums.*;
import com.eng1.non_actor.*;
import com.eng1.room.*;

import java.util.*;

@SuppressWarnings("unused")
public class GameScreen extends BaseScreen {

    final PiazzaPanic game;
    final Mode mode;
    final Difficulty difficulty;
    final OrthographicCamera camera;
    final List<Station> stations;
    final List<Counter> counters;
    final Chef[] chefs;
    final List<Customer> customers;
    final List<IngredientActor> ingredientActors;
    final Label messageLabel;
    final CustomerCommunicator customerCommunicator;
    private boolean tabPressed;
    final int width, height;
    private int chefSelector = 0, binnedItems = 0, customerNumber = 1;
    private long messageTimer = -1;
    private final float loci;

    public GameScreen(PiazzaPanic game, int width, int height, Mode mode, float loci, Difficulty difficulty) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.messageLabel = new Label(null, this.game.labelStyle[1]);
        this.messageLabel.setAlignment(Align.center);
        this.tabPressed = false;
        this.loci = loci;
        this.difficulty = difficulty;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);

        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/BackgroundMusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        BaseActor background = new BaseActor(0, 0, this.mainStage), customerFloor = new BaseActor(1025, 0, this.mainStage);
        background.loadTexture("background/Floor1.png");
        background.setSize(this.width, this.height);
        customerFloor.loadTexture("background/CustomerFloor2.png");
        customerFloor.setSize(256, this.height);

        this.counters = new ArrayList<>();

        counters();

        this.customers = new ArrayList<>();

        this.customerCommunicator = new CustomerCommunicator();

        addCustomer(new Recipe(Product.CHEESEBURGER), 0);
        addCustomer(new Recipe(Product.SALAD), 6000);
        addCustomer(new Recipe(Product.CHEESEBURGER), 12000);
        addCustomer(new Recipe(Product.BURGER), 18000);
        addCustomer(new Recipe(Product.BURGER), 24000);

        if (mode == Mode.ASSESSMENT_1)
            this.chefs = new Chef[2];
        else
            this.chefs = new Chef[3];

        this.chefs[0] = new Chef(90, 90, this.mainStage, 1);
        this.chefs[1] = new Chef(90, 130, this.mainStage, 2);
        if (mode == Mode.ASSESSMENT_2) this.chefs[2] = new Chef(90, 170, this.mainStage, 3);

        this.ingredientActors = new ArrayList<>();

//        this.ingredientActors.add(new IngredientActor(200f, 300f, this.mainStage, new Ingredient(Item.TEST_4, IngredientState.UNCOOKED)));
//        this.ingredientActors.add(new IngredientActor(200f, 350f, this.mainStage, new Ingredient(Item.TEST_5, IngredientState.UNCOOKED)));

        Label oldestOrder = new Label("Click here for the oldest order!", this.game.labelStyle[1]);
        this.uiTable.pad(10);
        this.uiTable.add(oldestOrder).expandX().align(Align.topRight);
        this.uiTable.row();
        this.uiTable.add(this.messageLabel).expand().align(Align.center);

        oldestOrder.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if (GameScreen.this.customers.get(0).getOrderPlaced() <= new Date().getTime())
                        GameScreen.this.game.setActiveScreen(new OrderScreen(GameScreen.this.customers.get(0), GameScreen.this.game.labelStyle, GameScreen.this, GameScreen.this.game));
                } catch (Exception ignored) {

                }
            }
        });

        this.stations = new ArrayList<>();

        stations();

//        new IngredientActor(100, 200, uiStage, new Ingredient(Item.BOTTOM_BUN, IngredientState.NOT_APPLICABLE));
//        p = new IngredientActor(100, 215, uiStage, new Ingredient(Item.PATTY, IngredientState.UNCOOKED));
//        new IngredientActor(100, 230, uiStage, new Ingredient(Item.CHEESE, IngredientState.NOT_APPLICABLE));
//        new IngredientActor(100, 245, uiStage, new Ingredient(Item.TOP_BUN, IngredientState.NOT_APPLICABLE));
//        p.makeReady();
//        p.makeReady();

    }

//    IngredientActor p;

    private void addCustomer(Recipe recipe, long delay) {
        Customer customer = new Customer(1300, 100, this.uiStage, recipe, customerNumber++, delay);
        this.customers.add(customer);
        CustomerMove customerMove = new CustomerMove(customer, this.customerCommunicator, delay);
        customerMove.start();
    }

    private static class CustomerCommunicator {
        private long packet;

        // True if receiver should wait
        // False if sender should wait
        private boolean transfer = true;

        public synchronized long receive() {
//            System.out.println("RECEIVE START");
            while (transfer) {
//                System.out.println("RECEIVE WAIT");
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread Interrupted");
                }
            }
            transfer = true;

            long returnPacket = packet;
//            System.out.println("RECEIVED");
            notifyAll();
//            System.out.println("RECEIVE NOTIFY");
            return returnPacket;
        }

        public synchronized void send(long packet) {
//            System.out.println("START SEND");
            while (!transfer) {
//                System.out.println("WAIT SEND");
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread Interrupted");
                }
            }
            transfer = false;

            this.packet = packet;
//            System.out.println("SENT");
            notifyAll();
//            System.out.println("SEND NOTIFY");
        }
    }

    private class CustomerMove implements Runnable {
        private Thread t;
        private final Customer customer;
        private final CustomerCommunicator customerCommunicator;
        private final long delay;

        CustomerMove(Customer customer, CustomerCommunicator customerCommunicator, long delay) {
            this.customer = customer;
            this.customerCommunicator = customerCommunicator;
            this.delay = delay;
//            System.out.println("Creating " + this.customer.getOrderPlaced());
        }

        public void run() {
//            System.out.println("Running " + this.customer.getOrderPlaced());
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MoveToAction action = new MoveToAction(), action1 = new MoveToAction(), action2 = new MoveToAction();
            action.setX(1150);
            action.setY(100);
            action.setDuration(3);
            this.customer.addAction(action);
            try {
                Thread.sleep(6000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.customer.getCustomerNumber() / 13f > 0)
                action2.setX(1150 + (40 * (int) (this.customer.getCustomerNumber() / 13f)));
            else action2.setX(1150);
            action2.setY(600 - (40 * ((this.customer.getCustomerNumber() - 1) % 12)));
            action2.setDuration(3);
            this.customer.addAction(action2);
            this.customer.addListener(new InputListener() {

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
            try {
//                System.out.println("Waiting " + this.customer.getOrderPlaced());
                long packet = this.customerCommunicator.receive();
                while (packet != this.customer.getOrderPlaced()) {
                    packet = this.customerCommunicator.receive();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("After waiting " + this.customer.getOrderPlaced());

            for (Action action3 : this.customer.getActions())
                this.customer.removeAction(action3);

            action1.setX(1300);
            action1.setY(550);
            action1.setDuration(2);
            this.customer.addAction(action1);
//            System.out.println("Finished " + this.customer.getOrderPlaced());
        }

        public void start() {
//            System.out.println("Starting " + this.customer.getOrderPlaced());
            if (t == null) {
                t = new Thread(this, String.valueOf(this.customer.getOrderPlaced()));
                t.start();
            }
        }
    }

    public void update(float dt) {
        this.timerCheck();
//        if (this.customers.get(0).getX() == 1150 && this.customers.get(0).getY() == 100)
//            this.movementTimer++;
//        if (this.movementTimer == 5 * 60) {
//            this.movementTimer = 0;
//            MoveToAction action = new MoveToAction();
//            action.setX(1150);
//            action.setY(550);
//            action.setDuration(3);
//            this.customers.get(0).addAction(action);
//        }
        try {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() + 300 * Gdx.graphics.getDeltaTime());
                this.checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                float oldY = this.chefs[this.chefSelector].getY();
                this.chefs[this.chefSelector].setY(this.chefs[this.chefSelector].getY() - 300 * Gdx.graphics.getDeltaTime());
                this.checkCollision(this.chefs[this.chefSelector].getX(), oldY);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() + 300 * Gdx.graphics.getDeltaTime());
                this.checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                float oldX = this.chefs[this.chefSelector].getX();
                this.chefs[this.chefSelector].setX(this.chefs[this.chefSelector].getX() - 300 * Gdx.graphics.getDeltaTime());
                this.checkCollision(oldX, this.chefs[this.chefSelector].getY());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.TAB) && !Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                if (!this.tabPressed) {
                    switch (this.mode) {
                        case ASSESSMENT_1:
                            this.chefSelector++;
                            if (this.chefSelector == 2) this.chefSelector = 0;
                            break;
                        case ASSESSMENT_2:
                            this.chefSelector++;
                            if (this.chefSelector == 3) this.chefSelector = 0;
                            break;
                    }
                }
                this.tabPressed = true;
            } else
                this.tabPressed = false;
            if (Gdx.input.isKeyPressed(Input.Keys.E))
                this.stationProximity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCollision(float oldX, float oldY) {
        if (this.chefs[this.chefSelector].getX() + this.chefs[this.chefSelector].getWidth() > this.width || this.chefs[this.chefSelector].getX() < 0)
            this.chefs[this.chefSelector].setX(oldX);
        if (this.chefs[this.chefSelector].getY() + this.chefs[this.chefSelector].getHeight() > this.height || this.chefs[this.chefSelector].getY() < 0)
            this.chefs[this.chefSelector].setY(oldY);
        for (Counter counter : this.counters)
            if (counter.getBoundaryRectangle().overlaps(this.chefs[this.chefSelector].getBoundaryRectangle())) {
                this.chefs[this.chefSelector].setX(oldX);
                this.chefs[this.chefSelector].setY(oldY);
            }
        switch (this.chefSelector) {
            case 0:
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[2].getBoundaryRectangle())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[1].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                break;
            case 1:
                if (this.mode == Mode.ASSESSMENT_2)
                    if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[2].getBoundaryRectangle())) {
                        this.chefs[this.chefSelector].setX(oldX);
                        this.chefs[this.chefSelector].setY(oldY);
                    }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[0].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                break;
            case 2:
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[0].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                if (this.chefs[this.chefSelector].getBoundaryRectangle().overlaps(this.chefs[1].getBoundaryRectangle())) {
                    this.chefs[this.chefSelector].setX(oldX);
                    this.chefs[this.chefSelector].setY(oldY);
                }
                break;
        }
    }

    private void stationProximity() {
        for (Station station : this.stations) {
            if (station.getLociRectangle().overlaps(this.chefs[this.chefSelector].getBoundaryRectangle())) {
                BaseScreen screen;
                switch (station.getStationType()) {
                    case BIN:
                        if (this.chefs[this.chefSelector].getInventoryItem() == null) {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                            this.messageLabel.setText("This chef has nothing in their inventory!\nYou can't bin emptiness!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        } else {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/OpenStation.mp3")).play();
                            this.game.setActiveScreen(new BinScreen(this, this.game));
                        }
                        break;
                    case CHOPPING:
                        if (this.chefs[this.chefSelector].getInventoryItem() == null) {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                            this.messageLabel.setText("This chef has nothing in their inventory!\nYou aren't allowed to chop yourself!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        } else if (this.chefs[this.chefSelector].getInventoryItem() instanceof Ingredient) {
                            if (((Ingredient) this.chefs[this.chefSelector].getInventoryItem()).getState() == IngredientState.UNCUT) {
                                Gdx.audio.newSound(Gdx.files.internal("sounds/OpenStation.mp3")).play();
                                this.game.setActiveScreen(new ChoppingScreen(this, this.game));
                            } else if (((Ingredient) this.chefs[this.chefSelector].getInventoryItem()).getState() == IngredientState.CUT) {
                                Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                                this.messageLabel.setText("This chef has cut ingredients in their inventory!\nYou can't cut an item twice!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            } else {
                                Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                                this.messageLabel.setText("This chef has nothing in their inventory that can be cut!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            }
                        } else {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                            this.messageLabel.setText("This chef has no ingredient in their inventory!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        }
                        break;
                    case COUNTER:
                        screen = station.getScreen();
                        screen.updateGameScreen(this);
                        Gdx.audio.newSound(Gdx.files.internal("sounds/OpenStation.mp3")).play();
                        this.game.setActiveScreen(screen);
                        break;
                    case FOOD_CHEST:
                        if (this.chefs[this.chefSelector].getInventoryItem() != null) {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                            this.messageLabel.setText("This chef has something in their inventory!\nYou have no inventory space available!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        } else {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/OpenStation.mp3")).play();
                            this.game.setActiveScreen(new FoodChestScreen(station.getFoodChestType(), this, this.game));
                        }
                        break;
                    case GRILL:
                        screen = station.getScreen();
                        screen.updateGameScreen(this);
                        Gdx.audio.newSound(Gdx.files.internal("sounds/OpenStation.mp3")).play();
                        this.game.setActiveScreen(screen);
                    case PREP:
                        screen = station.getScreen();
                        screen.updateGameScreen(this);
                        Gdx.audio.newSound(Gdx.files.internal("sounds/OpenStation.mp3")).play();
                        this.game.setActiveScreen(screen);
                        break;
                    case SERVING:
                        if (this.chefs[this.chefSelector].getInventoryItem() instanceof FoodActor)
                            if (((FoodActor) (this.chefs[this.chefSelector].getInventoryItem())).getFood().ready()) {
                                System.out.println("SERVED!");
                                Gdx.audio.newSound(Gdx.files.internal("sounds/BellSound.mp3")).play();
                                this.messageLabel.setText(String.format("Dinner is served!\nYou have served %s!", ((FoodActor) this.chefs[this.chefSelector].getInventoryItem()).getFood().getRecipe().getEndProduct()));
                                this.messageTimer = new Date().getTime() + 2500L;
                            } else {
                                Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                                this.messageLabel.setText("This chef has nothing able to be served in their inventory!");
                                this.messageTimer = new Date().getTime() + 5000L;
                            }
                        else {
                            Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                            this.messageLabel.setText("This chef has nothing able to be served in their inventory!");
                            this.messageTimer = new Date().getTime() + 5000L;
                        }
                        break;
                    default:
                        Gdx.audio.newSound(Gdx.files.internal("sounds/HonkSound.mp3")).play();
                        System.out.println("Invalid station type: " + station.getStationType());
                        break;
                }
            }
        }
    }

    /**
     * This checks if a message has been on the screen for a set amount of time and removes it
     */
    private void timerCheck() {
        if (this.messageTimer != -1)
            if (new Date().getTime() >= this.messageTimer) {
                this.messageTimer = -1;
                this.messageLabel.setText(null);
            }
    }

    private void counters() {
        this.counters.add(new Counter(0f, height - 80f, (int) (width / 5f * 4f), 80, this.mainStage)); // Top
        this.counters.add(new Counter(0f, 0f, (int) (width / 5f * 4f), 80, this.mainStage));   // Bottom
        this.counters.add(new Counter(0f, 0f, 80, height, this.mainStage));    // Left
        this.counters.add(new Counter((width / 5f * 4f), 0f, 80, height, this.mainStage));    // Right
//        this.counters.add(new Counter(width / 5f + 40f, (height / 3f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage));  // Counter 1
//        this.counters.add(new Counter(width / 5f + 40f, (height / 3f * 2f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage)); // Counter 2
        this.counters.add(new Counter(width / 5f + 40f, (height / 2f) - 40f, (int) (width / 5f * 2f), 80, this.mainStage));
    }

    private void stations() {
        this.stations.add(new Station(this.width / 5f + 40f, this.height - 80f, 80, 80, this.loci, StationType.PREP, this, this.game));
        this.stations.add(new Station((this.width / 5f + 40f) + 136f + 80f, this.height - 80f, 80, 80, this.loci, StationType.PREP, this, this.game));
        this.stations.add(new Station((this.width / 5f + 40f) + 2f * (136f + 80f), this.height - 80f, 80, 80, this.loci, StationType.PREP, this, this.game));
        this.stations.add(new Station(0, 0, 80, 80, this.loci, StationType.BIN, this.mainStage));
        this.stations.add(new Station(this.width / 5f + 40f, 0, 80, 80, this.loci, FoodChestType.DRY_FOOD, this.mainStage));
        this.stations.add(new Station((this.width / 5f + 40f) + 136f + 80f, 0, 80, 80, this.loci, FoodChestType.FRESH_FOOD, this.mainStage));
        this.stations.add(new Station((this.width / 5f + 40f) + 2f * (136f + 80f), 0, 80, 80, this.loci, FoodChestType.FROZEN_FOOD, this.mainStage));
        this.stations.add(new Station(0, this.height / 2f - 40f, 80, 80, this.loci, StationType.CHOPPING, this.mainStage));
//        this.stations.add(new Station(this.width / 5f + 40f, (this.height / 3f * 2f) - 40f, 80, 80, this.loci, StationType.COUNTER, this, this.game));
//        this.stations.add(new Station((this.width / 5f + 40f) + 136f + 80f, (this.height / 3f * 2f) - 40f, 80, 80, this.loci, StationType.COUNTER, this, this.game));
//        this.stations.add(new Station((this.width / 5f + 40f) + 2f * (136f + 80f), (this.height / 3f * 2f) - 40f, 80, 80, this.loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station(this.width / 5f + 40f, (this.height / 2f) - 40f, 80, 80, this.loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station((this.width / 5f + 40f) + 136f + 80f, (this.height / 2f) - 40f, 80, 80, this.loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station((this.width / 5f + 40f) + 2f * (136f + 80f), (this.height / 2f) - 40f, 80, 80, this.loci, StationType.COUNTER, this, this.game));
        this.stations.add(new Station((this.width / 5f * 4f), (this.height / 3f) - 40f, 80, 280, this.loci, StationType.SERVING, this.mainStage));
        this.stations.add(new Station(0, this.height - 80f, 80, 80, this.loci, StationType.GRILL, this, this.game));
    }

    /**
     * Get the selected chef number
     *
     * @return int chefSelector
     */
    public int getChefSelector() {
        return this.chefSelector;
    }

    /**
     * Get the number of binned items
     *
     * @return int binnedItems
     */
    public int getBinnedItems() {
        return this.binnedItems;
    }

    /**
     * Increase the number of binned items
     */
    public void increaseBinnedItems() {
        this.binnedItems++;
    }

    /**
     * Get the main stage
     *
     * @return Stage mainStage
     */
    public Stage getMainStage() {
        return this.mainStage;
    }
}
