package com.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.eng1.base.BaseActor;
import com.eng1.base.BaseScreen;

import java.util.Date;

public class EndScreen extends BaseScreen {

    public EndScreen(float width, float height, int binnedItems, Label.LabelStyle[] labelStyles, long startTime) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture( "background/background.png" );
        background.setSize(width, height);

        Label titleLabel = new Label("Game Completed!", labelStyles[0]), contents = new Label(String.format("This game took %d seconds to complete with %d number of binned items!\nTouch anywhere to exit!", (new Date().getTime() - startTime) / 1000, binnedItems), labelStyles[1]);
        titleLabel.setAlignment(Align.center);
        contents.setAlignment(Align.center);
        this.uiTable.pad(50);
        this.uiTable.add(titleLabel).expandX();
        this.uiTable.row();
        this.uiTable.add(contents).expand();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched())
            System.exit(0);
    }
}
