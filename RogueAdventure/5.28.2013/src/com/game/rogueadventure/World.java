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
	protected Player player;
	
	//Adaptive variables
	int maxEnemies, minEnemies;

	//first time the world is built
	World()
	{
	    minEnemies = 2;
	    maxEnemies = 5;
		buildWorld(); //creates the array of tiles and adds some boundaries
		createRooms(); //generates the rooms 
		addRoomsToTiles(); //converts rooms to the tiles array
		connectRooms();
	    placeStairCase();
		populateWorld(); //adds things to the world
	}
	
	World(World w)
	{
	    width = w.width + 1;
	    height = w.height +1;
        minEnemies += w.minEnemies++;
        maxEnemies += w.maxEnemies++;
        buildWorld(); //creates the array of tiles and adds some boundaries
        createRooms(); //generates the rooms 
        addRoomsToTiles(); //converts rooms to the tiles array
        connectRooms();
        placeStairCase();
        populateWorldWithMoreDifficultEnemies(w); //adds things to the world	    
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
				tiles[x][y] = new Tile(x,y,Tile_Type.WALL);
			}
		}
		for(int x = 0 ; x < width ; x++ ) //creates walls around room
		{
			for(int y = 0 ; y < height ; y++)
			{
				if( y == 0 || y == height-1 ) //if we are on the top or bottom of the grid make it a wall
				{
					tiles[x][y].type = Tile_Type.WALL;
				}
				else
				{
					if( x == 0 || x == width-1 )
					{
						tiles[x][y].type = Tile_Type.WALL;
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
				
				tiles[xTile][yTile] = new Tile(xTile,yTile);
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
			    
				tiles[xTile][yTile] = new Tile(xTile,yTile);
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
	        while(!tiles[randX][randY].isTraversableToEnemy())
	        {
	            randX = gen.nextInt(width-1) + 1;
	            randY = gen.nextInt(height-1) + 1; 
	        }
            enemies.add(new Enemy(randX,randY,this,Tile_Type.ENEMY));
            enemiesMade++;     
	    }
	    
	    for(Enemy e : enemies)
	    {
	        tiles[e.x][e.y].setType(e.type);
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
            while(!tiles[randX][randY].isTraversableToEnemy())
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
            tiles[e.x][e.y].setType(e.type);
        } 
    }
	
   private void placeStairCase()
   {
       tiles[rooms[getRoomCount()-1].centerX][rooms[getRoomCount()-1].centerY].setType(Tile_Type.STAIRS);
   }

	public Tile getTileFacing(int x, int y, Direction direction) 
	{
		 switch(direction)
         {
			 case NORTH:
			 {
				 return tiles[x][y-1];
			 }
			 case SOUTH:
			 {
				 return tiles[x][y+1]; 
			 }
			 case EAST:
			 {
				 return tiles[x+1][y];
			 }
			 case WEST:
			 {
				 return tiles[x-1][y];
			 }
			case NORTHEAST:
			{
	            return tiles[x+1][y-1];
			}
			case NORTHWEST:
			{
                return tiles[x-1][y-1];
			}
			case SOUTHEAST:
			{
                return tiles[x+1][y+1];
			}
			case SOUTHWEST:
			{
                return tiles[x-1][y+1];
			}
			default:
			{
				break;
			}
         }
		 return null;
	}

	public Tile getTile(int x, int y) 
	{
		return tiles[x][y];
	}

    public void remove(Enemy e) 
    {
        enemies.remove(e);
        getTile(e.getX(),e.getY()).setType(e.typeBeforeEntityMoved);  
    }
    
    public void setPlayer(Player p)
    {
        player = p;
    }
    
    
    //THIS IS THE A* SEARCH ALGORITHIM THAT RETURNS A PATHNODE TO A LIST WHICH CONTAINS A PATH TO FROM START TO FINISH
    public ArrayList<PathNode> getBestPathNode(PathNode start, PathNode goal)
    {
   	 ArrayList<PathNode> open = new ArrayList<PathNode>(0); //LOCATIONS WE NEED TO CHECK
   	 ArrayList<PathNode> closed = new ArrayList<PathNode>(0);//LOCATIONS THAT HAVE BEEN CHECKED
   	 PathNode currentNode = start;

   	 PathNode[] nodes = getSurroundingNodes(start); 
   	 for(PathNode n : getSurroundingNodes(start))
   	 {
   		open.add(n); 
   	 }
   	 
   	 while(!closed.contains(goal) || open.isEmpty())
   	 {
   		 currentNode = getLowestFCost( open	, goal);
   		 open.remove(currentNode);
   		 closed.add(currentNode);
   		 for(PathNode n : getSurroundingNodes(currentNode))
   		 {
   			 if(!open.contains(n))
   			 {
   				 open.add(n);
   				 currentNode = n;
   			 }
   			 else
   			 {
   				 if(currentNode.getG() < n.getG())//if current path is better
   				 {
   					 currentNode = n.parent;
   					 currentNode.calculateScore(goal);
   				 }
   			 }
   		 }
   	 }
   	 return closed;
    }
    
    
    private PathNode getLowestFCost(ArrayList<PathNode> nodes, PathNode goal)
    {
    	
    	//calculates the g value for all nodes 
    	for(PathNode n : nodes)
    	{
    		n.calculateScore(goal);
    	}

    	//return lowest cost
    	PathNode lowestCost = nodes.get(0);
    	for(PathNode n : nodes)
    	{
    		if(n.f < lowestCost.f)
    		{
    			lowestCost = n;
    		}
    	}
    	return lowestCost;
    }

	private PathNode[] getSurroundingNodes(PathNode location) 
	{
		int x = location.x;
		int y = location.y;
		int size = 0;
		int counter = 0;
		PathNode[] surroundingNodes;
		for(Direction d : Direction.values())
		{
			if(getTileFacing(x, y, d).isTraversable())
			{
				size++;
			}
		}
		surroundingNodes = new PathNode[size];
		for(Direction d : Direction.values())
		{
			if(getTileFacing(x, y, d).isTraversable())
			{
				surroundingNodes[counter] = getTileFacing(x,y,d).toPathNode(location);
				counter++;
			}
		}
		return surroundingNodes;
	}

}
