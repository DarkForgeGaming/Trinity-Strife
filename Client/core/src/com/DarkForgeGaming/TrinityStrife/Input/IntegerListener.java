package com.DarkForgeGaming.TrinityStrife.Input;

import com.badlogic.gdx.Input.TextInputListener;

public class IntegerListener implements TextInputListener {
	StringBuilder stringbuilder;

	public IntegerListener(StringBuilder stringbuilderToSetTo){
		stringbuilder = stringbuilderToSetTo;
	}
	
	@Override
	public void input(String text) {
		stringbuilder.append(text);
	}

	@Override
	public void canceled() {
		stringbuilder.append(0);
	}

}