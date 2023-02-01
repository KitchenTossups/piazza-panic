package com.eng1.room;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;

public class Counter extends BaseActor {

    public Counter(float x, float y, int width, int height, Stage s) {
        super(x, y, s);

        Texture texture = new Texture("images/Countertop2.png");
        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);

        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, width, height);

        this.setTexture(textureRegion);
    }
}
