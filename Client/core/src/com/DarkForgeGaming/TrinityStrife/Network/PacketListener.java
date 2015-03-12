package com.DarkForgeGaming.TrinityStrife.Network;

import java.io.IOException;

import com.DarkForgeGaming.TrinityStrife.Main;
import com.DarkForgeGaming.TrinityStrife.Logging.LogHelper;
import com.DarkForgeGaming.TrinityStrife.Reference.BooleanReference;
import com.DarkForgeGaming.TrinityStrife.Reference.NetworkReference;

public class PacketListener {
	static int length;
	
	
	
	public static void listen() throws IOException{
		boolean isEmpty = true;
		if(BooleanReference.connectedToServer && NetworkReference.hasSentLoginRequest){
			if(NetworkReference.in.available() != 0){
				try{
					byte[] tempPacket = new byte[NetworkReference.PKT_MAX];
					NetworkReference.in.readFully(tempPacket, 0, 46);
					for(int i=0; i<tempPacket.length; i++){
						if(tempPacket[i] != 0){
							isEmpty = false;
							break;
						}
						else if(tempPacket[i] == 0){
							isEmpty = true;
						}
					}
					if(!isEmpty){
						NetworkReference.packetCurrent = tempPacket;
					}
				} catch(IOException e){
					LogHelper.error("IOException in reading incoming packet.");
					e.printStackTrace();
				} catch(NullPointerException e){
					LogHelper.debug("Packet was not a LoginResponse");
				}
				if(NetworkReference.packetCurrent != NetworkReference.packetLast && isEmpty == false){
					String packetType = PacketHandler.parsePacket(NetworkReference.packetCurrent);
					LogHelper.debug("Packet parsed as: " + packetType);
					if(packetType == "LoginResponse"){
						PacketLoginResponse.decompilePacket(NetworkReference.packetCurrent);
						LogHelper.debug("Username: " + Main.player.getName() + " accepted by server. Player ID = " + Main.player.getPlayerID() + ". Position: (" + Main.player.getPosX() + ", " + Main.player.getPosY() + ").");
						NetworkReference.packetLast = NetworkReference.packetCurrent;
					}
				}
			}
		}
	}
}
