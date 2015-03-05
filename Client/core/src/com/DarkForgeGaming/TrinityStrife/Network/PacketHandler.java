package com.DarkForgeGaming.TrinityStrife.Network;

public class PacketHandler {
	public static String parsePacket(byte[] packet){
		String packetType = "none";
		//LoginResponse
		if(packet[0] == 0x00 && packet[1] == 0x46 && packet[2] == 0x00 && packet[3] == 0x01){
			packetType = "LoginResponse";
		}
		return packetType;
	}
	
	public static byte[] createLoginRequest(String name){
		
		byte[] bytes = name.getBytes();
		byte[] bytesToSend = new byte[34];
		byte[] bytesProcessed = new byte[34];
		
		bytesToSend[0] = 0x00;
		bytesToSend[1] = 0x22;
		bytesToSend[2] = 0x00;
		bytesToSend[3] = 0x01;
		
		for(int i=0; i<bytes.length; i++){
			bytesToSend[i+4] = bytes[i];
		}
		
		for(int i=0; i<(30-bytes.length); i++){
			bytesToSend[4+bytes.length+i] = 0x00;
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
