package com.eng1;

import com.eng1.base.BaseGame;
import com.eng1.enums.Difficulty;
import com.eng1.enums.Mode;
import com.eng1.screen.MainMenuScreen;

public class PiazzaPanic extends BaseGame {

    private final int width, height;

    private final Mode mode;
    private final float loci;
    private final Difficulty difficulty;
    private final boolean verbose;

    public PiazzaPanic(int width, int height, Mode mode, float loci, Difficulty difficulty, boolean verbose) {
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.loci = loci;
        this.difficulty = difficulty;
        this.verbose = verbose;
    }

    public void create() {
        super.create();
        this.setActiveScreen(new MainMenuScreen(this, this.width, this.height, this.mode, this.loci, this.difficulty, this.verbose));
    }

    @Override
    public void dispose() {
        super.dispose();
        System.exit(0);
    }
}
