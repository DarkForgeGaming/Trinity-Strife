package com.DarkForgeGaming.TrinityStrife.Logging;

public class LogLevel {
	public String logLevel;
	public int levelNumber;
	
	public LogLevel(String level){
		this.logLevel = level;
		this.levelNumber = GetLevelNumber(level);
	}
	
	public static int GetLevelNumber(String level){
		if(level.equals("ERROR_CRITICAL")){
			return 0;
		}
		else if(level.equals("ERROR_NORMAL")){
			return 1;
		}
		else if(level.equals("WARNING")){
			return 2;
		}
		else if(level.equals("INFO")){
			return 3;
		}
		else if(level.equals("DEBUG")){
			return 4;
		}
		else if(level.equals("STACK_TRACE")){
			return 5;
		}
		else{
			return 3;
		}
	}
}
