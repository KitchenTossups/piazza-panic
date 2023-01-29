package com.eng1.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.non_actor.*;
import com.eng1.base.BaseActor;

public class Chef extends BaseActor {

    private Object inventoryItem;

    public Chef(float x, float y, Stage s, int chefNumber) {
        super(x, y, s);

        this.loadTexture("images/ChefB" + chefNumber + ".png");
    }

    public Object getInventoryItem() {
        return this.inventoryItem;
    }

    public void setInventoryItem(Object inventoryItem) {
        if (inventoryItem instanceof Food || inventoryItem instanceof Ingredient || inventoryItem == null)
            this.inventoryItem = inventoryItem;
    }
}
