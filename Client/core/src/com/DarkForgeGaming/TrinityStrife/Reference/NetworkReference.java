package com.DarkForgeGaming.TrinityStrife.Reference;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class NetworkReference {
	public static Socket socket;
	public static BufferedReader reader;
	public static DataOutputStream out;
	public static DataInputStream in;
	public static byte[] packetLast;
	public static byte[] packetCurrent = new byte[46];
	public static boolean hasSentLoginRequest = false;
	public static final int PKT_MAX = 1024;
}
