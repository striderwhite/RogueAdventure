package com.game.rogueadventure;

import com.example.rogueadventure.R;

import java.util.Random;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity 
{
	World world;
	Graphics graphics; // Graphics is an extension of the View class
	Player player;
	State state; //controls saving and loading
	Button zoomOut, zoomIn, up, down, left, right, upLeft, downLeft, upRight, downRight, attack, load, newGame, slot1, slot2, slot3;
	MenuItem saveSlot1, saveSlot2, saveSlot3;
	EventLog eventLogTextView;
	ProgressBar healthBar, expBar;
	TextView vLevel, vDex, vInt, vStr;
	int floor;
	
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
	    getViewPointers();
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
        player = new Player(state.playerX,state.playerY,world,Tile_Type.PLAYER);
        setContentView(R.layout.activity_game);
        init();
    }	


	public void newGame(View view)
	{
	    getViewPointers();
		if(view == newGame)
		{
			world = new World();
			Toast.makeText(this, world.getRoomCount() + " Room(s) Created ", Toast.LENGTH_LONG).show();
		    player = new Player( world.rooms[0].centerX  , world.rooms[0].centerY,world,Tile_Type.PLAYER);
			setContentView(R.layout.activity_game);
			init();
		}
	}
	
	private void init()
	{
        getViewPointers();
        world.setPlayer(player);
        world.setGraphics(graphics);
        graphics.setWorld(world);
        graphics.setPlayer(player);
        graphics.setOnTouchListener(new ViewPoint(this));
        healthBar.setProgress(healthBar.getMax());
        healthBar.setMax(player.maxHealth);
        expBar.setMax(player.expBeforeNextLevel);
        healthBar.setProgress(player.experience);
        floor = 1;
        centerCamera();
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
        //combat();
        centerCamera();
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
        
        if(view == upLeft)
        {
            player.act(Direction.NORTHWEST);
        }
        
        if(view == downLeft)
        {
            player.act(Direction.SOUTHWEST);
        }
        
        if(view == upRight)
        {
            player.act(Direction.NORTHEAST);
        }
        
        if(view == downRight)
        {
            player.act(Direction.SOUTHEAST);
        }
        
        for( Enemy e : world.enemies )
        {
            e.move();
        }
        
        //SMALL CHANCE THAT PLAYER GETS SOME HP FOR MOVING
        Random gen = new Random();
        int chance = gen.nextInt(100);
        if(chance <= 10) //10% chance
        {
            int giveHealth = gen.nextInt(3) + 1;
            player.health += giveHealth;
            eventLogTextView.logText("info", "player gained " + giveHealth + " HP");
            player.health += giveHealth;
            healthBar.setProgress(player.health);
        }
        
        if(isOneStaircase(player))
        {
            floor++;
            eventLogTextView.logText("info", " You have decended to floor " + floor);
            createNewFloor();
        }
          
        graphics.invalidate();
	}
	
	
	private void createNewFloor() 
	{
	    world = new World(world);
        Toast.makeText(this, "You have decended to floor " + floor , Toast.LENGTH_LONG).show();
        player = new Player( world.rooms[0].centerX  , world.rooms[0].centerY,world,Tile_Type.PLAYER, player);
        graphics.setWorld(world);
        graphics.setPlayer(player);
        centerCamera();
        getViewPointers();
    }

    private boolean isOneStaircase(Player player) 
	{
        if(player.typeBeforeEntityMoved == Tile_Type.STAIRS)
        {
            return true;
        }
        else
        {
            return false;
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
	    
	    Enemy[] enemies =  new Enemy[world.enemies.size()];
	    enemies = world.enemies.toArray(enemies);
	    
	    //this iterates over a separate array than the one in world.enemies.
	    for(Enemy e : enemies)
	    {
	        if(player.isAdjacent(e)) //if the X or Y position differ by one cell (in other words they are beside each other on the grid)
	        {
	            eventLogTextView.logText("info  " , "engaged in combat");
	            player.attack(e);
	            eventLogTextView.logText("combat" , "Player attacked enemy for " + player.damage + " damage ");
	            
	            if(e.isDead()) 
	            {
	                kill(world.enemies.get(world.enemies.indexOf(e))); //this accesses the array within the world class (preventing concurrent mod exception)
	                continue;
	            }
	            else
	            {
	                e.attack(player);
	                healthBar.setProgress(player.health);
	                eventLogTextView.logText("combat", "Enemy attacked Player for " + e.damage + " damage ");
	            }
	            
	            if(player.isDead())
	            {
	                setContentView(R.layout.start_main_menu);
	                Toast.makeText(this, "YOU DIED! :(" , Toast.LENGTH_LONG).show();
	                destroyGame();
	                return;
	            }
	        }
	    }
	    graphics.invalidate();
	}

	private void destroyGame() 
	{
       world = null;
       player = null;
       graphics = null; 
    } 

    private void kill(Enemy e)
	{ 
        int healthToGain = Math.abs(e.health/3);
	    eventLogTextView.logText("info" , "Player killed enemy and gained " + e.experience + " exp ");
	    eventLogTextView.logText("info" , "Player gained " + healthToGain + " HP from enemy");
	    //returns true if player leveled up
	    if(player.setExperience(player.experience + e.experience))
	    {
	        eventLogTextView.logText("info  " , "Level up! Now level " + player.level);
	        eventLogTextView.logText("info  " , player.expBeforeNextLevel + " exp needed until level " + (player.level+1));
	    }
        player.setHealth(player.health + healthToGain);
        expBar.setProgress(player.experience);
        world.remove(e);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void centerCamera()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        
        int offsetX = 80;
        int offsetY = 10;
        
        int screenWidth = size.x;
        int screenHeight = size.y;
        int playerDistanceFromOriginX = (world.tileSize * player.x) + ( (graphics.TILE_SPACE) * player.x);
        int playerDistanceFromOriginY = (world.tileSize * player.y) + ( (graphics.TILE_SPACE) * player.y);
        int distanceFromCenterScreenX = (screenWidth/2);
        int distanceFromCenterScreenY = (screenHeight/2);
        int shiftX = playerDistanceFromOriginX - distanceFromCenterScreenX;
        int shiftY = playerDistanceFromOriginY - distanceFromCenterScreenY; 
        graphics.viewPoint.x = -(shiftX + offsetX);
        graphics.viewPoint.y = -(shiftY + offsetY);
        
        
    }

    //helper method to get pointers for UI
    public void getViewPointers()
    {
        //zoomOut = (Button) findViewById(R.id.zoom_out);
        //zoomIn = (Button) findViewById (R.id.zoom_in);
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);        
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right); 
        
        upLeft = (Button) findViewById(R.id.upLeft);
        downLeft = (Button) findViewById(R.id.downLeft);
        upRight = (Button) findViewById(R.id.upRight);
        downRight = (Button) findViewById(R.id.downRight);
        
        graphics = (Graphics) findViewById(R.id.graphics); 
        load = (Button) findViewById(R.id.loadBtn);
        newGame = (Button) findViewById(R.id.newGameBtn);
        saveSlot1 = (MenuItem) findViewById(R.id.saveSlot1);
        saveSlot2 = (MenuItem) findViewById(R.id.saveSlot2);
        saveSlot3 = (MenuItem) findViewById(R.id.saveSlot3);
        attack = (Button) findViewById(R.id.attackBtn);
        eventLogTextView = (EventLog) findViewById(R.id.EventLogTextView);
        healthBar = (ProgressBar) findViewById(R.id.heathBar);
        expBar = (ProgressBar) findViewById(R.id.expBar);
    }
}
