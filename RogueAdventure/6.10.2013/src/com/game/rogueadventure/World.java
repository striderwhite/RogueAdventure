/* This class contains the 2d array of tiles and the random generation code
 */

package com.game.rogueadventure;
import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;
import android.graphics.Rect;


public class World
{
    protected int width = 30;
	protected int height = 30;
    protected int roomsToMake;
	protected Block[][] blocks;
	protected Room[] rooms;
	protected ArrayList<Enemy> enemies;
	protected Player player;
	protected Graphics graphics; //Reference to graphics component
	protected Point spawnPoint, downstairsPoint, upstairsPoint;
	boolean visited;
	
	//Adaptive variables
	int maxEnemies, minEnemies;

	/*
	 * 
	 * first time the world is built
	 * 
	 * assigns the worlds minimum and maximim enemies
	 * 
	 * calls a series of methods that contstruct the world:
	 * 
	 * (1) buildWorld()
	 * (2) createRooms()
	 * (3) addRoomsToTiles()
	 * (4) placeStairCase();
	 * (5) popularWorld();
	*/
	
	World()
	{
	    visited = false;
	    minEnemies = 2;
	    maxEnemies = 5;
		buildWorld(); //creates the array of tiles and adds some boundaries
		createRooms(); //generates the rooms 
		addRoomsToTiles(); //converts rooms to the tiles array
		connectRooms();
	    placeStairCase();
		populateWorld(); //adds things to the world
		spawnPoint = new Point(rooms[0].centerX  , rooms[0].centerY);
	}
	
   World(Boolean createUpstairs)
    {
       visited = false;
        minEnemies = 2;
        maxEnemies = 5;
        buildWorld(); //creates the array of tiles and adds some boundaries
        createRooms(); //generates the rooms 
        addRoomsToTiles(); //converts rooms to the tiles array
        connectRooms();
        placeStairCase();
        populateWorld(); //adds things to the world
        spawnPoint = new Point(rooms[0].centerX  , rooms[0].centerY);
    }

	
	/*
	 * 
	 * creates a new world and assigns its paramaters realtive to the given world
	 * increases width and height
	 * increases min and max enemies
	 * places and up and down staircase, not just a down staircase
	 */
	World(World w)
	{
	    visited = false;
	    width = w.width + 1;
	    height = w.height +1;
        minEnemies += w.minEnemies++;
        maxEnemies += w.maxEnemies++;
        buildWorld(); //creates the array of tiles and adds some boundaries
        createRooms(); //generates the rooms 
        addRoomsToTiles(); //converts rooms to the tiles array
        connectRooms();
        placeStairCase();
        placeUpstairCase();
        populateWorldWithMoreDifficultEnemies(w); //adds things to the world	    
	}
	


    public World(Block[][] tiles) 
	{
		this.blocks = tiles;
	}

	private void buildWorld()
	{
		blocks = new Block[width][height];
		for(int x = 0 ; x < width ; x++)
		{
			for(int y = 0 ; y < height ; y++)
			{
				blocks[x][y] = new Block(x,y,Block_Type.WALL);
			}
		}
		for(int x = 0 ; x < width ; x++ ) //creates walls around room
		{
			for(int y = 0 ; y < height ; y++)
			{
				if( y == 0 || y == height-1 ) //if we are on the top or bottom of the grid make it a wall
				{
					blocks[x][y].type = Block_Type.WALL;
				}
				else
				{
					if( x == 0 || x == width-1 )
					{
						blocks[x][y].type = Block_Type.WALL;
					}
				}
			}
		}
		
	}
	
	private void createRooms()
	{
		//figure out how many rooms to make and create an array of that size
		Random gen = new Random(); //number generator
	    roomsToMake = gen.nextInt(15) + 15; //make between 4 and 7 rooms  
		rooms = new Room[roomsToMake]; //create the array
		
		//##################Create first room  ###################
		//we need at least one room defined before making the other ones 
		//add the first room to the array
		rooms[0] = new Room(this);
		
		//############### create the rest of the rooms ###################
		
		//these variables are fail safes in case the rooms being generated could not fit within the game area (to prevent infinite loop)
		int attempt = 0; //amount of times tried
		int maxAttempts = 1000; //max amount of times before not trying to make any more rooms
		
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
			        blocks[worldX][worldY] = room.roomTiles[roomXIndex][roomYIndex];
			        roomYIndex++;
			    }
	             roomXIndex++;
			}	
		}
	}
	
	//This creates "hallways" by connecting all the center tiles between two rooms with blocks. The block assigns itself with a random type 
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
				
				blocks[xTile][yTile] = new Block(xTile,yTile);
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
			    
				blocks[xTile][yTile] = new Block(xTile,yTile);
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
	    Random gen = new Random();
	    int enemiesToMake = gen.nextInt(maxEnemies) + minEnemies;
	    int enemiesMade = 0;
	    enemies = new ArrayList<Enemy>(enemiesToMake);
	    
	    while(enemiesMade < enemiesToMake)
	    {
	        int randX = gen.nextInt(width-1) + 1;
	        int randY = gen.nextInt(height-1) + 1;
	        while(!blocks[randX][randY].isTraversableToEnemy())
	        {
	            randX = gen.nextInt(width-1) + 1;
	            randY = gen.nextInt(height-1) + 1; 
	        }
            enemies.add(new Enemy(randX,randY,this,Block_Type.ENEMY));
            enemiesMade++;     
	    }
	    
	    for(Enemy e : enemies)
	    {
	        blocks[e.x][e.y].setType(e.type);
	    }
	    
	}
	
    private void populateWorldWithMoreDifficultEnemies(World w)
    {
        Random gen = new Random();
        int enemiesToMake = gen.nextInt(maxEnemies) + minEnemies;
        int enemiesMade = 0;
        ArrayList<Enemy> newEnemies = new ArrayList<Enemy>(enemiesToMake);
        
        while(enemiesMade < enemiesToMake)
        {
            int randX = gen.nextInt(width-1) + 1;
            int randY = gen.nextInt(height-1) + 1;
            while(!blocks[randX][randY].isTraversableToEnemy())
            {
                randX = gen.nextInt(width-1) + 1;
                randY = gen.nextInt(height-1) + 1; 
            }
            newEnemies.add(new Enemy(w.enemies.get(0), randX, randY));
            enemiesMade++;     
        }
        
        enemies = newEnemies;
        
        for(Enemy e : enemies)
        {
            blocks[e.x][e.y].setType(e.type);
        } 
    }
	
    private void placeStairCase()
   {
       blocks[rooms[getRoomCount()-1].centerX][rooms[getRoomCount()-1].centerY].setType(Block_Type.DOWNSTAIRS);
       downstairsPoint = new Point(rooms[getRoomCount()-1].centerX,rooms[getRoomCount()-1].centerY);
   }
   
    private void placeUpstairCase()
   {
      // blocks[rooms[getRoomCount()-1].centerX][rooms[0].centerY].setType(Block_Type.UPSTAIRS);
   }

	public Block getTileFacing(int x, int y, Direction direction) 
	{
		 switch(direction)
         {
			 case NORTH:
			 {
			     if(y-1 < 0)
			     {
			         return null;
			     }
		         else
		         {
		             return blocks[x][y-1];
		         }
			 }
			 case SOUTH:
			 {
			     if(y+1 > height-1)
			     {
			         return null;
			     }
			     else
			     {
				     return blocks[x][y+1]; 
			     }
			 }
			 case EAST:
			 {
			     if(x+1 > width-1)
			     {
			         return null;
			     }
			     else
			     {
			         return blocks[x+1][y];
			     }
			 }
			 case WEST:
			 {
			     if(x-1 < 0)
			     {
			         return null;
			     }
			     else
			     {
			         return blocks[x-1][y];
			     }
			 }
			case NORTHEAST:
			{
			    if(x+1 > width-1 || y-1 < 0)
			    {
			        return null;
			    }
			    else
			    {
			        return blocks[x+1][y-1];
			    }
			}
			case NORTHWEST:
			{
			    if(x-1 < 0 || y-1 < 0)
			    {
			        return null;
			    }
			    else
		        {
		            return blocks[x-1][y-1];
		        }
			}
			case SOUTHEAST:
			{
			    if(x+1 > width-1 || y+1 > height-1)
			    {
			        return null;
			    }
			    else
			    {
			        return blocks[x+1][y+1];
			    }
			}
			case SOUTHWEST:
			{
			    if(x-1 < 0 || y+1 > height)
			    {
			        return null;
			    }
			    else
			    {
			        return blocks[x-1][y+1];
			    }
			}
			default:
			{
				break;
			}
         }
		 return null;
	}

	public Block getBlock(int x, int y)  
	{
		return blocks[x][y];
	}

    public void remove(Enemy e) 
    {
        enemies.remove(e);
        getBlock(e.getX(),e.getY()).setType(e.typeBeforeEntityMoved);  
    }
    
    public void setPlayer(Player p)
    {
        player = p;
    }   
    public void setGraphics(Graphics graphics)
    {
        this.graphics = graphics;
    }

    public Direction getDirection(Entity player, Entity enemy)
    {
        if(player.x == enemy.x)
        {
            if(enemy.y > player.y)
            {
                return Direction.NORTH;
            }
            else
            {
                return Direction.SOUTH;
            }
        }
        if(player.y == enemy.y)
        {
            if(enemy.x > player.x)
            {
                return Direction.WEST;
            }
            else
            {
                return Direction.EAST;
            }
        }
        if(player.x > enemy.x)
        {
            if(player.y > enemy.y)
            {
                return Direction.SOUTHEAST;
            }
            if(player.y < enemy.y)
            {
                return Direction.NORTHEAST;
            }
        }
        if(player.x < enemy.x)
        {
            if(player.y > enemy.y)
            {
                return Direction.SOUTHWEST;
            }
            if(player.y < enemy.y)
            {
                return Direction.NORTHWEST;
            }
        }
        return null;
    }
}
