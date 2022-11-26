package com.eng1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.eng1.Objects.Mode;

public class PiazzaPanic extends Game {

	private final int width, height;

	private final Mode mode;

	SpriteBatch batch;
	BitmapFont font;
	Stage stage;

	public PiazzaPanic(int width, int height, Mode mode) {
		this.width = width;
		this.height = height;
		this.mode = mode;
	}

	public void create() {
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		this.stage = new Stage();
		setScreen(new MainMenuScreen(this, this.width, this.height, this.mode));
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
