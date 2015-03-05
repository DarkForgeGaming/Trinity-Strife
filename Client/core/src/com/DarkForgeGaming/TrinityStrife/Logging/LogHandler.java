package com.DarkForgeGaming.TrinityStrife.Logging;

import com.DarkForgeGaming.TrinityStrife.Reference.LogLevels;

public class LogHandler {
	public static void SendLog(LogLevel level, String text){
		if(level.levelNumber <= LogLevels.LogLevel){
			if(level.levelNumber == 0){
				System.err.println("[CRITICAL] " + text);
			}
			else if(level.levelNumber == 1){
				System.err.println("[ERROR] " + text);
			}
			else if(level.levelNumber == 2){
				System.out.println("[WARNING] " + text);
			}
			else if(level.levelNumber == 3){
				System.out.println("[INFO] " + text);
			}
			else if(level.levelNumber == 4){
				System.out.println("[DEBUG] " + text);
			}
			else if(level.levelNumber == 5){
				System.out.println("[STACK] " + text);
			}
			else{
				System.err.println("A Logging Error Has Occurred. Sorry about that.");
			}
		}
	}
}
