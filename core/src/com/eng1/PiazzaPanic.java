package com.eng1;

import com.eng1.base.BaseGame;
import com.eng1.screen.MainMenuScreen;

public class PiazzaPanic extends BaseGame {

    private final int width, height;

    private final Mode mode;

    public PiazzaPanic(int width, int height, Mode mode) {
        this.width = width;
        this.height = height;
        this.mode = mode;
    }

    public void create() {
        super.create();
        this.setActiveScreen(new MainMenuScreen(this, this.width, this.height, this.mode));
    }
}
