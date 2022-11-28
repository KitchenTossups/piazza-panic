package com.eng1.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;
import com.eng1.non_actor.Food;

public class FoodActor extends BaseActor {

    private final Food food;

    public FoodActor(float x, float y, Stage s, Food f) {
        super(x, y, s);

        this.food = f;
    }

    public Food getFood() {
        return this.food;
    }
}
