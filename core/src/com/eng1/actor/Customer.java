package com.eng1.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.eng1.non_actor.Recipe;
import com.eng1.base.BaseActor;

import java.util.Date;

public class Customer extends BaseActor {

    private final Recipe order;

    private final long orderPlaced, customerNumber;

    public Customer(float x, float y, Stage s, Recipe order, long customerNumber, long delay) {
        super(x, y, s);
        this.order = order;

        this.orderPlaced = new Date().getTime() + 11000 + delay;

        this.setTouchable(Touchable.enabled);

        this.customerNumber = customerNumber;

        this.loadTexture("images/person.jpg", 32, 32);
    }

    public Recipe getOrder() {
        return this.order;
    }

    public long getOrderPlaced() {
        return this.orderPlaced;
    }

    public long getCustomerNumber() {
        return customerNumber;
    }
}
