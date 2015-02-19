#pragma once
#include "Character.h"
class CharacterManager
{
public:
	CharacterManager();
	~CharacterManager();
	
	Character* CreateCharacter(ClientID);
	Character* GetCharacter(ClientID id){
		if (mCharacterList.find(id) == mCharacterList.end())
			return nullptr;
		return mCharacterList.at(id);
	}

	void onTick(float dt);
private:
	typedef std::map<ClientID, Character*> CharacterList;
	CharacterList mCharacterList;
};

extern CharacterManager* GCharacterManager;

