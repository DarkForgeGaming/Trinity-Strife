package com.DarkForgeGaming.TrinityStrife;

import java.io.IOException;
import java.net.UnknownHostException;

import com.DarkForgeGaming.TrinityStrife.Level.Level;
import com.DarkForgeGaming.TrinityStrife.Logging.LogHelper;
import com.DarkForgeGaming.TrinityStrife.Network.Initialization;
import com.DarkForgeGaming.TrinityStrife.Network.PacketListener;
import com.DarkForgeGaming.TrinityStrife.Player.ClientPlayer;
import com.DarkForgeGaming.TrinityStrife.Player.RemotePlayer;
import com.DarkForgeGaming.TrinityStrife.Reference.BooleanReference;
import com.DarkForgeGaming.TrinityStrife.Reference.NetworkReference;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {
	
	//---------------FIELD-------------------DECLARATIONS--------
	//---------------LibGDX-----------------TYPES----------------
	public OrthographicCamera camera;
	public SpriteBatch sprites1;
	public Level level = new Level(5000, 5000);
	
	int num = 0;
	
	//-----------------------PLAYERS-----------------------------
	public static ClientPlayer player;
	public static RemotePlayer player2;
	public static RemotePlayer player3;
	public static RemotePlayer player4;
	public static RemotePlayer player5;
	public static RemotePlayer player6;
	public static RemotePlayer player7;
	public static RemotePlayer player8;
	public static RemotePlayer player9;
	public static RemotePlayer player10;
	public static RemotePlayer player11;
	public static RemotePlayer player12;
	
	//------END--------------FIELD------------DECLARATIONS-------
	
	@Override
	public void create () {
		
		camera = new OrthographicCamera(level.getWidth(), level.getHeight());
		try {
			Initialization.startSocket();
		} catch (UnknownHostException e) {
			LogHelper.error("The Host IP and port given did not result to a valid server.");
			e.printStackTrace();
		} catch (IOException e) {
			LogHelper.error("A Java IOException occurred while attempting to Initialize the socket.");
			e.printStackTrace();
		}
		
	}

	@Override
	public void render (){
		if(num == 3){
			if(BooleanReference.connectedToServer && NetworkReference.hasSentLoginRequest){
				try{
					PacketListener.listen();
				}catch(IOException e){
					LogHelper.error("An IO Exception occurred while running PacketListener.listen()");
					e.printStackTrace();
				}
			}
			num = 0;
		}
		else if(num != 3){
			num++;
		}
	}//[][][][][][][][][][][][][End][][][][Render][][][][][][][][][][][][][][][][][][][]
}
