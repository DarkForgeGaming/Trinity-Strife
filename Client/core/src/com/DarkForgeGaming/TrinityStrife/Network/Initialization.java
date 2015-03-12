package com.DarkForgeGaming.TrinityStrife.Network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import com.DarkForgeGaming.TrinityStrife.Input.IntegerListener;
import com.DarkForgeGaming.TrinityStrife.Input.StringListener;
import com.DarkForgeGaming.TrinityStrife.Logging.LogHelper;
import com.DarkForgeGaming.TrinityStrife.Reference.BooleanReference;
import com.DarkForgeGaming.TrinityStrife.Reference.IntegerReference;
import com.DarkForgeGaming.TrinityStrife.Reference.NetworkReference;
import com.DarkForgeGaming.TrinityStrife.Reference.StringReference;
import com.badlogic.gdx.Gdx;

public class Initialization {
	static boolean waiting = false;
	public static void startSocket() throws UnknownHostException, IOException{
		int numberComplete = 0;
		while(numberComplete < 5){
			switch(numberComplete){
			case 0:
				if(waiting == false){
					Gdx.input.getTextInput(new StringListener(StringReference.serverIP), "Server IP", "", "localhost if on same computer");
					waiting = true;
				}
				else if(waiting){
					if(!StringReference.serverIP.toString().equals("")){
						waiting = false;
						numberComplete++;
						LogHelper.debug("Server IP: " + StringReference.serverIP.toString());
					}
				}
				break;
			case 1:
				if(waiting == false){
					Gdx.input.getTextInput(new IntegerListener(StringReference.serverPort), "Server Port", "", "default is 9002");
					waiting = true;
				}
				else if(waiting){
					if(!StringReference.serverPort.toString().equals("")){
						waiting = false;
						numberComplete++;
						LogHelper.debug("Server Port: " + StringReference.serverPort.toString());
					}
				}
				break;
			case 2:
				if(!StringReference.serverIP.toString().equals("") && !StringReference.serverPort.toString().equals("")){
					LogHelper.debug("Connecting to Server at: " + StringReference.serverIP + " on port: " + StringReference.serverPort.toString());
					NetworkReference.socket = new Socket(StringReference.serverIP.toString(), Integer.parseInt(StringReference.serverPort.toString()));
					NetworkReference.reader = new BufferedReader(new InputStreamReader(NetworkReference.socket.getInputStream()));
					NetworkReference.out = new DataOutputStream(new BufferedOutputStream(NetworkReference.socket.getOutputStream()));
					NetworkReference.in = new DataInputStream(new BufferedInputStream(NetworkReference.socket.getInputStream()));
					numberComplete++;
					BooleanReference.connectedToServer = true;
					LogHelper.debug("Connected to Server.");
					LogHelper.info("Connected to Server at IP: " + StringReference.serverIP.toString() + " on Port: " + StringReference.serverPort.toString() + ".");
				}
				break;
			case 3:
				if(waiting == false){
					Gdx.input.getTextInput(new StringListener(StringReference.playerName), "Enter Username", "", "Maximum 30 characters, no special characters");
					waiting = true;
				}
				else if(waiting){
					if(!StringReference.playerName.toString().equals("")){
						waiting = false;
						numberComplete++;
					}
				}
				break;
			case 4:
				if(BooleanReference.connectedToServer){
					byte[] loginRequest = PacketHandler.createLoginRequest(StringReference.playerName.toString());
					NetworkReference.out.write(loginRequest);
					NetworkReference.out.flush();
					NetworkReference.hasSentLoginRequest = true;
					numberComplete++;
				}
				else{
					LogHelper.error("Cannot request login if not connected to a server.");
				}
				break;
			default:
				break;
			}
		}
	}
	
	static void getStringFromTemp(String stringToUse){
		if(StringReference.tempString != ""){
			stringToUse = StringReference.tempString;
			StringReference.tempString = "";
		}
	}
	
	static void getIntFromTemp(int intToUse){
		if(IntegerReference.tempInt != 0){
			intToUse = IntegerReference.tempInt;
			IntegerReference.tempInt = 0;
		}
	}
}
