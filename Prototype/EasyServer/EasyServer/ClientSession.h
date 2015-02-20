#pragma once

#include <map>
#include <WinSock2.h>
#include "PacketType.h"
#include "CircularBuffer.h"

#define BUFSIZE	(1024*10)

class ClientSession ;
class ClientManager ;
struct DatabaseJobContext ;

struct OverlappedIO : public OVERLAPPED
{
	OverlappedIO() : mObject(nullptr)
	{}

	ClientSession* mObject ;
} ;

class ClientSession
{
public:
	ClientSession(SOCKET sock, ClientID id)
		: mConnected(false), mLogon(false), mSocket(sock), mSendBuffer(BUFSIZE), mRecvBuffer(BUFSIZE), mOverlappedRequested(0)
		, mClientID(id)
	{
		memset(&mClientAddr, 0, sizeof(SOCKADDR_IN)) ;
	}
	~ClientSession() {}


	
	void	OnRead(size_t len) ;
	void	OnWriteComplete(size_t len) ;

	bool	OnConnect(SOCKADDR_IN* addr) ;
	
	bool	PostRecv() ;

	bool	SendRequest(PacketHeader* pkt) ;
	bool	Broadcast(PacketHeader* pkt) ;

	void	Disconnect() ;

	bool	IsConnected() const { return mConnected ; }


	/// 현재 Send/Recv 요청 중인 상태인지 검사하기 위함
	void	IncOverlappedRequest()		{ ++mOverlappedRequested ; }
	void	DecOverlappedRequest()		{ --mOverlappedRequested ; }
	bool	DoingOverlappedOperation() const { return mOverlappedRequested > 0 ; }

	template <class PKT_TYPE>
	bool ParsePacket(PKT_TYPE& pkt)
	{
		return mRecvBuffer.Read((char*)&pkt, pkt.mSize);
	}

	ClientID GetClientID() { return mClientID; }


private:

	bool	SendFlush(); ///< Send요청 중인것들 모아서 보냄
	void	OnTick() ;

private:
	bool			mConnected ;
	bool			mLogon ;

	ClientID		mClientID ;
	SOCKADDR_IN		mClientAddr ;

	SOCKET			mSocket;

	CircularBuffer	mSendBuffer ;
	CircularBuffer	mRecvBuffer ;

	OverlappedIO	mOverlappedSend ;
	OverlappedIO	mOverlappedRecv ;
	int				mOverlappedRequested ;

	friend class ClientManager ;
} ;




void CALLBACK RecvCompletion(DWORD dwError, DWORD cbTransferred, LPWSAOVERLAPPED lpOverlapped, DWORD dwFlags);
void CALLBACK SendCompletion(DWORD dwError, DWORD cbTransferred, LPWSAOVERLAPPED lpOverlapped, DWORD dwFlags);
