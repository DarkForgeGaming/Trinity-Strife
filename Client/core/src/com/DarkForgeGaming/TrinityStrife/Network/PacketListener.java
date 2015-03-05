package com.DarkForgeGaming.TrinityStrife.Network;

import java.io.IOException;

import com.DarkForgeGaming.TrinityStrife.Main;
import com.DarkForgeGaming.TrinityStrife.Logging.LogHelper;
import com.DarkForgeGaming.TrinityStrife.Reference.NetworkReference;

public class PacketListener {
	static int length;
	
	
	
	public static void listen(){
		try{
			length = NetworkReference.in.readInt();
		} catch(IOException e){
			LogHelper.info("An IOException occured in PacketListener");
			e.printStackTrace();
		}
		if(NetworkReference.packetLast == null){
			NetworkReference.packetLast = new byte[0];
		}
		if(NetworkReference.packetCurrent != NetworkReference.packetLast){
			try {
				NetworkReference.in.readFully(NetworkReference.packetCurrent, 0, length);
			} catch (IOException e) {
				LogHelper.error("An IOException occured while reading packet in.");
				e.printStackTrace();
			}
			if(NetworkReference.packetCurrent != null && NetworkReference.packetCurrent != NetworkReference.packetLast){
				String packetType = PacketHandler.parsePacket(NetworkReference.packetCurrent);
				if(packetType == "LoginResponse"){
					PacketLoginResponse.decompilePacket(NetworkReference.packetCurrent);
					LogHelper.debug("Username: " + Main.player.getName() + " accepted by server. Player ID = " + Main.player.getPlayerID() + ". Position: (" + Main.player.getPosX() + ", " + Main.player.getPosY() + ").");
				}
			}
		}
	}
}
