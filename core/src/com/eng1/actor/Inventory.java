package com.eng1.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;

public class Inventory extends BaseActor {
    public Inventory(Stage s) {
        super(590, 0, s);
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 1, 0, 1));
        pixmap.fillRectangle(0, 0, 100, 100);
        this.setTexture(new Texture(pixmap));
        pixmap.dispose();
    }
}
