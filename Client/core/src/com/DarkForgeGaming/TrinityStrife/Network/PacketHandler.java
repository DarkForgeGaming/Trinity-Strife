package com.DarkForgeGaming.TrinityStrife.Network;

public class PacketHandler {
	public static String parsePacket(byte[] packet){
		String packetType = null;
		//LoginResponse
		if(packet[0] == 0 && packet[1] == 46 && packet[2] == 0 && packet[3] == 2){
			packetType = "LoginResponse";
		}
		else{
			boolean isEmpty = true;
			for(int i=0; i<packet.length; i++){
				if(packet[i] != 0){
					isEmpty = false;
					break;
				}
				else if(packet[i] == 0){
					isEmpty = true;
				}
			}
			if(isEmpty){
				packetType = "Empty";
			}
		}
		if(packetType != null){
			return packetType;
		}
		else{
			return "None";
		}
	}
	
	public static byte[] createLoginRequest(String name){
		
		byte[] bytes = name.getBytes();
		byte[] bytesToSend = new byte[34];
		byte[] bytesProcessed = new byte[34];
		
		bytesToSend[0] = 0;
		bytesToSend[1] = 34;
		bytesToSend[2] = 0;
		bytesToSend[3] = 1;
		
		for(int i=0; i<bytes.length; i++){
			bytesToSend[i+4] = bytes[i];
		}
		
		for(int i=0; i<(30-bytes.length); i++){
			bytesToSend[4+bytes.length+i] = 0;
		}
		
		bytesProcessed[0] = bytesToSend[1];
		bytesProcessed[1] = bytesToSend[0];
		bytesProcessed[2] = bytesToSend[3];
		bytesProcessed[3] = bytesToSend[2];
		
		for(int i=0; i<30; i++){
			bytesProcessed[i+4] = bytesToSend[i+4];
		}
		
		return bytesProcessed;
	}
	
	
}
