package com.eng1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;

public class PiazzaPanic extends Game {

	private final int width, height;
	private final boolean extraChef;

	SpriteBatch batch;
	BitmapFont font;
	Stage stage;

	public PiazzaPanic(int width, int height, boolean extraChef) {
		this.width = width;
		this.height = height;
		this.extraChef = extraChef;
	}

	public void create() {
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		this.stage = new Stage();
		setScreen(new MainMenuScreen(this, this.width, this.height, this.extraChef));
	}

	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.font.dispose();
		this.stage.dispose();
	}
}
