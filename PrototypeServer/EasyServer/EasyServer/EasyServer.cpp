#include "stdafx.h"
#include "EasyServer.h"

#include "PacketType.h"

#include "Exception.h"
#include "ClientSession.h"
#include "ClientManager.h"
#include "ProducerConsumerQueue.h"
#include "CharacterManager.h"
#pragma comment(lib,"ws2_32.lib")


__declspec(thread) int LThreadType = -1 ;

typedef ProducerConsumerQueue<SOCKET, 100> PendingAcceptList;

int _tmain(int argc, _TCHAR* argv[])
{
	/// crash 발생시 dump 남기기 위해서
	SetUnhandledExceptionFilter(ExceptionFilter) ;

	LThreadType = THREAD_MAIN ;

	/// Manager Init
	GClientManager = new ClientManager ;
	GCharacterManager = new CharacterManager;

	/// 윈속 초기화
	WSADATA wsa ;
	if (WSAStartup(MAKEWORD(2,2), &wsa) != 0)
		return -1 ;

	SOCKET listenSocket = socket(AF_INET, SOCK_STREAM, 0) ;
	if (listenSocket == INVALID_SOCKET)
		return -1 ;

	int opt = 1 ;
	setsockopt(listenSocket, SOL_SOCKET, SO_REUSEADDR, (const char*)&opt, sizeof(int) ) ;

	/// bind
	SOCKADDR_IN serveraddr ;
	ZeroMemory(&serveraddr, sizeof(serveraddr)) ;
	serveraddr.sin_family = AF_INET ;
	serveraddr.sin_port = htons(LISTEN_PORT) ;
	serveraddr.sin_addr.s_addr = htonl(INADDR_ANY) ;
	int ret = bind(listenSocket, (SOCKADDR*)&serveraddr, sizeof(serveraddr)) ;
	if (ret == SOCKET_ERROR)
		return -1 ;
	
	/// listen
	ret = listen(listenSocket, SOMAXCONN) ;
	if (ret == SOCKET_ERROR)
		return -1 ;

	/// accepting list
	PendingAcceptList pendingAcceptList;

	/// Client Logic + I/O Thread
	DWORD dwThreadId ;
	HANDLE hThread = (HANDLE)_beginthreadex(NULL, 0, ClientHandlingThread, (LPVOID)&pendingAcceptList, 0, (unsigned int*)&dwThreadId);
    if (hThread == NULL)
		return -1 ;

	/// accept loop
	while ( true )
	{
		SOCKET acceptedSocket = accept(listenSocket, NULL, NULL) ;
		if (acceptedSocket == INVALID_SOCKET)
		{
			printf("accept: invalid socket\n") ;
			continue ;
		}

		pendingAcceptList.Produce(acceptedSocket);
	}

	CloseHandle( hThread ) ;

	// 윈속 종료
	WSACleanup() ;

	delete GClientManager ;
	return 0 ;
}

unsigned int WINAPI ClientHandlingThread( LPVOID lpParam )
{
	LThreadType = THREAD_CLIENT ;

	PendingAcceptList* pAcceptList = (PendingAcceptList*)lpParam ;

	/// Timer
	HANDLE hTimer = CreateWaitableTimer(NULL, FALSE, NULL) ;
	if (hTimer == NULL)
		return -1 ;

	LARGE_INTEGER liDueTime ;
	liDueTime.QuadPart = -10000000 ; ///< 1초 후부터 동작
	if ( !SetWaitableTimer(hTimer, &liDueTime, 1, TimerProc, NULL, TRUE) )
		return -1 ;
		
	while ( true )
	{
		SOCKET acceptSock = NULL;

		/// 새로 접속한 클라이언트 처리
		if (pAcceptList->Consume(acceptSock, false))
		{
			/// 소켓 정보 구조체 할당과 초기화
			ClientSession* client = GClientManager->CreateClient(acceptSock);
			//ClientSession aClient(acceptSock, 0);
			//ClientSession* client = &aClient;
			SOCKADDR_IN clientaddr;
			int addrlen = sizeof(clientaddr);
			getpeername(acceptSock, (SOCKADDR*)&clientaddr, &addrlen);

			// 클라 접속 처리
			if (false == client->OnConnect(&clientaddr))
			{
				client->Disconnect();
			}

			continue; ///< 다시 대기로
		}

		/// 최종적으로 클라이언트들에 쌓인 send 요청 처리
		GClientManager->FlushClientSend();

		/// APC Queue에 쌓인 작업들 처리
		SleepEx(INFINITE, TRUE);
	}

	CloseHandle( hTimer ) ;
	return 0;
} 

void CALLBACK TimerProc(LPVOID lpArg, DWORD dwTimerLowValue, DWORD dwTimerHighValue)
{
	assert( LThreadType == THREAD_CLIENT ) ;

	GClientManager->OnPeriodWork() ;

	static ULONGLONG prevTime = 0;
	if (prevTime == 0)
	{
		prevTime = dwTimerLowValue + (((ULONGLONG)dwTimerHighValue) << 32);
	}

	ULONGLONG nowTime = dwTimerLowValue + (((ULONGLONG)dwTimerHighValue) << 32);
	float dt = ((nowTime - prevTime) / 1000.f);

	GCharacterManager->onTick(dt);
}
