package com.eng1.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.base.BaseActor;

public class Table extends BaseActor {

    public Table(Stage s) {
        super(0, 0, s);

        this.loadTexture("images/Table1.png");
    }
}

