package com.game.rogueadventure;

import java.util.Random;

import android.graphics.Rect;

public class Room
{
	int width, height;
    int startPointX;
    int startPointY;
    int centerX;
    int centerY;
    
    Rect rectangle;
    World world;
    Block roomTiles[][];
    Random gen;

    /*
     * This is when the room is generated. it chooses its values randomly.
     * 
     */
    Room(World world)
    { 
    	this.world = world;
    	gen = new Random();
	    
		//the width and height is between 4 and 5
	    width = gen.nextInt(5) + 4; 
	    height = gen.nextInt(5) + 4; 
	    
	    //create starting point 
        startPointX = gen.nextInt(world.width - width);
        startPointY = gen.nextInt(world.height - height);
        
        while(startPointX == 0 || startPointX == world.width)
        {
            startPointX = gen.nextInt(world.width - width);
        }
	    
        while(startPointY == 0 || startPointY == world.height)
        {
            startPointY = gen.nextInt(world.height - height);
        }
        
        roomTiles = new Block[width][height];   
        rectangle = new Rect( 
        		startPointX , 
        		startPointY , 
        		startPointX + width , 
        		startPointY + height );
        
    	centerX = (startPointX) + (width/2);
    	centerY = (startPointY) + (height/2);
        
        fillRoom(); //assigns tiles to the array
        makeWalls(); //assigns the 'wall' attribute around the area of the array
    }
    
    
    private void fillRoom()
    {
        for(int x = 0 ; x < width ; x++ )
        {
            for(int y = 0 ; y < height ; y++)
            {
                roomTiles[x][y] = new Block(x,y);
            }
        }       
    }
    
    /*
     * this sets all the tiles around the edges of the array to be a wall
     */
    private void makeWalls()
    {
        for(int x = 0 ; x < width ; x++ )
        {
            for(int y = 0 ; y < height ; y++)
            {
                //if we are on the top or bottom of the grid make it a wall
                if( y == 0 || y == height-1 )
                {
                    roomTiles[x][y].type = Tile_Type.WALL;
                }
                else
                {
                    if( x == 0 || x == width-1 )
                    {
                        roomTiles[x][y].type = Tile_Type.WALL;
                    }
                }
            }
        } 
    }
}
