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
    Tile roomTiles[][];
    Random gen;

    
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
	    
        roomTiles = new Tile[width][height];   
        rectangle = new Rect( 
        		startPointX , 
        		startPointY , 
        		startPointX + width , 
        		startPointY + height );
        
    	centerX = (startPointX) + (width/2);
    	centerY = (startPointY) + (height/2);
        
        fillRoom(); //assigns tiles to the array
        makeWalls(); //assigns the 'wall' attribute around the area of the array
        makeDoor();
    }
    
    
    private void fillRoom()
    {
        for(int x = 0 ; x < width ; x++ )
        {
            for(int y = 0 ; y < height ; y++)
            {
                roomTiles[x][y] = new Tile();
            }
        }       
    }
    
    private void makeWalls()
    {
        for(int x = 0 ; x < width ; x++ )
        {
            for(int y = 0 ; y < height ; y++)
            {
                //if we are on the top or bottom of the grid make it a wall
                if( y == 0 || y == height-1 )
                {
                    roomTiles[x][y].type = Tiles.WALL;
                }
                else
                {
                    if( x == 0 || x == width-1 )
                    {
                        roomTiles[x][y].type = Tiles.WALL;
                    }
                }
            }
        } 
    }
    
    private void makeDoor()
    {
    	/*
    	int side = gen.nextInt(3);
    	if(side == 0)
    	{
    		roomTiles[width/2 -1][height-1].wall = false;
    	}
    	if(side == 1)
    	{
    		roomTiles[width/2 - 1][0].wall = false;
    	}
    	if(side == 2)
    	{
    		roomTiles[0][height/2 - 1].wall = false;
    	}
    	if(side == 3)
    	{
    		roomTiles[width - 1][height/2 - 1].wall = false;
    	}*/
    }

}
