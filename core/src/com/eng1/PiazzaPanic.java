package com.eng1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.*;

public class PiazzaPanic extends Game {

	private final int width, height;

	SpriteBatch batch;
	BitmapFont font;
	Stage stage;

	public PiazzaPanic(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void create() {
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		this.stage = new Stage();
		setScreen(new MainMenuScreen(this, this.width, this.height));
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
