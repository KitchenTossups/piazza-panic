package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.eng1.base.BaseActor;

public class Steam extends BaseActor {
    public Steam(float x, float y, Stage s) {
        super(x, y, s);

        Texture texture = new Texture(Gdx.files.internal("spritemap/steam.png"), true);

        int rows = 4;
        int cols = 8;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();

        float frameDuration = 0.2f;

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);
        Animation<TextureRegion> animation = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        setAnimation(animation);
    }
}
