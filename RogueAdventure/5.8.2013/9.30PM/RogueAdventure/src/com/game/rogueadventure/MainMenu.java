package com.game.rogueadventure;

import com.example.rogueadventure.R;
import com.example.rogueadventure.R.layout;
import com.example.rogueadventure.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		Intent game = new Intent(this , Game.class);
		startActivity(game);

	}
}
