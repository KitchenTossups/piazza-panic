package com.eng1.actor;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;

public class Inventory extends BaseActor {
    public Inventory(float x, float y, int w, int h, float a, Stage s) {
        super(x, y, s);
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 1, 0, a));
        pixmap.fillRectangle(0, 0, w, h);
        this.setTexture(new Texture(pixmap));
        pixmap.dispose();
    }
}
