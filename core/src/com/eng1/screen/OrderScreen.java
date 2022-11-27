package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.eng1.PiazzaPanic;
import com.eng1.actor.Customer;
import com.eng1.base.BaseActor;
import com.eng1.base.BaseScreen;

import java.util.Date;

public class OrderScreen extends BaseScreen {

    private final GameScreen gameScreen;
    private final PiazzaPanic game;
    private final Label label;
    private final Customer customer;

    public OrderScreen(Customer customer, Label.LabelStyle[] styles, GameScreen gameScreen, PiazzaPanic game, float width, float height) {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "assets/background.png" );
        background.setSize(width, height);
        Label titleLabel = new Label(customer.getOrder().endProductName(), styles[0]);
        this.label = new Label(null, styles[1]);
        this.label.setAlignment(Align.center);
        this.uiTable.pad(50);
        this.uiTable.add(titleLabel).expand();
        this.uiTable.row();
        this.uiTable.add(this.label).expand();
        this.gameScreen = gameScreen;
        this.game = game;
        this.customer = customer;
    }

    @Override
    public void update(float dt) {
        this.label.setText(String.format(String.format("Order placed %d seconds ago\n\n%s\n\nClick anywhere to return", (new Date().getTime() - this.customer.getOrderPlaced()) / 1000, this.customer.getOrder().getIngredients())));
        if (Gdx.input.isTouched()) {
            this.game.setActiveScreen(this.gameScreen);
            this.dispose();
        }
    }
}