package com.eng1;

import com.badlogic.gdx.backends.lwjgl3.*;
import com.eng1.enums.Difficulty;
import com.eng1.enums.Mode;
import com.eng1.test.DragAndDropTest;
import com.eng1.test.GestureDetectorTest;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class PiazzaPanicLauncher {
	public static void main (String[] arg) {
		final int width = 1280, height = 720;
		final Mode mode = Mode.ASSESSMENT_1;
		final Difficulty difficulty = Difficulty.MEDIUM;
		final float loci = 40f;
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.useVsync(true);
		config.setWindowedMode(width, height);
		config.setTitle("Piazza Panic");
		new Lwjgl3Application(new PiazzaPanic(width, height, mode, loci, difficulty), config);
//		new Lwjgl3Application(new DragAndDropTest());
	}
}
