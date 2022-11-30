package com.eng1;

import com.eng1.base.BaseGame;
import com.eng1.enums.Mode;
import com.eng1.screen.MainMenuScreen;

public class PiazzaPanic extends BaseGame {

    private final int width, height;

    private final Mode mode;
    private final float loci;

    public PiazzaPanic(int width, int height, Mode mode, float loci) {
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.loci = loci;
    }

    public void create() {
        super.create();
        this.setActiveScreen(new MainMenuScreen(this, this.width, this.height, this.mode, this.loci));
    }
}
