#include "stdafx.h"
#include "CharacterManager.h"


CharacterManager* GCharacterManager = nullptr;

CharacterManager::CharacterManager()
{
}


CharacterManager::~CharacterManager()
{
}

Character* CharacterManager::CreateCharacter(ClientID id)
{
	Character* character = new Character(id);
	mCharacterList.insert(CharacterList::value_type(id, character));
	return character;
}

void CharacterManager::onTick(float dt)
{
	for (auto& character : mCharacterList)
		character.second->OnTick(dt);
}
