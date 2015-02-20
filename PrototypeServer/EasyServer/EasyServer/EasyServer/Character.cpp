#include "stdafx.h"
#include "Character.h"


Character::Character(ClientID id)
	: mID(id), mSpeed(1), mX(0.f), mY(0.f), mDestinationX(mX), mDestinationY(mY)
{
}


Character::~Character()
{
}

void Character::OnTick(float dt)
{
	Move(dt);
}

void Character::Move(float dt)
{
	if (mX != mDestinationX && mY != mDestinationY)
	{
		float moveLength = dt * mSpeed;
		float moveLength_Square = pow(moveLength, 2);
		float dX = mDestinationX - mX;
		float dY = mDestinationY - mY;
		float destinationLength_Square = pow(dX, 2) + pow(dY, 2);
		if (moveLength_Square >= destinationLength_Square)
		{
			mX = mDestinationX;
			mY = mDestinationY;
		}
		else
		{
			float destinationLength = sqrt(destinationLength_Square);
			mX += (dX / destinationLength) * moveLength;
			mY += (dY / destinationLength) + moveLength;
		}
	}
}
