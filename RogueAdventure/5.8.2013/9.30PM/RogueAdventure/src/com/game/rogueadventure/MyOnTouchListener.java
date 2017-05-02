package com.game.rogueadventure;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyOnTouchListener implements OnTouchListener {

	Game game;
	MyOnTouchListener(Game game)
	{
		this.game = game;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		game.onTouch(v,event);
		return true;
	}

}
