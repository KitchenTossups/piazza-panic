package com.eng1.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;
import com.eng1.enums.TableSpaceType;

public class TableSpace extends BaseActor {
    public TableSpace(float x, float y, TableSpaceType tableSpaceType, Stage s) {
        super(x, y, s);
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 1, 0, 1));
        switch (tableSpaceType) {
            case FILLED:
                pixmap.fillRectangle(0, 0, 100, 100);
                break;
            case OUTLINE:
                pixmap.fillRectangle(0, 0, 100, 5);
            case OUTLINE_OPEN_TOP:
                pixmap.fillRectangle(0, 95, 100, 5);
                pixmap.fillRectangle(0, 0, 5, 100);
                pixmap.fillRectangle(95, 0, 5, 100);
                break;
        }
        this.setTexture(new Texture(pixmap));
        pixmap.dispose();
    }
}
