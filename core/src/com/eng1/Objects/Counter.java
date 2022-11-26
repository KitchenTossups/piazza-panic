package com.eng1.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Counter extends BaseActor {

    private final Rectangle bounds;

    public Counter(float x, float y, int width, int height, Stage s) {
        super(x, y, s);

        this.bounds = new Rectangle(x, y, width, height);

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 1, 1));
        pixmap.fillRectangle(0, 0, width, height);
        setTexture(new Texture(pixmap));
        pixmap.dispose();
    }

    public Rectangle getBounds() {
        this.bounds.set(getX(), getY(), getWidth(), getHeight());
        return this.bounds;
    }
}
