package com.DarkForgeGaming.TrinityStrife.Network;

import com.DarkForgeGaming.TrinityStrife.Main;

public class PacketLoginResponse {
	public static void decompilePacket(byte[] packet){
		String playerID = "";
		String posX = "";
		String posY = "";
		StringBuilder playerName = new StringBuilder(30);
		char tempChar;
		for(int i=4; i<8; i++){
			tempChar = (char)packet[i];
			playerID = playerID + tempChar;
		}
		for(int i=8; i<12; i++){
			tempChar = (char)packet[i];
			posX = posX + tempChar;
		}
		for(int i=12; i<16; i++){
			tempChar = (char)packet[i];
			posY = posY + tempChar;
		}
		for(int i=16; i<packet.length; i++){
			playerName.setCharAt(i, (char)packet[i]);
		}
		playerName.reverse();
		Main.player.setName(playerName.toString());
		Main.player.setPlayerID(Integer.parseInt(playerID));
		Main.player.setPos((float)Integer.parseInt(posX), (float)Integer.parseInt(posY));
		}
}
