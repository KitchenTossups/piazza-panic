package com.eng1.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Station extends BaseActor {

    private final Rectangle bounds;

    private final float distance;

    public Station(float x, float y, int width, int height, float distance, Stage s) {
        super(x, y, s);

        this.bounds = new Rectangle(x, y, width, height);

        this.distance = distance;

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 1, 0, 1));
        pixmap.fillRectangle(0, 0, width, height);
        setTexture(new Texture(pixmap));
        pixmap.dispose();
    }

    public Rectangle getBounds() {
        this.bounds.set(getX(), getY(), getWidth(), getHeight());
        return this.bounds;
    }

    public Rectangle getLoci() {
        this.bounds.set(getX() - this.distance, getY() - this.distance, getWidth() + (2 * this.distance), getHeight() + (2 * this.distance));
        return this.bounds;
    }
}
