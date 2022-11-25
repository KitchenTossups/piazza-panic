package com.eng1;

import com.badlogic.gdx.backends.lwjgl3.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		final int width = 1280, height = 720;
		final boolean extraChef = false;
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.useVsync(true);
		config.setWindowedMode(width, height);
		config.setTitle("Piazza Panic");
		new Lwjgl3Application(new PiazzaPanic(width, height, extraChef), config);
	}
}
