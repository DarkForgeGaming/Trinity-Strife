package com.DarkForgeGaming.TrinityStrife.Player;

public class Player {
	static String name;
	static int playerID;
	static float posX;
	static float posY;

	public Player(String playername, int playerid, float posx, float posy){
		name = playername;
		playerID  = playerid;
		posX = posx;
		posY = posy;
	}
	
	public float getPosX(){
		return posX;
	}
	
	public float getPosY(){
		return posY;
	}
	
	public String getName(){
		return name;
	}
	
	public int getPlayerID(){
		return playerID;
	}
	
	public void setPlayerID(int id){
		playerID = id;
	}
	
	public void setName(String pname){
		name = pname;
	}
	
	public void setPosX(float xpos){
		posX = xpos;
	}
	
	public void setPosY(float ypos){
		posY = ypos;
	}
	
	public void setPos(float xpos, float ypos){
		posX = xpos;
		posY = ypos;
	}
}
