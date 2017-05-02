package com.game.rogueadventure;

import com.example.rogueadventure.R;

import java.util.ArrayList;
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
	ArrayList<World> worlds;
	Graphics graphics; // Graphics is an extension of the View class
	Player player;
	State state; //controls saving and loading
	Button zoomOut, zoomIn, up, down, left, right, upLeft, downLeft, upRight, downRight, attack, load, newGame, slot1, slot2, slot3, inventory, heal, fireball;
	MenuItem saveSlot1, saveSlot2, saveSlot3;
	EventLog eventLogTextView;
	ProgressBar healthBar, expBar, manaBar;
	TextView vLevel, vDex, vInt, vStr, buff, healthView, expView, manaView;
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
	    Block[][] tilesToSave = world.blocks;
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
        world = new World(state.blocks);
        player = new Player(state.playerX,state.playerY,world,Block_Type.PLAYER);
        setContentView(R.layout.activity_game);
        init();
    }	

	public void newGame(View view)
	{
	    worlds = new ArrayList<World>(0);
	    getViewPointers();
		if(view == newGame)
		{
			world = new World();
			worlds.add(world);
			Toast.makeText(this, world.getRoomCount() + " Room(s) Created ", Toast.LENGTH_LONG).show();
		    player = new Player( world.rooms[0].centerX  , world.rooms[0].centerY,world,Block_Type.PLAYER);
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

        /* * * * * * * * 
         * TEXT VIEWS
         * * * * * * * *
         */
        healthView.setText("Health " + player.health + "/" + player.health_threshold);
        manaView.setText("Mana " + player.mana + "/" + player.mana_threshold);
        expView.setText("Exp 0/" + player.experience_threshold);
        
        if(player.typeBeforeEntityMoved == Block_Type.WATER ||  player.typeBeforeEntityMoved == Block_Type.GRASS ||  player.typeBeforeEntityMoved == Block_Type.ICE)
        {
            buff.setText("On " + player.typeBeforeEntityMoved + "\nBuff is defensive");
        }
        else
        {
            buff.setText("On " + player.typeBeforeEntityMoved + "\nBuff is offensive");
        }
        //buff.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        
        
        /* * * * * * * * 
         * PROGRESSBAR VIEWS
         * * * * * * * *
         */
        
        healthBar.setMax(player.health_threshold);
        manaBar.setMax(player.mana_threshold);
        expBar.setMax(player.experience_threshold);
        
        healthBar.setProgress(player.health_threshold);
        manaBar.setProgress(player.mana_threshold);
        
        
        
        floor = 1;
        centerCamera();
	}
	
	
	public void fireball(View view)
	{
	    int fireballCost = 20;
	    if(player.mana >= fireballCost)
	    {
	       Enemy[] enemies =  new Enemy[world.enemies.size()];
	       enemies = world.enemies.toArray(enemies);
	       int fireballDamage = (player.level * player.intelligence) + 20; 
	        //this iterates over a separate array than the one in world.enemies.
	        for(Enemy e : enemies)
	        {
	            if(player.isAdjacent(e)) //if the X or Y position differ by one cell (in other words they are beside each other on the grid)
	            {
	                e.health = e.health - fireballDamage;
	                eventLogTextView.logText("Magic","Player used fireball and it did " + fireballDamage + " damage");
	                if(player.mana <= fireballCost)
	                {
	                    player.mana = 0;
	                }
	                else
	                {
	                    player.mana = player.mana - fireballCost;   
	                }
	                manaView.setText("Mana " + player.mana + "/" + player.mana_threshold);
	                manaBar.setProgress(player.mana);
	                if(e.isDead()) 
	                {
	                    kill(world.enemies.get(world.enemies.indexOf(e))); //this accesses the array within the world class (preventing concurrent mod exception)
	                    continue;
	                }
	                /*
	                 * PLAYER GETS AWAY WITH ATTACKING WITHOUT ENEMY ATTACKING ACK
	                 * else
	                {
	                    e.attack(player,eventLogTextView);
	                    healthView.setText("Health " + player.health + "/" + player.maxHealth);
	                    healthBar.setProgress(player.health);
	                }*/
	            }
	        }
	        graphics.invalidate(); 
	    }
	    else
	    {
	        eventLogTextView.logText("Magic", "Not enough mana to use fireball");
	    }
	}
	
	public void heal(View view)
	{
	    int healCost = 20;
	    Random gen = new Random();
	    if(player.mana >= healCost)
	    {
	        if(player.mana <= healCost)
	        {
	            player.mana = 0;
	        }
	        else
	        {
	            player.mana = player.mana - healCost;
	        }
	        
	        int amountToGetHealed = gen.nextInt(25) + 25; //between 25 and 50
	        player.setHealth(player.health + amountToGetHealed);
	        eventLogTextView.logText("Magic", "Used heal and gained " + amountToGetHealed + " HP");
	        healthView.setText("Health " + player.health + "/" + player.health_threshold);
	        manaView.setText("Mana " + player.mana + "/" + player.mana_threshold);
            manaBar.setProgress(player.mana);
            healthBar.setProgress(player.health);
	    }
	    else
	    {
	           eventLogTextView.logText("Magic", "Not enough mana to use heal");
	    }
	}
	
	public void attack(View view)
	{
		combat();
	}
	
	public void move(View view)//called by onclick
	{	
        combat();
        if(player.isDead())
        {
            return;
        }
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
            player.setHealth(player.health + giveHealth);
            eventLogTextView.logText("info", "player gained " + giveHealth + " HP");
            healthBar.setProgress(player.health);
            healthView.setText("Health " + player.health + "/" + player.health_threshold);
        }
        
        
        //SMALL CHANCE THAT PLAYER GETS SOME MANA FOR MOVING
        chance = gen.nextInt(100);
        if(chance <= 10) //10% chance
        {
            int giveMana = gen.nextInt(3) + 1;
            player.setMana(player.mana + giveMana);
            eventLogTextView.logText("info", "player gained " + giveMana + " MANA");
            manaBar.setProgress(player.mana);
            manaView.setText("Mana " + player.mana + "/" + player.mana_threshold);
        }
        
        
        if(player.isOnStaircase())
        {
            floor++;
            eventLogTextView.logText("info", " You have decended to floor " + floor);
            createNewFloor();
            return;
        }
        if(!eventLogTextView.log.isEmpty())
        {
            eventLogTextView.log.remove(eventLogTextView.log.size() -1);
            eventLogTextView.setText( eventLogTextView.assembleMessage()); 
        }
        
        if(player.typeBeforeEntityMoved == Block_Type.WATER ||  player.typeBeforeEntityMoved == Block_Type.GRASS ||  player.typeBeforeEntityMoved == Block_Type.ICE)
        {
            buff.setText("On " + player.typeBeforeEntityMoved +  "\nBuff is defensive");
        }
        else
        {
            buff.setText("On " + player.typeBeforeEntityMoved + "\nBuff is offensive");
        }
 
        centerCamera();
        graphics.invalidate();
	}
	
	private void createNewFloor() 
	{
        getViewPointers();
	    world = new World(world);
	    worlds.add(world);
        Toast.makeText(this, "You have decended to floor " + floor , Toast.LENGTH_LONG).show();
        player = new Player( world.rooms[0].centerX  , world.rooms[0].centerY,world,Block_Type.PLAYER, player);
        graphics.setWorld(world);
        graphics.setPlayer(player);
        world.setGraphics(graphics);
        centerCamera();
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
	            player.attack(e,eventLogTextView);
	            if(e.isDead()) 
	            {
	                kill(world.enemies.get(world.enemies.indexOf(e))); //this accesses the array within the world class (preventing concurrent mod exception)
	                continue;
	            }
	            else
	            {
	                e.attack(player,eventLogTextView);
	                healthView.setText("Health " + player.health + "/" + player.health_threshold);
	                healthBar.setProgress(player.health);
	            }
	            
	            if(player.isDead())
	            {
	                setContentView(R.layout.start_main_menu);
	                Toast.makeText(this, "YOU DIED! :(" , Toast.LENGTH_LONG).show();
	                return;
	            }
	        }
	    }
	    graphics.invalidate();
	}


    private void kill(Enemy e)
	{ 
        if(player.setExperience(player.experience + e.experience))
        {
            eventLogTextView.logText("info  " , "Level up! Now level " + player.level + " now " + player.experience_threshold + " exp needed until level " + (player.level+1));
        }
        int healthToGain = Math.abs(e.health/3);
	    eventLogTextView.logText("info" , "Player killed Enemy and gained " + e.experience + " exp " + " and " + healthToGain + " HP ");

        player.setHealth(player.health + healthToGain);
        expBar.setProgress(player.experience);
        expView.setText("Exp " + player.experience + "/" + player.experience_threshold);
        healthView.setText("Health " + player.health + "/" + player.health_threshold);
        world.remove(e);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void centerCamera()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        
        int screenWidth = size.x;
        int screenHeight = size.y;
        
        int offsetX = 80 + graphics.block_size;
        int offsetY = 10 + graphics.block_size;
        
        int playerDistanceFromOriginX = (graphics.block_size * player.x) + ( (graphics.TILE_SPACING) * player.x);
        int playerDistanceFromOriginY = (graphics.block_size * player.y) + ( (graphics.TILE_SPACING) * player.y);
        int distanceFromCenterScreenX = (screenWidth/2);
        int distanceFromCenterScreenY = (screenHeight/2);
        int shiftX = playerDistanceFromOriginX - distanceFromCenterScreenX;
        int shiftY = playerDistanceFromOriginY - distanceFromCenterScreenY; 
        graphics.view_point_x = -(shiftX + offsetX);
        graphics.view_point_y = -(shiftY + offsetY);
        
        
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
        inventory = (Button) findViewById(R.id.InventoryBtn);
        heal = (Button) findViewById(R.id.HealMagicBtn);
        fireball = (Button) findViewById(R.id.MagicFireballBtn);
        
        eventLogTextView = (EventLog) findViewById(R.id.EventLogTextView);
        healthBar = (ProgressBar) findViewById(R.id.heathBar);
        expBar = (ProgressBar) findViewById(R.id.expBar);
        manaBar = (ProgressBar) findViewById(R.id.manaBar);
        
        buff = (TextView) findViewById(R.id.buffTextView);
        healthView = (TextView) findViewById(R.id.healthTextView);
        expView = (TextView) findViewById(R.id.expTextView);
        manaView = (TextView) findViewById(R.id.manaTextview);
    }
}
