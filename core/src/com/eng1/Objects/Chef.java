package com.eng1.Objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Chef extends BaseActor {

    private final Rectangle bounds;

    public Chef(float x, float y, Stage s, int chefNumber) {
        super(x, y, s);

        this.bounds = new Rectangle(x, y, 32, 32);

        loadTexture("chef" + chefNumber + ".png");
    }

    public Rectangle getBounds() {
        this.bounds.set(getX(), getY(), getWidth(), getHeight());
        return this.bounds;
    }
}
