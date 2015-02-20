#pragma once
#include <WinSock2.h>

class ClientSession ;
struct PacketHeader ;
struct DatabaseJobContext ;

class ClientManager
{
public:
	ClientManager() : mLastGCTick(0), mLastClientWorkTick(0)
	{}

	ClientSession* CreateClient(SOCKET sock) ;
	
	void BroadcastPacket(ClientSession* from, PacketHeader* pkt) ;

	void OnPeriodWork() ;

	void FlushClientSend();

private:
	void CollectGarbageSessions() ;
	void ClientPeriodWork() ;
	

	ClientID GenerateClientID();

private:
	typedef std::map<ClientID, ClientSession*> ClientList ;
	ClientList	mClientList ;

	DWORD		mLastGCTick ;
	DWORD		mLastClientWorkTick ;
} ;

extern ClientManager* GClientManager ;