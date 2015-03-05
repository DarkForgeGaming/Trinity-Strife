package com.DarkForgeGaming.TrinityStrife.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.DarkForgeGaming.TrinityStrife.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Trinity Strife";
		config.width = 1920;
		config.height = 1080;
		new LwjglApplication(new Main(), config);
	}
}
