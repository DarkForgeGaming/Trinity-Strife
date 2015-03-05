package com.DarkForgeGaming.TrinityStrife.Logging;

import com.DarkForgeGaming.TrinityStrife.Reference.LogLevels;

public class LogHelper {
	public static void criticial(String text){
		LogHandler.SendLog(LogLevels.ERROR_CRITICAL, text);
	}
	
	public static void error(String text){
		LogHandler.SendLog(LogLevels.ERROR_NORMAL, text);
	}
	
	public static void warn(String text){
		LogHandler.SendLog(LogLevels.WARNING, text);
	}
	
	public static void info(String text){
		LogHandler.SendLog(LogLevels.INFO, text);
	}
	
	public static void debug(String text){
		LogHandler.SendLog(LogLevels.DEBUG, text);
	}
	
	public static void stack(String text){
		LogHandler.SendLog(LogLevels.STACK_TRACE, text);
	}
}
