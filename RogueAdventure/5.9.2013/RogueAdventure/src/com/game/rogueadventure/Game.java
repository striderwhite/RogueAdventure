package com.game.rogueadventure;

import com.example.rogueadventure.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity 
{
	World world;
	Graphics graphics; // Graphics is an extention of the View class
	Player player;
	//GameLoader loader;
	
	//Buttons
	Button zoomOut, zoomIn, up, down, left, right, load, newGame;
	
	//text views
	TextView debugTextView;
	
	//touch input variables
    float pointerOneInitalX = 0;
    float pointerOneInitalY = 0;
    
	//viewpoint variables
	int height;
	int width;
	
    
    //when the activity is created (this the first method that is called)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_main_menu);	
		//get pointers the ui buttons / graphics components
        load = (Button) findViewById(R.id.loadBtn);
        newGame = (Button) findViewById(R.id.newGameBtn);
	}
	
	



	//when the options menu is created 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	// Screen touch input (called from MyOnTouchlistener)
	public void onTouch(View view, MotionEvent ev)
	{
		if(view == graphics)
		{

            
            switch(ev.getAction())
		        {        
		            case(MotionEvent.ACTION_DOWN):
		            {
		                pointerOneInitalX =  ev.getX();
		                pointerOneInitalY =  ev.getY();
		                
		            }
		            case(MotionEvent.ACTION_MOVE):
		            {
		                //this simulates a camera, by dragging the screen the viewpoint is adjusted
		                graphics.viewPoint.x =  (int) ( graphics.viewPoint.x + ( ( ev.getX() - pointerOneInitalX ) /24 ) ); // 24 for smoothing
		                graphics.viewPoint.y =  (int) ( graphics.viewPoint.y + ( ( ev.getY() - pointerOneInitalY ) /24 ) );
		                graphics.invalidate(); // force redraw
		            }   

		        }

		}
	}
	
	
	//these methods are for for button input called by onClick ( attribute in res/layout/activity_game )
	
	public void load(View view)
	{
		if(view == load)
		{
			
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
		    world.add(player,player.x,player.y);
		    
		    //saves the world configuration
		    //loader.save(world.getStateConfiguration());
			
		    //set the content view
			setContentView(R.layout.activity_game);
    
			//get all the pointers
	        zoomOut = (Button) findViewById(R.id.zoom_out);
	        zoomIn = (Button) findViewById (R.id.zoom_in);
	        up = (Button) findViewById(R.id.up);
	        down = (Button) findViewById(R.id.down);        
	        left = (Button) findViewById(R.id.left);
	        right = (Button) findViewById(R.id.right); 
	        debugTextView = (TextView) findViewById(R.id.debugTextView);  
	        graphics = (Graphics) findViewById(R.id.graphics); 
		    
		    //gives the graphics component instance pointers
			graphics.setWorld(world);
			graphics.setPlayer(player);
			graphics.setOnTouchListener(new MyOnTouchListener(this));
			
		}
	}
	
	public void zoomOut(View view)
	{
	    if(view == zoomOut)
	    {
	        world.tileSize--;
	        graphics.invalidate();
	    }
	}
	
	public void zoomIn(View view)
	{
	    if(view == zoomIn)
	    {
	        world.tileSize++;
	        graphics.invalidate();
	    }
	}
	
	public void moveUp(View view)
	{
	    if(view == up)
	    {
	        player.move(player.NORTH);
	    }
	}
	
    public void moveDown(View view)
    {
        if(view == down)
        {
            player.move(player.SOUTH);
        }       
    }	

    public void moveLeft(View view)
    {
        if(view == left)
        {
            player.move(player.WEST);
        }       
    } 
    
    public void moveRight(View view)
    {
        if(view == right)
        {
            player.move(player.EAST);
        }      
    }
    
    public void setDebugText(String text)
    {
        debugTextView.setText(text);
    }
}
