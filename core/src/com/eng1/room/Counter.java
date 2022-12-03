package com.eng1.room;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;

public class Counter extends BaseActor {

    public Counter(float x, float y, int width, int height, Stage s) {
        super(x, y, s);

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 1, 1));
        pixmap.fillRectangle(0, 0, width, height);
        this.setTexture(new Texture(pixmap));
        pixmap.dispose();
    }
}
