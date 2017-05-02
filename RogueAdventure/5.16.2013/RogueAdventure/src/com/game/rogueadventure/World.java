/* This class contains the 2d array of tiles and the random generation code
 */

package com.game.rogueadventure;
import java.util.ArrayList;
import java.util.Random;

import android.graphics.Rect;


public class World
{
    protected int width = 30;
	protected int height = 30;
	protected int tileSize = 25;
    protected int roomsToMake;
	protected Tile[][] tiles;
	protected Room[] rooms;
	protected ArrayList<Enemy> enemies;

	World()
	{
		buildWorld(); //creates the array of tiles and adds some boundaries
		createRooms(); //generates the rooms 
		addRoomsToTiles(); //converts rooms to the tiles array
		connectRooms();
		populateWorld(); //adds things to the world
	}
	
	public World(Tile[][] tiles) 
	{
		this.tiles = tiles;
	}

	private void buildWorld()
	{
		tiles = new Tile[width][height];
		for(int x = 0 ; x < width ; x++)
		{
			for(int y = 0 ; y < height ; y++)
			{
				tiles[x][y] = new Tile(Tiles.WALL);
			}
		}
		for(int x = 0 ; x < width ; x++ ) //creates walls around room
		{
			for(int y = 0 ; y < height ; y++)
			{
				if( y == 0 || y == height-1 ) //if we are on the top or bottom of the grid make it a wall
				{
					tiles[x][y].type = Tiles.WALL;
				}
				else
				{
					if( x == 0 || x == width-1 )
					{
						tiles[x][y].type = Tiles.WALL;
					}
				}
			}
		}
		
	}
	
	private void createRooms()
	{
		//figure out how many rooms to make and create an array of that size
		Random gen = new Random(); //number generator
	    roomsToMake = gen.nextInt(7) + 3; //make between 4 and 7 rooms  
		rooms = new Room[roomsToMake]; //create the array
		
		//##################Create first room  ###################
		//we need at least one room defined before making the other ones 
		//add the first room to the array
		rooms[0] = new Room(this);
		
		//############### create the rest of the rooms ###################
		
		//these variables are fail safes in case the rooms being generated could not fit within the game area (to prevent infinite loop)
		int attempt = 0; //amount of times tried
		int maxAttempts = 30000; //max amount of times before not trying to make any more rooms
		
		makingRooms: // (the fail safe)
		for(int roomsMade = 1 ; roomsMade < roomsToMake ; roomsMade++)
		{
			//create a new room check and if it overlaps with any rooms in the array, make a new room until it doesnt
		    Room newRoom = new Room(this);	      	
		    
		    //note: potential infinite loop
		    while(isOverlaping(newRoom))
		    {
		    		newRoom = new Room(this); //make new room
		    		if(attempt > maxAttempts){ break makingRooms; } //if reached max attempts break out of making rooms
		    		else{ attempt++;} //
		    }
		    
		    rooms[roomsMade] = newRoom;  //if the room wasnt overlaping then add it to the rooms
		}

	}
	
	//reads the room tiles and puts them into the world tiles
	private void addRoomsToTiles()
	{
		for(int currentRoom = 0 ; currentRoom < getRoomCount() ; currentRoom++)
		{
			Room room = rooms[currentRoom];
			int roomOriginX = room.startPointX;
			int roomOriginY = room.startPointY;
			int roomMaxX =  room.width;
			int roomMaxY =  room.height;
			int roomXIndex = 0;//index of of room tile being accessed from room object
			int roomYIndex = 0;
			
			int worldX = roomOriginX;
			int worldY = roomOriginY;
			
			while(roomXIndex < roomMaxX)
			{
			    roomYIndex = 0;
			    while(roomYIndex < roomMaxY)
			    {
			        worldX = roomOriginX + roomXIndex;
			        worldY = roomOriginY + roomYIndex;
			        tiles[worldX][worldY] = room.roomTiles[roomXIndex][roomYIndex];
			        roomYIndex++;
			    }
	             roomXIndex++;
			}	
		}
	}
	
	//this creates "hallways" by finding all the tiles between two rooms and setting those tiles to be traversable
	private void connectRooms()
	{
		for(int room = 0 ; room < getRoomCount() - 1 ; room++) //-1 because i look ahead one cell in the array when comparing
		{
			int diffx = rooms[room+1].centerX - rooms[room].centerX; 
			int diffy = rooms[room+1].centerY - rooms[room].centerY;
			int xTile, yTile;
			
			for(int x = 0 ; x < Math.abs(diffx) ; x++)
			{
				if(diffx > 0)
				{
					xTile = rooms[room].centerX + x;
					yTile = rooms[room].centerY;
				}
				else
				{
					xTile = rooms[room].centerX - x;
					yTile = rooms[room].centerY;
				}
				
				tiles[xTile][yTile] = new Tile();
			 }

			for(int y = 0 ; y < Math.abs(diffy) ; y++ )
			{
			    if(diffy > 0)
			    {
			    	xTile = rooms[room+1].centerX;
			    	yTile = rooms[room].centerY + y;
			    }
			    else
			    {
			    	xTile = rooms[room+1].centerX;
			    	yTile = rooms[room].centerY - y;
			    }
			    
				tiles[xTile][yTile] = new Tile();
			}	
		} 
	}
	
	private boolean isOverlaping(Room room)
	{
		for(int x = 0 ; x < getRoomCount() ; x++)
		{
			if(Rect.intersects(room.rectangle , rooms[x].rectangle))
			{
				return true;
			}
		}
		return false;
	}
	
	//returns the amount of not null rooms in the rooms array
	protected int getRoomCount()
	{
		int amount=0;
		for(int x = 0 ; x < rooms.length ; x++)
		{
			if(rooms[x] != null)
			{
				amount++;
			}
		}
		return amount;
	}

	private void populateWorld()
	{
	    enemies = new ArrayList<Enemy>(5);
	}
	
	protected void update()
	{
	    
	}
	
	protected String[] getWorldConfiguration()
	{
		return null; //new String(width.toString() ,height,tileSize,roomsToMake)
	}

}
