#pragma once
class Character
{
public:
	Character(ClientID);
	~Character();

	void OnTick(float dt);

	void SetX(float x){ mX = x; }
	void SetY(float y){ mY = y; }

	void SetDestinationX(float x){ mX = x; }
	void SetDestinationY(float y){ mY = y; }

	void SetName(std::string name) { mName = name; }

	float GetX() { return mX; }
	float GetY() { return mY; }
private:
	void Move(float dt);
	
private:
	ClientID mID;
	float mX;
	float mY;
	float mDestinationX;
	float mDestinationY;
	float mSpeed;
	std::string mName;
};

