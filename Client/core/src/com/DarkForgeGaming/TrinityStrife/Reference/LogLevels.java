package com.DarkForgeGaming.TrinityStrife.Reference;

import com.DarkForgeGaming.TrinityStrife.Logging.LogLevel;

public class LogLevels {
	
	/*
	 * Sets the level of logs to show. Default is set to 3 (INFO).
	 * The level named and all above it will be shown in the console.
	 * You can change this to any of the LogLevels listed below.
	 * Possible Inputs:
	 * 0 - ERROR_CRITICAL
	 * 1 - ERROR_NORMAL
	 * 2 - WARNING
	 * 3 - INFO
	 * 4 - DEBUG
	 * 5 - STACK_TRACE
	 */
	public static final int LogLevel = 4;
	
	//-----------------------------LOGLEVELS--------------------------------------
	
	//A critical error is an error that causes the program to be unable to continue
	public static final LogLevel ERROR_CRITICAL = new LogLevel("ERROR_CRITICAL");
	
	//A normal error is any type of error that causes the program to work incorrectly but may be recoverable
	public static final LogLevel ERROR_NORMAL = new LogLevel("ERROR_NORMAL");
	
	//A warning is used when a problem is reached but the program will still work correctly
	public static final LogLevel WARNING = new LogLevel("WARNING");
	
	//Info is used to inform the user that an event has occurred. It is the default base of logging level.
	public static final LogLevel INFO = new LogLevel("INFO");
	
	//Debug logs do not show by default, but if level is set low enough to allow, it's used for debugging code.
	public static final LogLevel DEBUG = new LogLevel("DEBUG");
	
	//Stack trace does not show by default, but if level is set low enough to allow, it's used to log every little detail in a stack and is a more in depth version of debug.
	public static final LogLevel STACK_TRACE = new LogLevel("STACK_TRACE");
}
