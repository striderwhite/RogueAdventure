package com.game.rogueadventure;

import com.example.rogueadventure.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity 
{
	World world;
	Graphics graphics; // Graphics is an extension of the View class
	Player player;
	State state; //controls saving and loading
	Button zoomOut, zoomIn, up, down, left, right, load, newGame, slot1, slot2, slot3;
	MenuItem saveMenuItem;
	TextView debugTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_main_menu);	
		getViewPointers();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	public void save(MenuItem menu)
	{
		state = new State(world.tiles,player.x,player.y);
	    state.save();
	    Toast.makeText(this, "GAME SAVED to: " + state.saveFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
	}
	
	public void loadGame()
	{
	    state = new State();
	    state.load();
	    

	    player = new Player(state.playerX,state.playerY);
	    world = new World(state.tiles);
	    
	    setContentView(R.layout.activity_game);
	    getViewPointers();
	    
		graphics.setWorld(world);
		graphics.setPlayer(player);
		graphics.setOnTouchListener(new ViewPoint(this));
	}
	
	public void load(View view)
	{
		if(view == load)
		{
			setContentView(R.layout.load);
			slot1 = (Button) findViewById(R.id.load_slot_1);
			slot2 = (Button) findViewById(R.id.load_slot_2);
			slot3 = (Button) findViewById(R.id.load_slot_3);
		}
		if((Button) view == slot1)
		{
		    Toast.makeText(this, "LOADING SLOT1", Toast.LENGTH_LONG).show();
		    loadGame();
		    
		}
		if((Button) view == slot2)
        {
            Toast.makeText(this, "LOADING SLOT2", Toast.LENGTH_LONG).show();
        }
		if((Button) view == slot3)
        {
            Toast.makeText(this, "LOADING SLOT3", Toast.LENGTH_LONG).show();
        }
	}
	
	public void newGame(View view)
	{
		if(view == newGame)
		{
			world = new World();
			Toast.makeText(this, world.getRoomCount() + " Room(s) Created ", Toast.LENGTH_LONG).show();
			
			//creates the player and gives the object some instance pointers to the world and this class
			//the player is placed in the middle of the first room
		    player = new Player( world.rooms[0].centerX  , world.rooms[0].centerY);
		    player.setWorld(world);
		    player.setGame(this);
		     
		    //adds the player to the world
		    world.tiles[player.x][player.y].type = Tiles.PLAYER;
			
		    //set the content view
			setContentView(R.layout.activity_game);
            getViewPointers();
		    
		    //gives the graphics component instance pointers
			graphics.setWorld(world);
			graphics.setPlayer(player);
			graphics.setOnTouchListener(new ViewPoint(this));
			
		}
	}
	
	public void zoom(View view) //called by onclick
	{
	    if(view == zoomOut)
	    {
	        world.tileSize--;
	        graphics.invalidate();
	    }
	    
        if(view == zoomIn)
        {
            world.tileSize++;
            graphics.invalidate();
        }	    
	    
	}
	
	public void move(View view)//called by onclick
	{
	    if(view == up)
	    {
	        player.move(player.NORTH);
	    }
	    
        if(view == down)
        {
            player.move(player.SOUTH);
        }
        
        if(view == left)
        {
            player.move(player.WEST);
        } 
        
        if(view == right)
        {
            player.move(player.EAST);
        }    
	}
	
	
	//helper method to get pointers for UI
    public void getViewPointers()
    {
        zoomOut = (Button) findViewById(R.id.zoom_out);
        zoomIn = (Button) findViewById (R.id.zoom_in);
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);        
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right); 
        debugTextView = (TextView) findViewById(R.id.debugTextView);  
        graphics = (Graphics) findViewById(R.id.graphics); 
        load = (Button) findViewById(R.id.loadBtn);
        newGame = (Button) findViewById(R.id.newGameBtn);
        saveMenuItem = (MenuItem) findViewById(R.id.saveMenuItem);
    }
    
    
    public void setDebugText(String text)
    {
        debugTextView.setText(text);
    }
}
