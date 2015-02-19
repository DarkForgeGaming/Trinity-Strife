#include "stdafx.h"
#include "ClientSession.h"
#include "CharacterManager.h"


//@{ Handler Helper

typedef void(*HandlerFunc)(ClientSession* session);

static HandlerFunc HandlerTable[PKT_MAX];

static void DefaultHandler(ClientSession* session)
{
	printf("[DEBUG] Invalid packet handler", session->GetClientID());
	session->Disconnect();
}

struct InitializeHandlers
{
	InitializeHandlers()
	{
		for (int i = 0; i < PKT_MAX; ++i)
			HandlerTable[i] = DefaultHandler;
	}
} _init_handlers_;

struct RegisterHandler
{
	RegisterHandler(int pktType, HandlerFunc handler)
	{
		HandlerTable[pktType] = handler;
	}
};

#define REGISTER_HANDLER(PKT_TYPE)	\
	static void Handler_##PKT_TYPE(ClientSession* session); \
	static RegisterHandler _register_##PKT_TYPE(PKT_TYPE, Handler_##PKT_TYPE); \
	static void Handler_##PKT_TYPE(ClientSession* session)

//@}

void CALLBACK RecvCompletion(DWORD dwError, DWORD cbTransferred, LPWSAOVERLAPPED lpOverlapped, DWORD dwFlags)
{
	ClientSession* fromClient = static_cast<OverlappedIO*>(lpOverlapped)->mObject;

	//	fromClient->DecRefCount();

	if (!fromClient->IsConnected())
		return;

	/// 에러 발생시 해당 세션 종료
	if (dwError || cbTransferred == 0)
	{
		fromClient->Disconnect();
		return;
	}

	/// 받은 데이터 처리
	fromClient->OnRead(cbTransferred);

	/// 다시 받기
	if (false == fromClient->PostRecv())
	{
		fromClient->Disconnect();
		return;
	}
}


void CALLBACK SendCompletion(DWORD dwError, DWORD cbTransferred, LPWSAOVERLAPPED lpOverlapped, DWORD dwFlags)
{
	ClientSession* fromClient = static_cast<OverlappedIO*>(lpOverlapped)->mObject;

	//	fromClient->DecRefCount();

	if (!fromClient->IsConnected())
		return;

	/// 에러 발생시 해당 세션 종료
	if (dwError || cbTransferred == 0)
	{
		fromClient->Disconnect();
		return;
	}

	fromClient->OnWriteComplete(cbTransferred);

}

///////////////////////////////////////////////////////////

void ClientSession::OnRead(size_t len)
{
	mRecvBuffer.Commit(len);

	/// 패킷 파싱하고 처리
	while (true)
	{
		/// 패킷 헤더 크기 만큼 읽어와보기
		PacketHeader header;
		if (false == mRecvBuffer.Peek((char*)&header, sizeof(PacketHeader)))
			return;

		/// 패킷 완성이 되는가? 
		if (mRecvBuffer.GetStoredSize() < (size_t)header.mSize)
			return;


		if (header.mType >= PKT_MAX || header.mType <= PKT_NONE)
		{
			printf("[DEBUG] Invalid packet type", GetClientID());
			Disconnect();
			return;
		}

		/// packet dispatch...
		HandlerTable[header.mType](this);
	}
}

//////////////////////////////////////////////////////////////////////////

REGISTER_HANDLER(PKT_CS_LOGIN)
{
	LoginRequest inPacket;
	if (false == session->ParsePacket(inPacket))
	{
		printf("[DEBUG] packet parsing error", inPacket.mType);
		return;
	}
	auto character = GCharacterManager->CreateCharacter(session->GetClientID());

	int length = strnlen_s(inPacket.mCharacterName, MAX_NAME_LEN);
	character->SetName(std::string(inPacket.mCharacterName, length));

	printf("%s\n", inPacket.mCharacterName);
	{
		LoginResult outPacket;
		memcpy(outPacket.mName, inPacket.mCharacterName, length);
		outPacket.mPlayerId = session->GetClientID();
		outPacket.mPosX = character->GetX();
		outPacket.mPosY = character->GetY();

		printf("%s\n", outPacket.mName);
		session->Broadcast(&outPacket);
	}
}

REGISTER_HANDLER(PKT_CS_MOVE)
{
	MoveRequest inPacket;
	if (false == session->ParsePacket(inPacket))
	{
		printf("[DEBUG] packet parsing error", inPacket.mType);
		return;
	}
	auto character = GCharacterManager->GetCharacter(session->GetClientID());
	CRASH_ASSERT(character != nullptr);

	character->SetDestinationX(inPacket.mPosX);
	character->SetDestinationY(inPacket.mPosY);

	{
		MoveBroadcastResult outPacket;
		outPacket.mPlayerId = session->GetClientID();
		outPacket.mStartPosX = character->GetX();
		outPacket.mStartPosY = character->GetY();
		outPacket.mEndPosX = inPacket.mPosX;
		outPacket.mEndPosY = inPacket.mPosY;
		
		session->Broadcast(&outPacket);
	}
}
