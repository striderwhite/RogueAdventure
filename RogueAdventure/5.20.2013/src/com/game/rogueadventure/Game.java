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
	Button zoomOut, zoomIn, up, down, left, right, attack, load, newGame, slot1, slot2, slot3;
	MenuItem saveSlot1, saveSlot2, saveSlot3;
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
	
	/*@Override
	public void onBackPressed()
	{
	    if(findViewById(android.R.id.content) == findViewById(R.layout.activity_game))
	    {
	        
	    }
	}*/
	
	public void save(MenuItem menu)
	{
	    Tile[][] tilesToSave = world.tiles;
	    tilesToSave[player.x][player.y].setType(player.typeBeforeEntityMoved); //this makes sure to remove the player from the world before saving (when loading a new player object is built)
		
		if(menu.getTitle().equals("Slot 1"))
		{
	        state = new State(tilesToSave,player.x,player.y,1);
		}
		
		if(menu.getTitle().equals("Slot 2"))
        {
		      state = new State(tilesToSave,player.x,player.y,2);
        }
      
		if(menu.getTitle().equals("Slot 3"))
        {
		      state = new State(tilesToSave,player.x,player.y,3);
        }
		
		state.save();
	    Toast.makeText(this, "GAME SAVED to: " + state.saveFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
	}
	

	//intercepts the load call from the button
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
		    Toast.makeText(this, "LOADING SLOT 1", Toast.LENGTH_LONG).show();
		    load(1);
		    
		}
		if((Button) view == slot2)
        {
            Toast.makeText(this, "LOADING SLOT 2", Toast.LENGTH_LONG).show();
            load(2);
        }
		if((Button) view == slot3)
        {
            Toast.makeText(this, "LOADING SLOT 3", Toast.LENGTH_LONG).show();
            load(3);
        }
	}

    public void load(int slot)
    {
        state = new State(slot);
        state.load();
        world = new World(state.tiles);
        player = new Player(state.playerX,state.playerY,world,this,Tile_Type.PLAYER);
        setContentView(R.layout.activity_game);
        getViewPointers();
        graphics.setWorld(world);
        graphics.setPlayer(player);
        graphics.setOnTouchListener(new ViewPoint(this));
    }	
	
	public void newGame(View view)
	{
		if(view == newGame)
		{
			world = new World();
			Toast.makeText(this, world.getRoomCount() + " Room(s) Created ", Toast.LENGTH_LONG).show();
		    player = new Player( world.rooms[0].centerX  , world.rooms[0].centerY,world,this,Tile_Type.PLAYER);
			setContentView(R.layout.activity_game);
            getViewPointers(); //init graphics
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
	
	public void attack(View view)
	{
		combat();
	}
	
	public void move(View view)//called by onclick
	{	
		combat();
	    if(view == up)
	    {
	        player.act(Direction.NORTH);
	    }
	    
        if(view == down)
        {
            player.act(Direction.SOUTH);
        }
        
        if(view == left)
        {
            player.act(Direction.WEST);
        } 
        
        if(view == right)
        {
            player.act(Direction.EAST);
        }    
	}
	
	
	private void combat() 
	{
		/* 	if enemies are in range of the player init combat sequence, else tell the enemies to move.
		 * 	combat sequence is:
		 * 	(1)	let player attack the enemy then the enemy attack the player. The attack method handles heath and mana inside.
		 * 	(2)	check their health (see if anything died)
		 * 	(3)	if the player died end the game.
		 * 	(4)	if an enemy died follow its death sequence
		 * 
		 * 	death sequence is:
		 * 	(1) extract its experience points for the player 
		 * 	(2) tell the world to remove it (call world.remove(<Enemy>)), this also sets the world tile it was on to 'typeBeforeEntityMoved'
		 */
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
        graphics = (Graphics) findViewById(R.id.graphics); 
        load = (Button) findViewById(R.id.loadBtn);
        newGame = (Button) findViewById(R.id.newGameBtn);
        saveSlot1 = (MenuItem) findViewById(R.id.saveSlot1);
        saveSlot2 = (MenuItem) findViewById(R.id.saveSlot2);
        saveSlot3 = (MenuItem) findViewById(R.id.saveSlot3);
        attack = (Button) findViewById(R.id.attackBtn);
    }
}
