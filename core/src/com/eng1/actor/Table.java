package com.eng1.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;

public class Table extends BaseActor {

    public Table(Stage s) {
        super(0, 0, s);

        Pixmap pixmap = new Pixmap(1280, 300, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 1, 0, 1));
        pixmap.fillRectangle(0, 150, 1280, 150);
        pixmap.fillRectangle(150, 0, 980, 150);
        pixmap.fillTriangle(0, 150, 150, 0, 150, 150);
        pixmap.fillTriangle(1280, 150, 1130, 0, 1130, 150);
        pixmap.setColor(new Color(1, 0, 0, 1));
        pixmap.fillRectangle(300, 50, 100, 100);
        pixmap.fillRectangle(590, 50, 100, 100);
        pixmap.fillRectangle(880, 50, 100, 100);
        this.setTexture(new Texture(pixmap));
        pixmap.dispose();
    }
}
